package com.example;

import java.io.File;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;

public class TomcatStarter {

    private static final String WEBAPP_DIR_LOCATION = "servlet/src/main/webapp/";

    private final Tomcat tomcat;

    public TomcatStarter() {
        this(WEBAPP_DIR_LOCATION);
    }

    public TomcatStarter(final String webappDirLocation) {
        this.tomcat = new Tomcat();
        tomcat.setConnector(createConnector());

        final var docBase = new File(webappDirLocation).getAbsolutePath();
        tomcat.addWebapp("", docBase);
    }

    public void start() throws LifecycleException {
        tomcat.start();
    }

    public void await() {
        tomcat.getServer().await();
    }

    public void stop() throws LifecycleException {
        tomcat.stop();
    }

    private Connector createConnector() {
        final var connector = new Connector();
        connector.setPort(8080);
        connector.setProperty("bindOnInit", "false");
        return connector;
    }
}
