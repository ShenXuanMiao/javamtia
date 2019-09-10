package io.github.viscent.mtia.ch9;

import io.github.viscent.mtia.util.Debug;
import io.github.viscent.mtia.util.Tools;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPReply;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;

public class FileBatchUploader implements Closeable {
    private final String ftpServer;
    private final String userName;
    private final String password;
    private final String targetRemoteDir;
    private final FTPClient ftp = new FTPClient();
    private final CompletionService<File> completionService;
    private final ExecutorService es;
    private final ExecutorService dispatcher;

    public FileBatchUploader(String ftpServer, String userName, String password,
                             String targetRemoteDir) {
        this.ftpServer = ftpServer;
        this.userName = userName;
        this.password = password;
        this.targetRemoteDir = targetRemoteDir;
        // 使用单工作者线程的线程池
        this.es = Executors.newSingleThreadExecutor();
        this.dispatcher = Executors.newSingleThreadExecutor();
        this.completionService = new ExecutorCompletionService<File>(es);
    }

    private static File moveToSuccessDir(File file) {
        File targetFile = null;
        try {
            targetFile = moveFile(file, Paths.get(file.getParent(), "..", "backup", "success"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return targetFile;
    }

    private static File moveToDeadDir(File file) {
        File targetFile = null;
        try {
            targetFile = moveFile(file, Paths.get(file.getParent(), "..", "backup", "dead"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return targetFile;
    }

    private static File moveFile(File srcFile, Path destPath) throws IOException {
        Path sourcePath = Paths.get(srcFile.getAbsolutePath());
        if (!Files.exists(destPath)) {
            Files.createDirectories(destPath);
        }
        Path destFile = destPath.resolve(srcFile.getName());
        Files.move(sourcePath, destFile,
                StandardCopyOption.REPLACE_EXISTING);
        return destFile.toFile();
    }

    public void uploadFiles(final Set<File> files) {
        dispatcher.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    doUploadFiles(files);
                } catch (InterruptedException ignored) {
                }
            }
        });
    }

    private void doUploadFiles(Set<File> files) throws InterruptedException {
        // 批量提交文件上传任务
        for (final File file : files) {
            completionService.submit(new UploadTask(file));
        }

        Future<File> future;
        File md5File;
        File uploadedFile;
        Set<File> md5Files = new HashSet<File>();
        for (File file : files) {
            try {
                future = completionService.take();
                uploadedFile = future.get();
                // 将上传成功的文件移动到备份目录，并为其生成相应的MD5文件
                md5File = generateMD5(moveToSuccessDir(uploadedFile));
                md5Files.add(md5File);
            } catch (ExecutionException | IOException | NoSuchAlgorithmException e) {
                e.printStackTrace();
                moveToDeadDir(file);
            }
        }
        for (File file : md5Files) {
            // 上传相应的MD5文件
            completionService.submit(new UploadTask(file));
        }
        // 检查md5文件的上传结果
        int successUploaded = md5Files.size();
        for (int i = 0; i < successUploaded; i++) {
            future = completionService.take();
            try {
                uploadedFile = future.get();
                md5Files.remove(uploadedFile);
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        // 将剩余（即未上传成功）的md5文件移动到相应备份目录
        for (File file : md5Files) {
            moveToDeadDir(file);
        }
    }

    private File generateMD5(File file) throws IOException, NoSuchAlgorithmException {
        String md5 = Tools.md5sum(file);
        File md5File = new File(file.getAbsolutePath() + ".md5");
        Files.write(Paths.get(md5File.getAbsolutePath()), md5.getBytes("UTF-8"));
        return md5File;
    }

    // 初始化FTP客户端
    public void init() throws Exception {
        FTPClientConfig config = new FTPClientConfig();
        ftp.configure(config);
        int reply;
        ftp.connect(ftpServer);
        Debug.info("FTP Reply:%s", ftp.getReplyString());
        reply = ftp.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            ftp.disconnect();
            throw new Exception("FTP server refused connection.");
        }
        boolean isOK = ftp.login(userName, password);
        if (isOK) {
            Debug.info("FTP Reply:%s", ftp.getReplyString());
        } else {
            throw new Exception("Failed to login." + ftp.getReplyString());
        }
        reply = ftp.cwd(targetRemoteDir);
        if (!FTPReply.isPositiveCompletion(reply)) {
            ftp.disconnect();
            throw new Exception("Failed to change working directory.reply:"
                    + reply);
        } else {
            Debug.info("FTP Reply:%s", ftp.getReplyString());
        }
        ftp.setFileType(FTP.ASCII_FILE_TYPE);
    }

    // 将指定的文件上传至FTP服务器
    protected void upload(File file) throws Exception {
        boolean isOK;
        try (InputStream dataIn = new BufferedInputStream(new FileInputStream(file))) {
            isOK = ftp.storeFile(file.getName(), dataIn);
        }
        if (!isOK) {
            throw new IOException("Failed to upload " + file + ",reply:" + ","
                    + ftp.getReplyString());
        }
    }

    @Override
    public void close() throws IOException {
        dispatcher.shutdown();
        try {
            es.awaitTermination(60, TimeUnit.SECONDS);
        } catch (InterruptedException ignored) {
        }
        es.shutdown();
        try {
            es.awaitTermination(60, TimeUnit.SECONDS);
        } catch (InterruptedException ignored) {
        }
        Tools.silentClose(new Closeable() {
            @Override
            public void close() throws IOException {
                if (ftp.isConnected()) {
                    ftp.disconnect();
                }
            }
        });
    }

    class UploadTask implements Callable<File> {
        private final File file;

        public UploadTask(File file) {
            this.file = file;
        }

        @Override
        public File call() throws Exception {
            Debug.info("uploading %s", file.getCanonicalPath());
            // 上传指定的文件
            upload(file);
            return file;
        }
    }
}
