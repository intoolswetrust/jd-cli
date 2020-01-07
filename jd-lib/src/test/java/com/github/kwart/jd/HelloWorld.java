package com.github.kwart.jd;

/**
 * Sample test application.
 */
public final class HelloWorld {

    private final PrivateClass priv;

    private HelloWorld() {
        System.out.println("ctor");
        priv = new PrivateClass();
        priv.test();
    }

    public static void main(String[] args) {
        System.out.println("xxx");
        StaticClass.test();
    }

    /**
     * Static test class.
     */
    public static class StaticClass {
        public static String test() {
            return "static";
        }
    }

    /**
     * Private test class.
     */
    private class PrivateClass {
        public String test() {
            return "private";
        }
    }
}
