package com.intellij.openapi.application;


public class ApplicationManager {

    protected static Application app = new Application();

    public static Application getApplication() {
        return app;
    }

}
