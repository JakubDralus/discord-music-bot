package com.bot.modules.spotify;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.credentials.ClientCredentials;
import se.michaelthelin.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;

import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;


public class SpotifyToken {
    private static final Logger LOGGER = LoggerFactory.getLogger(SpotifyToken.class);
    
    public static void clientCredentials_Async() {
        SpotifyApi spotifyApi = SpotifyApiInstance.get();
        ClientCredentialsRequest clientCredentialsRequest = spotifyApi.clientCredentials().build();
        
        try {
            final CompletableFuture<ClientCredentials> clientCredentialsFuture = clientCredentialsRequest.executeAsync();
            // Example Only. Never block in production code.
            final ClientCredentials clientCredentials = clientCredentialsFuture.join();
            spotifyApi.setAccessToken(clientCredentials.getAccessToken());
            
            LOGGER.info("Spotify token get.");
        }
        catch (CompletionException e) {
            System.out.println("Error: " + e.getCause().getMessage());
        }
        catch (CancellationException e) {
            System.out.println("Async operation cancelled.");
        }
    }
}
