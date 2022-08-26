package com.example;

public class ServletApplication {

    public static void main(String[] args) throws Exception {
        final var tomcatStarter = new TomcatStarter();
        tomcatStarter.start();
        tomcatStarter.await();
    }
}
