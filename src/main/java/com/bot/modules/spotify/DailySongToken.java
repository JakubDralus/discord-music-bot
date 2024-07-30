package com.bot.modules.spotify;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Setter;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class DailySongToken {
    private static final String ratPartyMixApiUrl = "http://130.61.63.141:8888/api/v1/auth/token";
    
    @Setter
    private static String xApiKey;
    
    public static String getToken() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ratPartyMixApiUrl))
                .GET()
                .header("x-api-key", xApiKey)
                .build();
        
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
//            System.out.println("token response body: " + response.body());
            
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response.body());
            return rootNode.get("access_token").toString().replace("\"", ""); // remove quotes from json;
        }
        catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
