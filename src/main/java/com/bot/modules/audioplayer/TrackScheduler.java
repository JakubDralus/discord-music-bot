package com.bot.modules.audioplayer;

import com.bot.shared.AutoLeaver;
import com.bot.shared.NowPlayingUtil;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


@Getter
@Setter
public class TrackScheduler extends AudioEventAdapter {
    
    private final AudioPlayer player;
    private final BlockingQueue<AudioTrack> queue;
    private boolean isRepeat = false;
    private SlashCommandInteractionEvent commandEvent;
    private StringSelectInteractionEvent menuEvent;
    
    public TrackScheduler(AudioPlayer player) {
        this.player = player;
        this.queue = new LinkedBlockingQueue<>();
    }
    
    public void clearQueue() {
        queue.clear();
    }
    
    public void shuffleQueue() {
        List<AudioTrack> trackList = new ArrayList<>(queue);
        Collections.shuffle(trackList);
        queue.clear();
        queue.addAll(trackList);
    }
    
    public void queueTrack(AudioTrack track, boolean reply) {
//        System.out.println("queue track: "+ track.getInfo().title);
        
        player.setPaused(false); // reset the pause
        
        if (!player.startTrack(track, true)) {
            queue.offer(track);
            
            if (commandEvent != null && reply && !commandEvent.isAcknowledged()) {
                commandEvent.replyEmbeds(new EmbedBuilder()
                        .setTitle("Added to queue :white_check_mark:")
                        .setDescription(track.getInfo().title)
                        .appendDescription("\n tracks in queue: %d".formatted(queue.size()))
                        .build()).queue();
            }
            if (menuEvent != null && reply && !menuEvent.isAcknowledged()) {
                menuEvent.replyEmbeds(new EmbedBuilder()
                        .setTitle("Added to queue :white_check_mark:")
                        .setDescription(track.getInfo().title)
                        .appendDescription("\n tracks in queue: %d".formatted(queue.size()))
                        .build()).queue();
            }
        }
    }
    
    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {
//        System.out.println("on track start");
        AutoLeaver.stopInactivityTimer();
        
        if (!commandEvent.isAcknowledged()) {
            NowPlayingUtil.displayCurrentPlayingTrackEmbedReply(commandEvent, player);
        }
        if (menuEvent != null && !menuEvent.isAcknowledged()) {
            NowPlayingUtil.displayCurrentPlayingTrackEmbedReply(menuEvent,player);
        }
    }
    
    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
//        System.out.println(queue.size());
        System.out.println("end reason: " + endReason);
        if (isRepeat) {
            player.startTrack(track.makeClone(), false);
        }
        else {
            var nextTrack = queue.poll();
            player.startTrack(nextTrack, false);
            
            // show another track after prev finished
            if (endReason.mayStartNext && nextTrack != null) {
                NowPlayingUtil.displayCurrentPlayingTrackEmbedNoReply(commandEvent, player);
            }
        }

        if (player.getPlayingTrack() == null)
            AutoLeaver.startInactivityTimer(commandEvent.getGuild().getAudioManager().getConnectedChannel().asVoiceChannel());
    }
    
    @Override
    public void onTrackException(AudioPlayer player, AudioTrack track, FriendlyException exception) {
        exception.printStackTrace();
    }
    
    @Override
    public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs) {
        System.out.println("track stuck " + track.getInfo().title);
    }
}
