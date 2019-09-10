package io.github.viscent.mtia.ch9;

import io.github.viscent.mtia.ch9.XMLDocumentParser.ResultHandler;
import io.github.viscent.mtia.util.Debug;
import io.github.viscent.mtia.util.Tools;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class XMLDocumentParserUsage {
    static ExecutorService es = Executors.newSingleThreadExecutor();

    public static void main(String[] args) throws Exception {
        final int argc = args.length;
        URL url = argc > 0 ? new URL(args[0]) : XMLDocumentParserUsage.class.getClassLoader()
                .getResource("data/ch9/feed");

        syncParse(url);
        asyncParse1(url);
        asyncParse2(url);
        Tools.delayedAction("The ExecutorService will be shutdown", new Runnable() {
            @Override
            public void run() {
                es.shutdown();
            }
        }, 70);
    }

    private static void syncParse(URL url) throws Exception {
        Future<Document> future;
        future = XMLDocumentParser.newTask(url).execute();
        process(future.get());// 直接获取解析结果进行处理
    }

    private static void asyncParse1(URL url) throws Exception {
        XMLDocumentParser.newTask(url).setExecutor(es).setResultHandler(
                new ResultHandler() {
                    @Override
                    public void onSuccess(Document document) {
                        process(document);
                    }
                }).execute();

    }

    private static void asyncParse2(URL url) throws Exception {

        Future<Document> future = XMLDocumentParser.newTask(url).setExecutor(es).execute();
        doSomething();// 执行其他操作
        process(future.get());

    }

    private static void doSomething() {
        Tools.randomPause(2000);
    }

    private static void process(Document document) {
        Debug.info(queryTitle(document));
    }

    private static String queryTitle(Document document) {
        Element eleRss = (Element) document.getFirstChild();
        Element eleChannel = (Element) eleRss.getElementsByTagName("channel")
                .item(0);
        Node ndTtile = eleChannel.getElementsByTagName("title").item(0);
        String title = ndTtile.getFirstChild().getNodeValue();
        return title;
    }

}
