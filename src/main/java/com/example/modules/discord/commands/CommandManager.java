package com.example.modules.discord.commands;

import com.example.modules.discord.commands.admin.Dupa;
import com.example.modules.discord.commands.admin.Echo;
import com.example.modules.discord.commands.admin.Info;
import com.example.modules.discord.commands.admin.Twitter;
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
//        commandsMap.put("play", new PlayCommand(restService, playerManagerService, trackService));
//        commandsMap.put("skip", new SkipCommand(playerManagerService, musicCommandUtils));
//        commandsMap.put("forward", new ForwardCommand(playerManagerService, musicCommandUtils));
//        commandsMap.put("rewind", new RewindCommand(playerManagerService, musicCommandUtils));
//        commandsMap.put("pause", new PauseCommand(playerManagerService, musicCommandUtils));
//        commandsMap.put("resume", new ResumeCommand(playerManagerService, musicCommandUtils));
//        commandsMap.put("leave", new LeaveCommand(playerManagerService, musicCommandUtils));
//        commandsMap.put("queue", new QueueCommand(playerManagerService));
//        commandsMap.put("swap", new SwapCommand(playerManagerService, musicCommandUtils));
//        commandsMap.put("shuffle", new ShuffleCommand(playerManagerService, musicCommandUtils));
//        commandsMap.put("loop", new LoopCommand(playerManagerService, musicCommandUtils));
//        commandsMap.put("remove", new RemoveCommand(playerManagerService, musicCommandUtils));
//        commandsMap.put("nowplaying", new NowPlayingCommand(playerManagerService, musicCommandUtils));
//        commandsMap.put("mhelp", new MusicHelpCommand(musicCommandUtils));
    }
}
