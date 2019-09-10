package io.github.viscent.mtia.ch9;

import io.github.viscent.mtia.util.Debug;
import io.github.viscent.mtia.util.Tools;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.*;

/**
 * 支持异步解析器的XML解析器。
 *
 * @author Viscent Huang
 */
public class XMLDocumentParser {

    public static ParsingTask newTask(InputStream in) {
        return new ParsingTask(in);
    }

    public static ParsingTask newTask(URL url) throws IOException {
        return newTask(url, 30000, 30000);
    }

    public static ParsingTask newTask(String strURL) throws IOException {
        URL url = new URL(strURL);
        return newTask(url);
    }

    public static ParsingTask newTask(URL url, int connectTimeout, int readTimeout)
            throws IOException {
        URLConnection conn = url.openConnection();
        conn.setConnectTimeout(connectTimeout);
        conn.setReadTimeout(readTimeout);
        InputStream in = conn.getInputStream();
        return newTask(in);
    }

    public static ParsingTask newTask(String strURL, int connectTimeout, int readTimeout)
            throws IOException {
        URL url = new URL(strURL);
        return newTask(url, connectTimeout, readTimeout);
    }

    // 封装对XML解析结果进行处理的回调方法
    public abstract static class ResultHandler {
        abstract void onSuccess(Document document);

        void onError(Throwable e) {
            e.printStackTrace();
        }
    }

    public static class ParsingTask {
        private final InputStream in;
        private volatile Executor executor;
        private volatile ResultHandler resultHandler;

        public ParsingTask(InputStream in, Executor executor, ResultHandler resultHandler) {
            this.in = in;
            this.executor = executor;
            this.resultHandler = resultHandler;
        }

        public ParsingTask(InputStream in) {
            this(in, null, null);
        }

        static Document doParse(InputStream in) throws Exception {
            Document document = null;
            try {
                DocumentBuilder db = DocumentBuilderFactory.newInstance()
                        .newDocumentBuilder();
                document = db.parse(in);
            } finally {
                Tools.silentClose(in);
            }
            return document;
        }

        public Future<Document> execute() throws Exception {
            FutureTask<Document> ft;

            final Callable<Document> task = new Callable<Document>() {
                @Override
                public Document call() throws Exception {
                    return doParse(in);
                }
            };
            final Executor theExecutor = executor;
            // 解析模式：异步/同步
            final boolean isAsyncParsing = null != theExecutor;
            final ResultHandler rh;
            if (isAsyncParsing && null != (rh = resultHandler)) {
                ft = new FutureTask<Document>(task) {
                    @Override
                    protected void done() {
                        // 回调ResultHandler的相关方法对XML解析结果进行处理
                        callbackResultHandler(this, rh);
                    }
                };// FutureTask匿名类结束
            } else {
                ft = new FutureTask<Document>(task);
            }

            if (isAsyncParsing) {
                theExecutor.execute(ft);// 交给Executor执行，以支持异步执行
            } else {
                ft.run();// 直接（同步）执行
            }
            return ft;
        }

        void callbackResultHandler(FutureTask<Document> ft, ResultHandler rh) {
            // 获取任务处理结果前判断任务是否被取消
            if (ft.isCancelled()) {
                Debug.info("parsing cancelled.%s", ParsingTask.this);
                return;
            }
            try {
                Document doc = ft.get();
                rh.onSuccess(doc);
            } catch (InterruptedException ignored) {
                Debug.info("retrieving result cancelled.%s", ParsingTask.this);
            } catch (ExecutionException e) {
                rh.onError(e.getCause());
            }
        }

        public ParsingTask setExecutor(Executor executor) {
            this.executor = executor;
            return this;
        }

        public ParsingTask setResultHandler(ResultHandler resultHandler) {
            this.resultHandler = resultHandler;
            return this;
        }
    }// ParsingTask定义结束
}
