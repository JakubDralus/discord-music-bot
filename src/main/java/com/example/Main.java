package com.example;

import com.example.spotify.SpotifyToken;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;


public class Main {
    public static void main(String[] args) {
        String token = args[0];
        JDA jda = JDABuilder.createDefault(token).build();
        
        System.out.println("success!");
        SpotifyToken.aquireToken();
    }
}
