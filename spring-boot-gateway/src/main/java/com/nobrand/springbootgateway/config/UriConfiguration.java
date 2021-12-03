package com.nobrand.springbootgateway.config;

import org.springframework.context.annotation.Configuration;


@Configuration
public class UriConfiguration {

    private String httpBin = "http://httpbin.org:80";

    public String getHttpBin() {
        return this.httpBin;
    }

    public void setHttpBin(String httpBin) {
        this.httpBin = httpBin;
    }

}
