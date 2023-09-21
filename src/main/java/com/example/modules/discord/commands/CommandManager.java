package com.example.modules.discord.commands;

import com.example.modules.discord.commands.admin.Dupa;
import com.example.modules.discord.commands.admin.Echo;
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
//        for (var guild: event.getJDA().getGuilds()) {
//            for (var command: commands) {
//                guild.upsertCommand(command.)
//            }
//        }
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
        
        //Music Commands
        //commandsMap.put("play", new PlayCommand(restService, playerManagerService, trackService));
    }
    
}
