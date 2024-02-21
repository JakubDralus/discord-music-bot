package com.bot.modules.discord.commands.admin;

import com.bot.modules.audioplayer.PlayerManager;
import com.bot.modules.discord.commands.ISlashCommand;
import com.bot.modules.spotify.Playlist;
import com.bot.shared.CommandUtil;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;


public class PlayRandomSong implements ISlashCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(PlayRandomSong.class);
    
    @Override
    public void execute(SlashCommandInteractionEvent event) {
        Playlist.getPlaylistsItems_Async();
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
        
        // set commandEvent for scheduler to make him display a current track being played
        playerManager.getMusicManager(event.getGuild()).getScheduler().setCommandEvent(event);
        
        int playlistLength = Playlist.getTracks().size();
        int randomId = (int)(Math.random() * playlistLength) + 1;
        String randomTrack = "ytsearch: " + Playlist.getTracks().get(randomId);
        System.out.println("track name " + randomTrack);
        
        playerManager.play(event.getGuild(), randomTrack, true, event);
        
        LOGGER.info("used /random-song command in {}", event.getChannel().getName());
    }
}
