package com.github.fbrandes.library.openlibrary.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.http.HttpClient;

@Configuration
public class OpenLibraryConfig {
    @Bean
    public HttpClient httpClient() {
        return HttpClient.newBuilder().build();
    }
}
