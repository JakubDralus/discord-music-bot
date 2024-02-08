package com.bot.shared;

import com.bot.modules.audioplayer.PlayerManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;


public class Util {
    
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
        long trackDurationInSeconds = track.getDuration()/1000;
        String trackThumbnailUrl = "https://img.youtube.com/vi/" + track.getIdentifier() + "/hqdefault.jpg";
        
        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setTitle("Now playing: ")
                .appendDescription(trackTitle)
                .addField("Duration", getDynamicDuration(trackDurationInSeconds), false)
                .setThumbnail(trackThumbnailUrl)
                .setUrl(track.getInfo().uri);
        
        event.replyEmbeds(embedBuilder.build()).queue(originalMessage -> {
            new Thread(() -> {
                try {
                    while (player.getPlayingTrack() != null && player.getPlayingTrack().equals(track)) {
                        // Calculate the current position of the track
                        long currentPosition = track.getPosition() / 1000;
                        
                        // Update the duration field of the embed with the current track time
                        String updatedDuration = getDynamicDuration(trackDurationInSeconds - currentPosition);
                        embedBuilder.clearFields().addField("Duration", updatedDuration, false);
                        
                        // Edit the original message with the updated embed
                        originalMessage.editOriginalEmbeds(embedBuilder.build()).queue();
                        
                        
                        Thread.sleep(1000);
                        
                        // Check if the track is still playing every second
                        System.out.println(track);
                        System.out.println(player.getPlayingTrack());
                        
                        System.out.println(getDynamicDuration((track.getPosition())/1000));
                    }
                    
                    // If the track is not playing anymore or has been skipped, delete the original response
                    originalMessage.deleteOriginal().queue();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        });
    }
    
    public static void displayCurrentPlayingTrackEmbedAck(SlashCommandInteractionEvent event, AudioPlayer player) {
        PlayerManager playerManager = PlayerManager.get();
        AudioTrack track = playerManager.getMusicManager(event.getGuild()).getAudioPlayer().getPlayingTrack();
        playerManager.getMusicManager(event.getGuild()).getScheduler().setEvent(event);
        
        String trackTitle = track.getInfo().title;
        long trackDurationInSeconds = track.getDuration()/1000;
        String trackThumbnailUrl = "https://img.youtube.com/vi/" + track.getIdentifier() + "/hqdefault.jpg";
        
        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setTitle("Now playing: ")
                .appendDescription(trackTitle)
                .addField("Duration", getDynamicDuration(trackDurationInSeconds), false)
                .setThumbnail(trackThumbnailUrl)
                .setUrl(track.getInfo().uri);
        
        event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue(originalMessage -> {
            new Thread(() -> {
                try {
                    while (player.getPlayingTrack() != null && player.getPlayingTrack().equals(track)) {
                        long currentPosition = track.getPosition() / 1000;
                        
                        String updatedDuration = getDynamicDuration(trackDurationInSeconds - currentPosition);
                        embedBuilder.clearFields().addField("Duration", updatedDuration, false);
                        
                        originalMessage.editMessageEmbeds(embedBuilder.build()).queue();
//                        try {
//                            originalMessage.editMessageEmbeds(embedBuilder.build()).queue();
//
//                        } catch (Exception e) {
//                            event.deferReply().queue();
//                        }
                        //todo catch errors when playing from rat-party-mix command
                        
                        Thread.sleep(1000);
                        // Check if the track is still playing every second
//                        System.out.println(track);
//                        System.out.println(player.getPlayingTrack());
                        
                        System.out.println(getDynamicDuration((track.getPosition())/1000));
                    }
                    
                    // If the track is not playing anymore or has been skipped, delete the original response
                    originalMessage.delete().queue();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        });
    }
    
    //todo: checking in commands here
}
