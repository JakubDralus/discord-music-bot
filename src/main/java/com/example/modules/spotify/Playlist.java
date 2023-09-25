package com.example.modules.spotify;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import se.michaelthelin.spotify.model_objects.specification.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;


@Getter
public class Playlist {
    // Rat Party mix 2023
    // uri and url:
    // spotify:playlist:0RHhiQ6hGLKgjE7eqNdXzh
    // https://open.spotify.com/playlist/0RHhiQ6hGLKgjE7eqNdXzh?si=203a1096f9b64e27
    
    private static final String ratPartyMix2023id = "0RHhiQ6hGLKgjE7eqNdXzh";
    private static final Map<Integer, String> tracks = new HashMap<>();
    
    public static Map<Integer, String> getTracks() {
        return tracks;
    }
    
    // todo: make this when endpoint in RatPartyMixTracker is done
    public String getDailySongId() {
        String url = "";
        String dailySongId = "";
    
        // Create an HTTP request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response.body());
    
            return rootNode.get("id").toString();
        }
        catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // ------------------------------ from github example ----------------------------
    public static void getPlaylistsItems_Async() {
        SpotifyToken.clientCredentials_Async(); // get token
        int offset = 0;
    
        try {
            boolean morePages = true;
            
            while (morePages) {
                final CompletableFuture<Paging<PlaylistTrack>> pagingFuture = SpotifyApiInstance.getSpotifyApi()
                        .getPlaylistsItems(ratPartyMix2023id)
                        .limit(100)
                        .offset(offset)
                        .build()
                        .executeAsync();
                
                Paging<PlaylistTrack> playlistTrackPaging = pagingFuture.join();
                
                loadTracks(playlistTrackPaging.getItems(), offset);
                
                // Check if there are more pages
                morePages = playlistTrackPaging.getNext() != null;
                offset += 100;
            }
        }
        catch (CompletionException e) {
            System.out.println("Error: " + e.getCause().getMessage());
        }
        catch (CancellationException e) {
            System.out.println("Async operation cancelled.");
        }
    }
    
    private static void loadTracks(PlaylistTrack[] tracks, int offset) {
        for (var track: tracks) {
            StringBuilder song = new StringBuilder();
            song.append(" ").append(track.getTrack().getName()).append(" - ");
            
            for (var artist: ((Track) track.getTrack()).getArtists()) {
                song.append(artist.getName()).append(", ");
            }
            
            song.setLength(song.length() - 2); //remove last `, `
            String songStr = song.toString().replace("\"", ""); // remove quotes from json

            Playlist.tracks.put(++offset, songStr);
            
            //System.out.println(offset + " " + songStr);
        }
    }
    
    // ---------------------------------------------
    
    // @Deprecated
    public static void getPlaylistItems() {
        String spotifyToken = SpotifyToken.getToken();
        String url = "https://api.spotify.com/v1/playlists/" + ratPartyMix2023id + "/tracks";
        HttpClient httpClient = HttpClient.newHttpClient();
        ObjectMapper objectMapper = new ObjectMapper();
        
        // Create an HTTP request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + spotifyToken)
                .GET()
                .build();
        
        // Send the request asynchronously
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            JsonNode rootNode = objectMapper.readTree(response.body());
            
            displayTracks(rootNode.get("items"), 0);
            url = rootNode.get("next").toString().replace("\"", "");
        }
        catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        
        
        HttpRequest nextRequest = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + spotifyToken)
                .GET()
                .build();
        
        try {
            HttpResponse<String> response = httpClient.send(nextRequest, HttpResponse.BodyHandlers.ofString());
            JsonNode rootNode = objectMapper.readTree(response.body());
            
            displayTracks(rootNode.get("items"), 100);
        }
        catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    // @Deprecated
    private static void displayTracks(JsonNode items, int offset) {
        for (var track: items) {
            StringBuilder song = new StringBuilder();
            song.append(" ").append(track.get("track").get("name")).append(" - ");
            
            for (var artist: track.get("track").get("artists")) {
                song.append(artist.get("name")).append(", ");
            }
            song.setLength(song.length() - 2); //remove last `, `
            String songStr = song.toString().replace("\"", ""); // remove quotes from json
            
            tracks.put(++offset, songStr);
            System.out.println(offset + " " + songStr);
        }
    }
}
