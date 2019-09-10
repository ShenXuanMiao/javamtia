package io.github.viscent.mtia.ch7.case01;

import java.util.HashMap;
import java.util.Map;

/**
 * 配置实体.
 *
 * @author Viscent Huang
 */
//非线程安全
public class Configuration {
    /**
     * 配置名称
     */
    private final String name;
    /**
     * 配置当前版本号
     */
    private volatile int version;
    /**
     * 存储配置项的Map. 每个配置项是一个“属性名”->“属性值”的关联。
     */
    private volatile Map<String, String> configItemMap;

    public Configuration(String name, int version) {
        this.name = name;
        this.version = version;
        configItemMap = new HashMap<String, String>();
    }

    public String getName() {
        return name;
    }

    public void setProperty(String key, String value) {
        configItemMap.put(key, value);
    }

    public String getProperty(String key) {
        return configItemMap.get(key);
    }

    public void update(Map<String, String> properties) {
        configItemMap = properties;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (name == null ? 0 : name.hashCode());
        result = prime * result + version;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Configuration other = (Configuration) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (version != other.version)
            return false;
        return true;
    }
}