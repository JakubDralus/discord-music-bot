package com.bot.modules.discord.commands.music;

import com.bot.modules.audioplayer.PlayerManager;
import com.bot.modules.audioplayer.TrackScheduler;
import com.bot.modules.discord.commands.ISlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.managers.AudioManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Leave implements ISlashCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(Leave.class);
    
    @Override
    public void execute(SlashCommandInteractionEvent event) {
        if (event.getGuild() != null) {
            event.replyEmbeds(new EmbedBuilder().setDescription("leaving channel").build()).queue();
            
            TrackScheduler scheduler = PlayerManager.get().getMusicManager(event.getGuild()).getScheduler();
            AudioManager audioManager = event.getGuild().getAudioManager();
            scheduler.getQueue().poll();
            scheduler.clearQueue();
            scheduler.setRepeat(false);
            scheduler.getPlayer().stopTrack();
            scheduler.getPlayer().setPaused(false);
            scheduler.getPlayer().destroy();
            audioManager.closeAudioConnection();
            PlayerManager.get().getMusicManager(event.getGuild()).getAudioPlayer().destroy();
            
            LOGGER.info("used /leave command in {}", event.getChannel().getName());
        }
    }
}
