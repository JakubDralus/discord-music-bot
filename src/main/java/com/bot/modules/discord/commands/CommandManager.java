package com.bot.modules.discord.commands;

import com.bot.modules.audioplayer.PlayerManager;
import com.bot.modules.discord.commands.admin.PlayRandomSong;
import com.bot.modules.discord.commands.admin.RandomSong;
import com.bot.modules.discord.commands.admin.Echo;
import com.bot.modules.discord.commands.other.Help;
import com.bot.modules.discord.commands.other.Info;
import com.bot.modules.discord.commands.other.Twitter;
import com.bot.modules.discord.commands.music.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class CommandManager extends ListenerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommandManager.class);
    private static final Map<String, ISlashCommand> commandsMap;
    static {
        commandsMap = new ConcurrentHashMap<>();
        
        //Admin Commands
        commandsMap.put("random-song", new RandomSong());
        commandsMap.put("play-random-song", new PlayRandomSong());
        commandsMap.put("echo", new Echo());
        commandsMap.put("yeahbuddy", new YeahBuddy());
        
        // other
        commandsMap.put("info", new Info());
        commandsMap.put("twitter", new Twitter());
        commandsMap.put("help", new Help());
        
        //Music Commands
        commandsMap.put("play", new Play());
        commandsMap.put("now-playing", new NowPlaying());
        commandsMap.put("queue", new Queue());
        commandsMap.put("clear-queue", new ClearQueue());
        commandsMap.put("skip", new Skip());
        commandsMap.put("pause", new Pause());
        commandsMap.put("resume", new Resume());
        commandsMap.put("repeat", new Repeat());
        commandsMap.put("leave", new Leave());
        commandsMap.put("shuffle-queue", new Shuffle());
        commandsMap.put("play-ratpartymix", new RatPartyMix());
        commandsMap.put("play-daily-song", new PlayDailySong());
//        commandsMap.put("play-youtube-banger", new PlayYoutubeBanger());
        commandsMap.put("forward", new Forward());
    }
    
    public CommandManager() {}
    
    @Override
    public void onReady(@NotNull ReadyEvent event) {
        LOGGER.info("command manager ready");
    }
    
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String commandName = event.getName();
        
        ISlashCommand command;
        if ((command = commandsMap.get(commandName)) != null) {
            command.execute(event);
        }
    }
    
    @Override
    public void onStringSelectInteraction(StringSelectInteractionEvent event) {
        if (event.getComponentId().equals("youtube-only-bangers-select")) {
            PlayerManager playerManager = PlayerManager.get();
            playerManager.getMusicManager(event.getGuild()).getScheduler().setMenuEvent(event);
            
            String trackName = event.getValues().get(0);
            try {
                new URI(trackName);
            }
            catch (URISyntaxException e) {
                trackName = "ytsearch: " + trackName;
            }
            
            playerManager.play(event.getGuild(), trackName, true,
                    playerManager.getMusicManager(event.getGuild()).getScheduler().getCommandEvent());
        }
    }
}
