package com.example.modules.audioplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


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
    
    public void queue(AudioTrack track) {
        if (!player.startTrack(track, true)) {
            queue.offer(track);
        }
    }
    
    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (isRepeat) {
            player.startTrack(track.makeClone(), false);
        }
        else {
            player.startTrack(queue.poll(), false);
        }
    }
    
    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {
        event.replyEmbeds(new EmbedBuilder()
                .setTitle("Now playing: ")
                .setDescription(track.getInfo().title + "\n")
                .appendDescription(timeFormat(track.getDuration()/1000))
                .setThumbnail("https://img.youtube.com/vi/" + track.getIdentifier() + "/hqdefault.jpg") // icon
                .build()).queue();
    }
    
    private String timeFormat(long seconds) {
        long minutes = seconds / 60;
        long remainingSeconds = seconds % 60;
        return minutes + ":" + remainingSeconds;
    }
}
