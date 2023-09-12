package com.example.spotify;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.credentials.ClientCredentials;
import se.michaelthelin.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;


@Getter
@RequiredArgsConstructor
public class SpotifyToken {
    
    // from example file in github
    public static void clientCredentials_Async() {
        SpotifyApi spotifyApi = SpotifyApiInstance.getSpotifyApi();
        final ClientCredentialsRequest clientCredentialsRequest = spotifyApi.clientCredentials().build();
        
        try {
            final CompletableFuture<ClientCredentials> clientCredentialsFuture = clientCredentialsRequest.executeAsync();
            
            // Example Only. Never block in production code.
            final ClientCredentials clientCredentials = clientCredentialsFuture.join();
            
            // Set access token for further "spotifyApi" object usage
            spotifyApi.setAccessToken(clientCredentials.getAccessToken());
        }
        catch (CompletionException e) {
            System.out.println("Error: " + e.getCause().getMessage());
        }
        catch (CancellationException e) {
            System.out.println("Async operation cancelled.");
        }
    }
    
    
    public static String getToken() {
        // Define the Spotify API endpoint and request data
        String url = "https://accounts.spotify.com/api/token";
        String requestBody = "grant_type=client_credentials" +
                "&client_id=2b6c5d4d81a642078c86ca9d49f2f574" +
                "&client_secret=0f235a01e6134be9a323ecfe3d02706c";

        HttpClient httpClient = HttpClient.newHttpClient();
        
        // Create an HTTP request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        
        // Send the request asynchronously
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response.body());
            return rootNode.get("access_token").toString().replace("\"", "");
        }
        catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
