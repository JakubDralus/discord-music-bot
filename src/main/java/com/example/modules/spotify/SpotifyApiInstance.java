package com.example.modules.spotify;

import se.michaelthelin.spotify.SpotifyApi;


public class SpotifyApiInstance {
    private static final String CLIENT_ID = "2b6c5d4d81a642078c86ca9d49f2f574";
    private static final String CLIENT_SECRET = "0f235a01e6134be9a323ecfe3d02706c"; //todo: refresh and hide it
    
    private static final SpotifyApi SPOTIFY_API;
    
    static {
        SPOTIFY_API = new SpotifyApi.Builder()
                .setClientId(CLIENT_ID)
                .setClientSecret(CLIENT_SECRET)
                .build();
    }
    
    public static SpotifyApi getSpotifyApi() {
        return SPOTIFY_API;
    }
    
    private SpotifyApiInstance() {
    }
}
