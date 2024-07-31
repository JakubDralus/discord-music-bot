package com.bot.modules.discord.commands.music;

import com.bot.modules.audioplayer.GuildMusicManager;
import com.bot.modules.audioplayer.PlayerManager;
import com.bot.modules.discord.commands.ISlashCommand;
import com.bot.shared.CommandUtil;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;


public class Pause implements ISlashCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(Pause.class);
    
    @Override
    public void execute(SlashCommandInteractionEvent event) {
        PlayerManager playerManager = PlayerManager.get();
        
        var track = playerManager.getMusicManager(event.getGuild()).getAudioPlayer().getPlayingTrack();
        if (track == null) {
            CommandUtil.replyEmbedErr(event, "No track is being played right now");
            return;
        }
        
        AudioChannel userChannel = CommandUtil.getUserVoiceChannel(event);
        AudioChannel botChannel = CommandUtil.getBotVoiceChannel(event);
        
        if (userChannel == null) {
            CommandUtil.replyEmbedErr(event, "Please join a voice channel.");
            return;
        }
        
        if (!Objects.equals(botChannel, userChannel)) {
            CommandUtil.replyEmbedErr(event, "Please be in the same voice channel as the bot.");
            return;
        }
       
        GuildMusicManager musicManager = playerManager.getMusicManager(event.getGuild());
        musicManager.getScheduler().getPlayer().setPaused(true);
        CommandUtil.replyEmbed(event, "Track paused.");
    
        LOGGER.info("used /pause command in {}", event.getChannel().getName());
    }
}
