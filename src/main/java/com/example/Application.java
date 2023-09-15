package com.example;

import com.example.modules.discord.Listeners;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.util.EnumSet;
import java.util.stream.IntStream;


// todo make this shitty class cleaner
public class Application {
    public static void main(String[] args) throws InterruptedException {
        EnumSet<GatewayIntent> intents = EnumSet.of(
                // Enables MessageReceivedEvent for guild (also known as servers)
                GatewayIntent.GUILD_MESSAGES,
                // Enables the event for private channels (also known as direct messages)
                GatewayIntent.DIRECT_MESSAGES,
                // Enables access to message.getContentRaw()
                GatewayIntent.MESSAGE_CONTENT,
                // Enables MessageReactionAddEvent for guild
                GatewayIntent.GUILD_MESSAGE_REACTIONS,
                // Enables MessageReactionAddEvent for private channels
                GatewayIntent.DIRECT_MESSAGE_REACTIONS
        );
        
        String discordToken = args[0];
        
        // start the bot
        JDA jda = JDABuilder.createDefault(discordToken, intents)
                .addEventListeners(new Listeners())
                .setActivity(Activity.watching("waiting for a prompt"))
                .build();
    
        jda.getRestPing().queue(ping ->
                // shows ping in milliseconds
                System.out.println("Logged in with ping: " + ping)
        );
    
        // If you want to access the cache, you can use awaitReady() to block the main thread until the jda instance is fully loaded
        jda.awaitReady();
        
        // Now we can access the fully loaded cache and print out list of all servers where bot is running
        System.out.println("Guilds: " + jda.getGuildCache().size());
        IntStream.range(0, jda.getGuilds().size())
                .forEach(i -> System.out.println((i+1) + " " + jda.getGuilds().get(i).getName()));
        
        
        // Spotify
        //Playlist.getPlaylistsItems_Async();
        
    }
}
