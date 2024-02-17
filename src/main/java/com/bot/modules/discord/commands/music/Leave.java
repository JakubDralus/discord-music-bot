package com.bot.modules.discord.commands.music;

import com.bot.modules.audioplayer.GuildMusicManager;
import com.bot.modules.audioplayer.PlayerManager;
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
            GuildMusicManager musicManager = PlayerManager.get().getMusicManager(event.getGuild());
            AudioManager audioManager = event.getGuild().getAudioManager();
            musicManager.getScheduler().getPlayer().stopTrack();
            musicManager.getScheduler().getPlayer().setPaused(false);
            musicManager.getScheduler().getPlayer().destroy();
            musicManager.getScheduler().clearQueue();
            audioManager.closeAudioConnection();
            event.replyEmbeds(new EmbedBuilder().setDescription("leaving channel").build()).queue();
    
            LOGGER.info("used /leave command in {}", event.getChannel().getName());
        }
    }
}
