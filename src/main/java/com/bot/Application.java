package com.bot;

import com.bot.modules.discord.commands.CommandManager;
import com.bot.modules.discord.commands.GlobalCommands;
import com.bot.modules.discord.commands.Listener;
import com.bot.modules.discord.commands.TestCommands;
import com.bot.modules.spotify.SpotifyApiInstance;
import com.bot.modules.spotify.SpotifyToken;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.EnumSet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class Application {
    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);
    
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
            GatewayIntent.GUILD_VOICE_STATES
        );
        
        final String discordToken = args[0];
        SpotifyApiInstance.initSpotifyApi(args[1]);
        String RatPartyMixServerId = "598494742896181267";
        
        // start the bot
        JDA jda = JDABuilder.createDefault(discordToken, intents)
                .addEventListeners(new CommandManager())
                .addEventListeners(new Listener())
                .setActivity(Activity.playing("/help"))
                .build();
        new TestCommands().addTestCommands(jda, RatPartyMixServerId);
        new GlobalCommands().addGlobalCommands(jda);
    
        // shows ping in milliseconds
        jda.getRestPing().queue(ping ->
            LOGGER.info("Logged in with ping: " + ping)
        );
    
        // If you want to access the cache, you can use awaitReady() to block the main thread until the jda instance is fully loaded
        jda.awaitReady();
    
        // scheduler that refreshes the Spotify token every hour
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(SpotifyToken::clientCredentials_Async, 0, 1, TimeUnit.HOURS);
    }
}
