package com.drone.poc.context;

import io.cucumber.spring.ScenarioScope;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * This class stores context data of a scenario.
 * The data stored in the context is shared among all step definitions of a scenario during the execution of that scenario.
 */
@Component
@ScenarioScope
public class ScenarioContext {
    private final HashMap<ContextItem, Object> contextData;

    /**
     * Creates new context.
     */
    public ScenarioContext() {
        contextData = new HashMap<>();
    }

    /**
     * Get an item stored in the context data.
     *
     * @param contextItem item key
     * @return stored item or null if item key does not exist in the context
     */
    public Object getItem(ContextItem contextItem) {
        return contextData.get(contextItem);
    }

    /**
     * Set an item in the context data.
     *
     * @param contextItem item key
     * @param value       item value
     */
    public void setItem(ContextItem contextItem, Object value) {
        contextData.put(contextItem, value);
    }
}