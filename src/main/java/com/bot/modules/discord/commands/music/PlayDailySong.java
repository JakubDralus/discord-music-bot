package com.bot.modules.discord.commands.music;

import com.bot.modules.audioplayer.PlayerManager;
import com.bot.modules.discord.commands.ISlashCommand;
import com.bot.modules.spotify.Playlist;
import com.bot.shared.CommandUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;


public class PlayDailySong implements ISlashCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(PlayDailySong.class);
    
    @Override
    public void execute(SlashCommandInteractionEvent event) {
        AudioChannel userChannel = CommandUtil.getUserVoiceChannel(event);
        AudioChannel botChannel = CommandUtil.getBotVoiceChannel(event);
        
        if (userChannel == null) {
            CommandUtil.replyEmbedErr(event, "Please join a voice channel.");
            return;
        }
        
        if (botChannel == null) {
            CommandUtil.connectToUserChannel(event, userChannel);
            botChannel = userChannel;
        }
        
        if (!Objects.equals(botChannel, userChannel)) {
            CommandUtil.replyEmbedErr(event, "Please be in the same voice channel as the bot.");
            return;
        }
    
        PlayerManager playerManager = PlayerManager.get();
        playerManager.getMusicManager(event.getGuild()).getScheduler().setCommandEvent(event);
    
        String trackName = Playlist.getDailySongName();
        if (trackName == null) {
            event.replyEmbeds(new EmbedBuilder().setDescription("error while getting the daily song form API").build()).queue();
            return;
        }
        
        try {
            new URI(trackName);
        }
        catch (URISyntaxException e) {
            trackName = "ytsearch: " + trackName + " Official Audio";
        }
        catch (NullPointerException e) {
            CommandUtil.replyEmbedErr(event, "Something went wrong.");
            return;
        }
    
        playerManager.play(event.getGuild(), trackName, true, event);
    
        LOGGER.info("used /play-daily-song command in {}", event.getChannel().getName());
    }
}
