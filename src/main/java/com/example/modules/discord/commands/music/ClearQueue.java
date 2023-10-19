package com.example.modules.discord.commands.music;

import com.example.modules.audioplayer.PlayerManager;
import com.example.modules.discord.commands.ISlashCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ClearQueue implements ISlashCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClearQueue.class);
    
    @Override
    public void execute(SlashCommandInteractionEvent event) {
        PlayerManager playerManager = PlayerManager.get();
        if (!playerManager.getMusicManager(event.getGuild()).getScheduler().getQueue().isEmpty()){
            playerManager.getMusicManager(event.getGuild()).getScheduler().clearQueue();
            event.reply("queue cleared").queue();
        }
        else {
            event.reply("the queue is empty").queue();
        }
    
        LOGGER.info("used /clear-queue command in {}", event.getChannel().getName());
    }
}
