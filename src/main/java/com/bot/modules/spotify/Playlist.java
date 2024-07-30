package com.bot.modules.spotify;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
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
    // https://open.spotify.com/playlist/0RHhiQ6hGLKgjE7eqNdXzh
    
    private static final String ratPartyMixApiUrl = "http://130.61.63.141:8888/api/v1/dailysong/get";
    private static final String ratPartyMix2023id = "0RHhiQ6hGLKgjE7eqNdXzh";
    
    @Getter
    private static final Map<Integer, String> tracks = new HashMap<>();
    
    public static String getDailySongId() {
        final String authToken = DailySongToken.getToken();
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ratPartyMixApiUrl))
                .header("Authorization", "Bearer " + authToken)
                .GET()
                .build();
        
        HttpResponse<String> response = null;
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            
            JsonNode rootNode = new ObjectMapper().readTree(response.body());
    
//            System.out.println("id: " + rootNode.get("spotify_id").toString());
            return rootNode.get("spotify_id").toString().replace("\"", ""); // remove quotes from json;
        }
        catch (InterruptedException | IOException e) {
            if (response != null ) {
                System.out.println("response body: " + response.body());
            }
            e.printStackTrace();
        }
        return null;
    }
    
    public static String getDailySongName() {
        // this line can be omitted because the token is always valid (refreshed every hour)
//        SpotifyToken.clientCredentials_Async();
        SpotifyApi spotifyApi = SpotifyApiInstance.get();
        
        try {
            final GetTrackRequest getTrackRequest = spotifyApi.getTrack(getDailySongId()).build();
            final CompletableFuture<Track> trackFuture = getTrackRequest.executeAsync();
            final Track track = trackFuture.join();
            
            StringBuilder trackName = new StringBuilder();
            trackName.append(track.getName()).append(" - ");
            for (var artist: track.getArtists()) {
                trackName.append(artist.getName()).append(", ");
            }
            trackName.setLength(trackName.length() - 2); //remove last `, `
            
            System.out.println("daily song: " + trackName);
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
    
    public static void getPlaylistsItems_Async() {
//        SpotifyToken.clientCredentials_Async(); // get token
        int offset = 0;
        try {
            boolean morePages = true;
            
            while (morePages) {
                final CompletableFuture<Paging<PlaylistTrack>> pagingFuture = SpotifyApiInstance.get()
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
        }
    }
    
    public static String getTrackImage_Sync(String id) {
        SpotifyApi spotifyApi = SpotifyApiInstance.get();
        GetTrackRequest getTrackRequest = spotifyApi.getTrack(id).build();
        
        try {
            final Track track = getTrackRequest.execute();
            return track.getAlbum().getImages()[0].getUrl();
        }
        catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return "";
    }
}
