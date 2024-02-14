package com.bot.shared;

import com.bot.modules.audioplayer.PlayerManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;

import java.awt.*;


public class NowPlayingUtil {
    
    public static EmbedBuilder nowPlayingMessage;
    
    private static String getDynamicDuration(long durationInSeconds) {
        long minutes = durationInSeconds / 60;
        long seconds = durationInSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
    
    @NotNull
    private static EmbedBuilder makeTrackEmbed(AudioTrack track) {
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
        
        EmbedBuilder nowPlayingEmbed = makeTrackEmbed(track);
        event.deferReply().queue(); // wait a bit for the embed to initialize
        
        nowPlayingMessage = nowPlayingEmbed;
        event.getHook().sendMessageEmbeds(nowPlayingEmbed.build())
                .queue(originalMessage -> embedThread(player, originalMessage, track));
    }
    
    public static void displayCurrentPlayingTrackEmbedNoReply(SlashCommandInteractionEvent event, AudioPlayer player) {
        PlayerManager playerManager = PlayerManager.get();
        AudioTrack track = playerManager.getMusicManager(event.getGuild()).getAudioPlayer().getPlayingTrack();
        
        EmbedBuilder embedBuilder = makeTrackEmbed(track);
        
        event.getChannel().sendMessageEmbeds(embedBuilder.build())
                .queue(originalMessage -> embedThread(player, originalMessage, track));
    }
    
    // deletes track embed when the track is not playing anymore
    private static void embedThread(AudioPlayer player, Message originalMessage, AudioTrack track) {
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
    
    
    // this is probably too music requests for discord api so dont'use it
    @Deprecated
    private static void embedUpdateThread(AudioPlayer player, Message originalMessage, AudioTrack track, EmbedBuilder embedBuilder) {
        final long trackDurationInSeconds = track.getDuration()/1000;
        final String trackDuration = getDynamicDuration(trackDurationInSeconds);
        
        
        new Thread(() -> {
            try {
                while (player.getPlayingTrack() != null && player.getPlayingTrack().equals(track)) {
                    long currentPosition = track.getPosition() / 1000;

                    // Update the duration field of the embed with the current track time
                    // note: the currentTimestamp is not paused when the track is paused (i don't need to fix it)
                    String currentTimestamp = getDynamicDuration(trackDurationInSeconds - currentPosition);
                    embedBuilder
                            .clearFields()
                            .addField("Duration",
                                    currentTimestamp + " / " + trackDuration,
                                    false);
                    originalMessage.editMessageEmbeds(embedBuilder.build()).queue();
                    
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
    
    
    //todo: checking in commands here
}
