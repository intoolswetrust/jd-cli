package com.intellij.openapi.application;

import com.intellij.openapi.application.Application;

public class ApplicationManager {
    protected static Application app = new Application();

    public static Application getApplication() {
        return app;
    }
}
