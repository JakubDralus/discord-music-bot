package com.bot.shared;

import com.bot.modules.audioplayer.PlayerManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import org.jetbrains.annotations.NotNull;

import java.awt.*;


public class NowPlayingUtil {
    
    private static String getDynamicDuration(long durationInSeconds) {
        long minutes = durationInSeconds / 60;
        long seconds = durationInSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
    
    @NotNull
    public static EmbedBuilder makeTrackEmbed(AudioTrack track, Member member) {
        String trackTitle = track.getInfo().title;
        String author = track.getInfo().author;
        String trackLink = track.getInfo().uri;
        String thumbnail = track.getInfo().artworkUrl;
        long trackDurationInSeconds = track.getDuration()/1000;
        String trackDuration = getDynamicDuration(trackDurationInSeconds);
        String nickname = member.getNickname() != null ? member.getNickname() : member.getEffectiveName();
        
        return new EmbedBuilder()
                .setTitle("Now playing :musical_note:")
                .setDescription("[" + trackTitle + "](" + trackLink + ")\n")
                .appendDescription(author)
                .addField("Duration", trackDuration, true)
                .addField("Added by", nickname, true)
                .addBlankField(true)
                .setThumbnail(thumbnail)
                .setColor(new Color(30, 215, 96)); //spotify green
    }
    
    public static void displayCurrentPlayingTrackEmbedReply(SlashCommandInteractionEvent event, AudioPlayer player) {
        PlayerManager playerManager = PlayerManager.get();
        AudioTrack track = playerManager.getMusicManager(event.getGuild()).getAudioPlayer().getPlayingTrack();
        event.deferReply().submit(); // wait a bit for the external apis to get resources
        
        EmbedBuilder nowPlayingEmbed = makeTrackEmbed(track, event.getMember());
        
        event.getHook().sendMessageEmbeds(nowPlayingEmbed.build())
                .queue(originalMessage -> embedThread(player, originalMessage, track));
    }
    
    // this one is for menu selection event from /play-youtube-banger dropdown
    public static void displayCurrentPlayingTrackEmbedReply(StringSelectInteractionEvent event, AudioPlayer player) {
        PlayerManager playerManager = PlayerManager.get();
        AudioTrack track = playerManager.getMusicManager(event.getGuild()).getAudioPlayer().getPlayingTrack();
        event.deferReply().submit(); // wait a bit for the external apis to get resources
        
        EmbedBuilder nowPlayingEmbed = makeTrackEmbed(track, event.getMember());
        
        event.getHook().sendMessageEmbeds(nowPlayingEmbed.build())
                .queue(originalMessage -> embedThread(player, originalMessage, track));
    }
    
    public static void displayCurrentPlayingTrackEmbedNoReply(SlashCommandInteractionEvent event, AudioPlayer player) {
        PlayerManager playerManager = PlayerManager.get();
        AudioTrack track = playerManager.getMusicManager(event.getGuild()).getAudioPlayer().getPlayingTrack();
        
        EmbedBuilder nowPlayingEmbed = makeTrackEmbed(track, event.getMember());
        
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
