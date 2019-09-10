package io.github.viscent.mtia.ch12;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class SectionBasedStorageV1 {
    private Deque<String> sectionNames = new LinkedList<String>();
    // Key->value: 存储子目录名->子目录下缓存文件计数器
    private Map<String, AtomicInteger> sectionFileCountMap = new HashMap<String, AtomicInteger>();
    private int maxFilesPerSection = 2000;
    private int maxSectionCount = 100;
    private String storageBaseDir = System.getProperty("java.io.tmpdir") + "/vpn";

    public SectionBasedStorageV1() {
        File dir = new File(storageBaseDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public synchronized String[] apply4Filename() {
        String sectionName;
        int iFileCount;
        String[] fileName = new String[2];
        // 获取当前的存储子目录名
        sectionName = getSectionName();
        AtomicInteger fileCount;
        fileCount = sectionFileCountMap.get(sectionName);
        iFileCount = fileCount.get();
        // 当前存储子目录已满
        if (iFileCount >= maxFilesPerSection) {
            if (sectionNames.size() >= maxSectionCount) {
                // 删除最老的存储子目录
                String oldestSectionName = sectionNames.removeFirst();
                removeSection(oldestSectionName);
            }
            // 创建新的存储子目录
            sectionName = makeNewSectionDir();
            fileCount = sectionFileCountMap.get(sectionName);
        }
        iFileCount = fileCount.incrementAndGet();
        fileName[0] = storageBaseDir + "/" + sectionName + "/"
                + new DecimalFormat("0000").format(iFileCount) + "-"
                + new Date().getTime() / 1000 + ".rq";
        fileName[1] = sectionName;
        return fileName;
    }

    public void decrementSectionFileCount(String sectionName) {
        AtomicInteger fileCount = sectionFileCountMap.get(sectionName);
        if (null != fileCount) {
            fileCount.decrementAndGet();
        }
    }

    @SuppressFBWarnings(value = "NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE",
            justification = "忽略这种可能性")
    private boolean removeSection(String sectionName) {
        boolean result = true;
        File dir = new File(storageBaseDir + "/" + sectionName);
        for (File file : dir.listFiles()) {
            result = result && file.delete();
        }
        result = result && dir.delete();
        return result;
    }

    private String getSectionName() {
        String sectionName;
        if (sectionNames.isEmpty()) {
            sectionName = makeNewSectionDir();
        } else {
            sectionName = sectionNames.getLast();
        }
        return sectionName;
    }

    private String makeNewSectionDir() {
        String sectionName;
        SimpleDateFormat sdf = new SimpleDateFormat("MMddHHmmss");
        sectionName = sdf.format(new Date());
        File dir = new File(storageBaseDir + "/" + sectionName);
        if (dir.mkdir()) {
            sectionNames.addLast(sectionName);
            sectionFileCountMap.put(sectionName, new AtomicInteger(0));
        } else {
            throw new RuntimeException("Cannot create section dir " + sectionName);
        }
        return sectionName;
    }
}