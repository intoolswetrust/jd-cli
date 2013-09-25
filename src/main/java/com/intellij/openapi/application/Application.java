package com.intellij.openapi.application;

import jd.ide.intellij.config.JDPluginComponent;

import com.intellij.openapi.components.ComponentManager;

public class Application implements ComponentManager {

	private static final JDPluginComponent jdpc = new JDPluginComponent();

	@SuppressWarnings("unchecked")
	public <T> T getComponent(Class<T> interfaceClass) {
		return (T) (jdpc);
	}

}
