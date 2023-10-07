package com.example.modules.discord.commands;

import com.example.modules.discord.commands.admin.Dupa;
import com.example.modules.discord.commands.admin.Echo;
import com.example.modules.discord.commands.admin.Info;
import com.example.modules.discord.commands.admin.Twitter;
import com.example.modules.discord.commands.music.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class CommandManager extends ListenerAdapter {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(CommandManager.class);
    private Map<String, ISlashCommand> commandsMap;
    
    public CommandManager() {
        //...
        commandMapper();
    }
    
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
    
    private void commandMapper() {
        commandsMap = new ConcurrentHashMap<>();
        
        //Admin Commands
        commandsMap.put("dupa", new Dupa());
        commandsMap.put("echo", new Echo());
        commandsMap.put("info", new Info());
        commandsMap.put("twitter", new Twitter());
        
        //Music Commands
        commandsMap.put("play", new Play());
        commandsMap.put("queue", new Queue());
        commandsMap.put("skip", new Skip());
        commandsMap.put("stop", new Stop());
        commandsMap.put("resume", new Resume());
        commandsMap.put("leave", new Leave());
        commandsMap.put("ratpartymix", new RatPartyMix());
        commandsMap.put("yeahbuddy", new YeahBuddy());
    }
}
