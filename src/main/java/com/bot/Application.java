package com.bot;

import com.bot.modules.discord.commands.CommandManager;
import com.bot.modules.discord.commands.GlobalCommands;
import com.bot.modules.discord.commands.Listener;
import com.bot.modules.discord.commands.TestCommands;
import com.bot.modules.spotify.DailySongToken;
import com.bot.modules.spotify.SpotifyApiInstance;
import com.bot.modules.spotify.SpotifyToken;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.util.EnumSet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class Application {
    
    @Getter
    private static final String ratPartyMixServerId = "598494742896181267";
    
    public static void main(String[] args) throws InterruptedException {
        final EnumSet<GatewayIntent> intents = EnumSet.of(
            GatewayIntent.GUILD_MESSAGES, // Enables MessageReceivedEvent for guild (also known as servers)
            GatewayIntent.DIRECT_MESSAGES,
            GatewayIntent.MESSAGE_CONTENT,  // Enables access to message.getContentRaw()
            GatewayIntent.GUILD_MESSAGE_REACTIONS, // Enables MessageReactionAddEvent for guild
            GatewayIntent.GUILD_VOICE_STATES,
            GatewayIntent.GUILD_EMOJIS_AND_STICKERS
        );
        final String discordToken = args[0];
        final String spotifyToken = args[1];
        final String xApiKey      = args[2]; // daily song API key
        
        SpotifyApiInstance.initSpotifyApi(spotifyToken);
        DailySongToken.setXApiKey(xApiKey);
        
        // start the bot
        JDA jda = JDABuilder.createDefault(discordToken, intents)
                .addEventListeners(new CommandManager())
                .addEventListeners(new Listener())
                .setActivity(Activity.listening("/help"))
                .build();
        
        TestCommands.addTestCommands(jda, ratPartyMixServerId);
        GlobalCommands.addGlobalCommands(jda);
    
        // shows ping in milliseconds
        jda.getRestPing().queue(ping ->
            log.info("Logged in with ping: " + ping)
        );
        
        // If you want to access the cache, you can use awaitReady() to block the main thread until the jda instance is fully loaded
        jda.awaitReady();
    
        // scheduler that refreshes the Spotify token every hour
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(SpotifyToken::clientCredentials_Async, 0, 1, TimeUnit.HOURS);
    }
}
