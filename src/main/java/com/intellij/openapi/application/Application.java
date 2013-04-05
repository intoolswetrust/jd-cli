package com.intellij.openapi.application;

import com.intellij.openapi.components.ComponentManager;
import jd.ide.intellij.config.JDPluginComponent;

public class Application implements ComponentManager {
	
	@SuppressWarnings("unchecked")
	public <T> T getComponent(Class<T> interfaceClass){
		return (T) (new JDPluginComponent());
	}
	
}
