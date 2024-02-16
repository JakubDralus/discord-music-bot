package com.bot.shared;

import com.bot.modules.audioplayer.PlayerManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import org.jetbrains.annotations.NotNull;

import java.awt.*;


public class NowPlayingUtil {
    public static EmbedBuilder nowPlayingEmbedMsg;
    public static AudioTrack nowPlayingTrack;
    
    private static String getDynamicDuration(long durationInSeconds) {
        long minutes = durationInSeconds / 60;
        long seconds = durationInSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
    
    @NotNull
    public static EmbedBuilder makeTrackEmbed(AudioTrack track) {
        String trackThumbnailUrl = "https://img.youtube.com/vi/" + track.getIdentifier() + "/hqdefault.jpg";
        String trackTitle = track.getInfo().title;
        String trackLink = track.getInfo().uri;
        long trackDurationInSeconds = track.getDuration()/1000;
        String trackDuration = getDynamicDuration(trackDurationInSeconds);
        
        return new EmbedBuilder()
                .setTitle("Now playing :musical_note:")
                .setDescription("[" + trackTitle + "](" + trackLink + ")")
                .addField("Duration",
                         trackDuration,
                        false)
                .setThumbnail(trackThumbnailUrl)
                .setColor(new Color(50, 205, 50));
    }
    
    public static void displayCurrentPlayingTrackEmbedReply(SlashCommandInteractionEvent event, AudioPlayer player) {
        PlayerManager playerManager = PlayerManager.get();
        AudioTrack track = playerManager.getMusicManager(event.getGuild()).getAudioPlayer().getPlayingTrack();
        event.deferReply().submit(); // wait a bit for the external apis to get resources
        
        EmbedBuilder nowPlayingEmbed = makeTrackEmbed(track);
        nowPlayingEmbedMsg = nowPlayingEmbed;
        nowPlayingTrack = track;
        
        event.getHook().sendMessageEmbeds(nowPlayingEmbed.build())
                .queue(originalMessage -> embedThread(player, originalMessage, track));
    }
    
    // this one is for menu selection event from /play-youtube-banger dropdown
    public static void displayCurrentPlayingTrackEmbedReply(StringSelectInteractionEvent event, AudioPlayer player) {
        PlayerManager playerManager = PlayerManager.get();
        AudioTrack track = playerManager.getMusicManager(event.getGuild()).getAudioPlayer().getPlayingTrack();
        event.deferReply().submit(); // wait a bit for the external apis to get resources
        
        EmbedBuilder nowPlayingEmbed = makeTrackEmbed(track);
        nowPlayingEmbedMsg = nowPlayingEmbed;
        nowPlayingTrack = track;
        
        event.getHook().sendMessageEmbeds(nowPlayingEmbed.build())
                .queue(originalMessage -> embedThread(player, originalMessage, track));
    }
    
    public static void displayCurrentPlayingTrackEmbedNoReply(SlashCommandInteractionEvent event, AudioPlayer player) {
        PlayerManager playerManager = PlayerManager.get();
        AudioTrack track = playerManager.getMusicManager(event.getGuild()).getAudioPlayer().getPlayingTrack();
        
        EmbedBuilder nowPlayingEmbed = makeTrackEmbed(track);
        nowPlayingEmbedMsg = nowPlayingEmbed;
        nowPlayingTrack = track;
        
        event.getChannel().sendMessageEmbeds(nowPlayingEmbed.build())
                .queue(originalMessage -> embedThread(player, originalMessage, track));
    }
    
    // deletes track embed when the track is not playing anymore
    public static void embedThread(AudioPlayer player, Message originalMessage, AudioTrack track) {
        new Thread(() -> {
            try {
                while (player.getPlayingTrack() != null && player.getPlayingTrack().equals(track)) {
                    Thread.sleep(1000);
                }
                
                // If the track is not playing anymore or has been skipped, delete the original response
                originalMessage.delete().queue();
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
    
    public static void embedThreadInteractionHook(AudioPlayer player, InteractionHook originalMessage, AudioTrack track) {
        new Thread(() -> {
            try {
                while (player.getPlayingTrack() != null && player.getPlayingTrack().equals(track)) {
                    Thread.sleep(1000);
                }
                
                // If the track is not playing anymore or has been skipped, delete the original response
                originalMessage.deleteOriginal().queue();
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
