package com.bot.modules.spotify;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.PlaylistTrack;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.requests.data.tracks.GetTrackRequest;

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
    
    private static String getDailySongId() {
        String url = "http://130.162.243.45:8443/ratpartymix/dailysong";
    
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
    
            System.out.println("id: " + rootNode.get("SpotifyID").toString());
            return rootNode.get("SpotifyID").toString().replace("\"", ""); // remove quotes from json;
        }
        catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static String getDailySongName() {
        SpotifyToken.clientCredentials_Async();
        SpotifyApi spotifyApi = SpotifyApiInstance.getSpotifyApi();
        final GetTrackRequest getTrackRequest = spotifyApi.getTrack(getDailySongId()).build();
        
        try {
            final CompletableFuture<Track> trackFuture = getTrackRequest.executeAsync();
            final Track track = trackFuture.join();
            
            StringBuilder trackName = new StringBuilder();
            trackName.append(track.getName()).append(" - ");
            for (var artist: track.getArtists()) {
                trackName.append(artist.getName()).append(", ");
            }
            trackName.setLength(trackName.length() - 2); //remove last `, `
            
            System.out.println(trackName);
            return trackName.toString();
        }
        catch (CompletionException e) {
            System.out.println("Error: " + e.getCause().getMessage());
        }
        catch (CancellationException e) {
            System.out.println("Async operation cancelled.");
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
}
