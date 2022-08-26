package com.example;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class TestHttpUtils {

    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofSeconds(3))
            .build();

    public static TomcatStarter createTomcatStarter() {
        return new TomcatStarter("../servlet/src/main/webapp/");
    }

    public static HttpResponse<String> send(final String path) throws Exception {
        final var request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080" + path))
                .timeout(Duration.ofSeconds(3))
                .build();

        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }
}
