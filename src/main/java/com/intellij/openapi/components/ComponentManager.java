package com.intellij.openapi.components;

public interface ComponentManager {
    public <T> T getComponent(Class<T> interfaceClass);
}
