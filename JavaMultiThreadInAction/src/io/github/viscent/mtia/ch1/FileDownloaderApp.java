package io.github.viscent.mtia.ch1;

import io.github.viscent.mtia.util.Debug;
import io.github.viscent.mtia.util.Tools;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

public class FileDownloaderApp {

    public static void main(String[] args) {
        Thread downloaderThread = null;
        for (String url : args) {
            // 创建文件下载器线程
            downloaderThread = new Thread(new FileDownloader(url));
            // 启动文件下载器线程
            downloaderThread.start();
        }
    }

    // 文件下载器
    static class FileDownloader implements Runnable {
        private final String fileURL;

        public FileDownloader(String fileURL) {
            this.fileURL = fileURL;
        }

        @Override
        public void run() {
            Debug.info("Downloading from " + fileURL);
            String fileBaseName = fileURL.substring(fileURL.lastIndexOf('/') + 1);
            try {
                URL url = new URL(fileURL);
                String localFileName = System.getProperty("java.io.tmpdir")
                        + "/viscent-"
                        + fileBaseName;
                Debug.info("Saving to: " + localFileName);
                downloadFile(url, new FileOutputStream(
                        localFileName), 1024);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Debug.info("Done downloading from " + fileURL);
        }

        // 从指定的URL下载文件，并将其保存到指定的输出流中
        private void downloadFile(URL url, OutputStream outputStream, int bufSize)
                throws MalformedURLException, IOException {
            // 建立HTTP连接
            final HttpURLConnection httpConn = (HttpURLConnection) url
                    .openConnection();
            httpConn.setRequestMethod("GET");
            ReadableByteChannel inChannel = null;
            WritableByteChannel outChannel = null;
            try {
                // 获取HTTP响应码
                int responseCode = httpConn.getResponseCode();
                // HTTP响应非正常:响应码不为2开头
                if (2 != responseCode / 100) {
                    throw new IOException("Error: HTTP " + responseCode);
                }

                if (0 == httpConn.getContentLength()) {
                    Debug.info("Nothing to be downloaded " + fileURL);
                    return;
                }
                inChannel = Channels
                        .newChannel(new BufferedInputStream(httpConn.getInputStream()));
                outChannel = Channels
                        .newChannel(new BufferedOutputStream(outputStream));
                ByteBuffer buf = ByteBuffer.allocate(bufSize);
                while (-1 != inChannel.read(buf)) {
                    buf.flip();
                    outChannel.write(buf);
                    buf.clear();
                }
            } finally {
                // 关闭指定的Channel以及HttpURLConnection
                Tools.silentClose(inChannel, outChannel);
                httpConn.disconnect();
            }
        }// downloadFile结束
    }// FileDownloader结束
}