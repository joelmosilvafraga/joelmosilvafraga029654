package com.joelmofraga.artists_albums_api.config;

public final class ApiPaths {

    private ApiPaths() {}

    public static final String BASE = "/api";
    public static final String V1 = BASE + "/v1";


    public static final String AUTH = V1 + "/auth";
    public static final String ARTISTS = V1 + "/artists";
    public static final String ALBUMS = V1 + "/albums";
    public static final String TRACKS = V1 + "/tracks";
    public static final String MEDIA = V1 + "/media";
}