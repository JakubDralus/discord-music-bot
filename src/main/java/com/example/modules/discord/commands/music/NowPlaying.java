package com.example.modules.discord.commands.music;

import com.example.modules.discord.commands.ISlashCommand;
import com.example.shared.Util;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class NowPlaying implements ISlashCommand {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(NowPlaying.class);
    
    @Override
    public void execute(SlashCommandInteractionEvent event) {
        Util.displayCurrentPlayingTrackEmbed(event, true);
    
        LOGGER.info("used /now-playing command in {}", event.getChannel().getName());
    }
}
