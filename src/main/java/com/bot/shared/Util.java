package com.bot.shared;

import com.bot.modules.audioplayer.PlayerManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackState;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;


public class Util {
    
    public static String durationFormat(long seconds) {
        long minutes = seconds / 60;
        long remainingSeconds = seconds % 60;
        return minutes + ":" + ((remainingSeconds < 10) ? "0" + remainingSeconds : remainingSeconds);
    }
    
    private static String getDynamicDuration(long durationInSeconds) {
        long minutes = durationInSeconds / 60;
        long seconds = durationInSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
    
    public static boolean isSchedulerRunning = true;
    
    public static void displayCurrentPlayingTrackEmbed(SlashCommandInteractionEvent event, AudioPlayer player) {
        PlayerManager playerManager = PlayerManager.get();
        AudioTrack track = playerManager.getMusicManager(event.getGuild()).getAudioPlayer().getPlayingTrack();
        playerManager.getMusicManager(event.getGuild()).getScheduler().setEvent(event);
        
        String trackTitle = track.getInfo().title;
        AtomicLong trackDurationInSeconds = new AtomicLong((track.getDuration()/1000));
        String trackThumbnailUrl = "https://img.youtube.com/vi/" + track.getIdentifier() + "/hqdefault.jpg";
        
        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setTitle("Now playing: ")
                .appendDescription(trackTitle)
                .addField("Duration", getDynamicDuration(trackDurationInSeconds.get()), false)
                .setThumbnail(trackThumbnailUrl);
        

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        event.replyEmbeds(embedBuilder.build()).queue(message -> {
            scheduler.scheduleAtFixedRate(() -> {
                if (!isSchedulerRunning) {
                    return; // Don't update if scheduler is paused
                }

                String updatedDuration = getDynamicDuration(trackDurationInSeconds.getAndDecrement());
                embedBuilder.clearFields().addField("Duration", updatedDuration, false);
                message.editOriginalEmbeds(embedBuilder.build()).submit();
                if (player.getPlayingTrack().getState().equals(AudioTrackState.FINISHED)
                        || trackDurationInSeconds.get() == 0) {
                    message.deleteOriginal().submit();
                    scheduler.shutdown();
                }
            }, 0, 1, TimeUnit.SECONDS);
        });
    }
    
    public static void displayCurrentPlayingTrackEmbedAck(SlashCommandInteractionEvent event, AudioPlayer player) {
        PlayerManager playerManager = PlayerManager.get();
        AudioTrack track = playerManager.getMusicManager(event.getGuild()).getAudioPlayer().getPlayingTrack();
        playerManager.getMusicManager(event.getGuild()).getScheduler().setEvent(event);
        
        String trackTitle = track.getInfo().title;
        AtomicLong trackDurationInSeconds = new AtomicLong((track.getDuration()/1000));
        String trackThumbnailUrl = "https://img.youtube.com/vi/" + track.getIdentifier() + "/hqdefault.jpg";
        
        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setTitle("Now playing: ")
                .appendDescription(trackTitle)
                .addField("Duration", getDynamicDuration(trackDurationInSeconds.get()), false)
                .setThumbnail(trackThumbnailUrl);
        
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue(message -> {
            scheduler.scheduleAtFixedRate(() -> {
                if (!isSchedulerRunning) {
                    return; // Don't update if scheduler is paused
                }
                
                String updatedDuration = getDynamicDuration(trackDurationInSeconds.getAndDecrement());
                embedBuilder.clearFields().addField("Duration", updatedDuration, false);
                message.editMessageEmbeds(embedBuilder.build()).submit();
                
                if (player.getPlayingTrack().getState().equals(AudioTrackState.FINISHED)
                        || player.getPlayingTrack().getState().equals(AudioTrackState.STOPPING)) {
                    message.delete().submit();
                    scheduler.shutdown();
                }
            }, 0, 1, TimeUnit.SECONDS);
        });
    }
    
    //todo: checking in commands here
}
