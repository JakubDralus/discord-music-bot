package com.bot.shared;

import com.bot.modules.audioplayer.PlayerManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
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
    
    public static void displayCurrentPlayingTrackEmbed(SlashCommandInteractionEvent event, boolean lateCall) {
        PlayerManager playerManager = PlayerManager.get();
        AudioTrack playingTrack = playerManager.getMusicManager(event.getGuild()).getAudioPlayer().getPlayingTrack();
        playerManager.getMusicManager(event.getGuild()).getScheduler().setEvent(event);
    
        String trackTitle = playingTrack.getInfo().title;
        AtomicLong trackDurationInSeconds = new AtomicLong(playingTrack.getDuration()/1000);
        String trackThumbnailUrl = "https://img.youtube.com/vi/" + playingTrack.getIdentifier() + "/hqdefault.jpg";
    
        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setTitle("Now playing: ")
                .appendDescription(trackTitle)
                .addField("Duration", getDynamicDuration(trackDurationInSeconds.get()), false)
                .setThumbnail(trackThumbnailUrl); // icon
                //.setColor(Color.GREEN);
        
        event.replyEmbeds(embedBuilder.build()).queue(message -> {
            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
            scheduler.scheduleAtFixedRate(() -> {
                if (!isSchedulerRunning || lateCall) {
                    //message.editOriginalEmbeds(embedBuilder.setColor(Color.RED).build()).queue();
                    return; // Don't update if scheduler is paused
                }
                
                String updatedDuration = getDynamicDuration(trackDurationInSeconds.getAndDecrement());
                embedBuilder.clearFields().addField("Duration", updatedDuration, false);
                message.editOriginalEmbeds(embedBuilder.build()).queue();
                
                if (trackDurationInSeconds.get() <= 1) {
                    message.editOriginalEmbeds(embedBuilder.clear().build()).queue();
                }
            }, 0, 1, TimeUnit.SECONDS);
        });
    }
    
    //todo: checking in commands here
}
