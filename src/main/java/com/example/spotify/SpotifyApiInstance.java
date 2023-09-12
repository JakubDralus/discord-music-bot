package com.example.spotify;

import se.michaelthelin.spotify.SpotifyApi;


public class SpotifyApiInstance {
    private static final String clientId = "2b6c5d4d81a642078c86ca9d49f2f574";
    private static final String clientSecret = "0f235a01e6134be9a323ecfe3d02706c";
    
    private static final SpotifyApi spotifyApi = new SpotifyApi.Builder()
            .setClientId(clientId)
            .setClientSecret(clientSecret)
            .build();
    
    public static SpotifyApi getSpotifyApi() {
        return spotifyApi;
    }
}
