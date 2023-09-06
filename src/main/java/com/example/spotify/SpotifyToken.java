package com.example.spotify;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


@Getter
@RequiredArgsConstructor
public class SpotifyToken {
    
    private static String token;
    
    //todo dodac scheduler
    public static void aquireToken() {
        // Define the Spotify API endpoint and request data
        String url = "https://accounts.spotify.com/api/token";
        String requestBody = "grant_type=client_credentials" +
                "&client_id=2b6c5d4d81a642078c86ca9d49f2f574" +
                "&client_secret=0f235a01e6134be9a323ecfe3d02706c";
        
        // Create an instance of HttpClient
        HttpClient httpClient = HttpClient.newHttpClient();
        
        // Create an HTTP request with the appropriate headers and method
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        
        // Send the request asynchronously
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    
            ObjectMapper objectMapper = new ObjectMapper();
            TokenResponseDto tokenResponse = objectMapper.readValue(response.body(), TokenResponseDto.class);
            System.out.println(tokenResponse.getAccessToken());
            token = tokenResponse.getAccessToken();
        }
        catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
