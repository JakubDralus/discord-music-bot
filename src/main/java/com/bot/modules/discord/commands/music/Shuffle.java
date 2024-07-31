package com.bot.modules.discord.commands.music;

import com.bot.modules.audioplayer.PlayerManager;
import com.bot.modules.discord.commands.ISlashCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Shuffle implements ISlashCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(Shuffle.class);
    
    @Override
    public void execute(SlashCommandInteractionEvent event) {
        PlayerManager playerManager = PlayerManager.get();
        
        if (!playerManager.getMusicManager(event.getGuild()).getScheduler().getQueue().isEmpty()){
            playerManager.getMusicManager(event.getGuild()).getScheduler().shuffleQueue();
        }
        event.reply("shuffled the queue").queue();
    
        LOGGER.info("used /shuffle command in {}", event.getChannel().getName());
    }
}
