package io.github.viscent.mtia.ch6.so;

import java.util.HashMap;
import java.util.Map;

enum UnsafeSingleton {
    INSTANCE;

    public int state1;

    public int doSomething(String s) {
        // 省略其他代码

        // 访问state1
        return 0;
    }
}

public class BrokenStatelessObject {
    public String doSomething(String s) {
        UnsafeSingleton us = UnsafeSingleton.INSTANCE;
        int i = us.doSomething(s);
        UnsafeStatefullObject sfo = new UnsafeStatefullObject();
        String str = sfo.doSomething(s, i);
        return str;
    }

    public String doSomething1(String s) {
        UnsafeSingleton us = UnsafeSingleton.INSTANCE;
        UnsafeStatefullObject sfo = new UnsafeStatefullObject();
        String str;
        synchronized (this) {
            str = sfo.doSomething(s, us.doSomething(s));
        }
        return str;
    }
}

class UnsafeStatefullObject {
    static Map<String, String> cache = new HashMap<String, String>();

    public String doSomething(String s, int len) {
        String result = cache.get(s);
        if (null == result) {
            result = md5sum(result, len);
            cache.put(s, result);
        }
        return result;
    }

    public String md5sum(String s, int len) {
        // 生成md5摘要
        // 省略其他代码
        return s;
    }
}
