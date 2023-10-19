package com.example.modules.spotify;

import se.michaelthelin.spotify.SpotifyApi;


public class SpotifyApiInstance {
    private static final String CLIENT_ID = "2b6c5d4d81a642078c86ca9d49f2f574";
    private static SpotifyApi SPOTIFY_API;
    
    public static SpotifyApi getSpotifyApi() {
        return SPOTIFY_API;
    }
    
    public static void initSpotifyApi(String clientSecret) {
        SPOTIFY_API = new SpotifyApi.Builder()
                .setClientId(CLIENT_ID)
                .setClientSecret(clientSecret)
                .build();
    }
    
    private SpotifyApiInstance() {
    }
}
