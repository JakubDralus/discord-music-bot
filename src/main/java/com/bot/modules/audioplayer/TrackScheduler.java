package com.bot.modules.audioplayer;

import com.bot.shared.NowPlayingUtil;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;


@Getter
@Setter
public class TrackScheduler extends AudioEventAdapter {
    
    private final AudioPlayer player;
    private final BlockingQueue<AudioTrack> queue;
    private boolean isRepeat = false;
    protected SlashCommandInteractionEvent event;
    
    public TrackScheduler(AudioPlayer player) {
        this.player = player;
        this.queue = new LinkedBlockingQueue<>();
    }
    
    public void queueTrack(AudioTrack track, boolean reply) {
        if (!player.startTrack(track, true)) {
            queue.offer(track);
            
            if (reply && !event.isAcknowledged()) {
                event.replyEmbeds(new EmbedBuilder()
                        .setTitle("Added to queue: ")
                        .setDescription(track.getInfo().title + "\n")
                        .build()
                ).queue();
            }
        }
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
    
    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {
        System.out.println("on track start");
        if (!event.isAcknowledged()) {
            NowPlayingUtil.displayCurrentPlayingTrackEmbedReply(event, player);
        }
    }
    
    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (isRepeat) {
            player.startTrack(track.makeClone(), false);
        }
        else {
            player.startTrack(queue.poll(), false);
            
            //todo check why some end reasons cleanups appear
//            System.out.println("end reason:" + endReason.toString());
            
            // show another track after prev finished
            if (endReason == AudioTrackEndReason.FINISHED && !queue.isEmpty()){
                NowPlayingUtil.displayCurrentPlayingTrackEmbedNoReply(event, player);
            }
        }
    }
}
