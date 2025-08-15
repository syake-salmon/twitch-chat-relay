package com.syakeapps.tcr.util;

import java.net.URI;

public class UriBuilder {
    private String scheme = "http";
    private String host;
    private int port = -1;
    private String path = "";
    private final StringBuilder query = new StringBuilder();

    private UriBuilder() {
        // NOP
    }

    public static UriBuilder create() {
        return new UriBuilder();
    }

    public static UriBuilder create(String uri) {
        UriBuilder builder = new UriBuilder();
        
        try {
            java.net.URI parsedUri = new java.net.URI(uri);
            if (parsedUri.getScheme() != null) {
                builder.scheme = parsedUri.getScheme();
            }
            if (parsedUri. getHost() != null) {
                builder.host = parsedUri.getHost();
            }
            if (parsedUri.getPort() != -1) {
                builder.port = parsedUri.getPort();
            }
            if (parsedUri.getPath() != null && !parsedUri.getPath().isEmpty()) {
                builder.path = parsedUri.getPath();
            }
            if (parsedUri.getQuery() != null && !parsedUri.getQuery().isEmpty()) {
                builder.query.append(parsedUri.getQuery());
            }
        } catch (java.net.URISyntaxException e) {
            throw new IllegalArgumentException("Invalid URI: " + uri, e);
        }

        return builder;
    }

    public UriBuilder scheme(String scheme) {
        this.scheme = scheme;
        return this;
    }

    public UriBuilder host(String host) {
        this.host = host;
        return this;
    }

    public UriBuilder port(int port) {
        this.port = port;
        return this;
    }

    public UriBuilder path(String path) {
        if (path != null) {
            if (!path.startsWith("/")) {
                path = "/" + path;
            }

            if (this.path.endsWith("/")) {
                this.path = this.path.substring(0, this.path.length() - 1);
            }
            
            this.path += path;
        }
        return this;
    }

    public UriBuilder queryParam(String key, String value) {
        if (query.length() > 0) {
            query.append("&");
        }
        query.append(key).append("=").append(value);
        return this;
    }

    public URI build() {
        StringBuilder sb = new StringBuilder();
        sb.append(scheme).append("://").append(host);
        if (port > 0) {
            sb.append(":").append(port);
        }
        sb.append(path);
        if (query.length() > 0) {
            sb.append("?").append(query);
        }
        return URI.create(sb.toString());
    }

}
