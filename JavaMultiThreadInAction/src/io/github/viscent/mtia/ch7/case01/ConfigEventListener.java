package io.github.viscent.mtia.ch7.case01;

import java.util.Map;

/**
 * 事件监听器接口.
 *
 * @author Viscent Huang
 */
public interface ConfigEventListener {
    void onConfigLoaded(Configuration cfg);

    void onConfigUpdated(String name, int newVersion,
                         Map<String, String> properties);
}
