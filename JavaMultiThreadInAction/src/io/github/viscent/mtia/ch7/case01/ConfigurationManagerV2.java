package io.github.viscent.mtia.ch7.case01;

import io.github.viscent.mtia.util.Tools;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public enum ConfigurationManagerV2 {
    INSTANCE;
    protected final Set<ConfigEventListener> listeners;

    {
        listeners = new CopyOnWriteArraySet<ConfigEventListener>();
    }

    public Configuration load(String name) {
        Configuration cfg = loadConfigurationFromDB(name);
        for (ConfigEventListener listener : listeners) {
            listener.onConfigLoaded(cfg);
        }
        return cfg;
    }

    private Configuration loadConfigurationFromDB(String name) {
        // 模拟从数据库加载配置数据
        Tools.randomPause(50);
        Configuration cfg = new Configuration(name, 0);
        cfg.setProperty("url", "https://github.com/Viscent");
        cfg.setProperty("connectTimeout", "2000");
        cfg.setProperty("readTimeout", "2000");
        return cfg;
    }

    public void registerListener(ConfigEventListener listener) {
        listeners.add(listener);
    }

    public void removeListener(ConfigEventListener listener) {
        listeners.remove(listener);
    }

    public void update(String name, int newVersion,
                       Map<String, String> properties) {
        for (ConfigEventListener listener : listeners) {
            listener.onConfigUpdated(name, newVersion, properties);
        }
    }
}