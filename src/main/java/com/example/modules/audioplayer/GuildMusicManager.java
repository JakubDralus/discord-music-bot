package com.example.modules.audioplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import lombok.Getter;


@Getter
public class GuildMusicManager {
    private final AudioPlayerSendHandler sendHandler;
    private final TrackScheduler scheduler;
    private final AudioPlayer audioPlayer;
    
    public GuildMusicManager(AudioPlayerManager manager) {
        audioPlayer = manager.createPlayer();
        scheduler = new TrackScheduler(audioPlayer);
        audioPlayer.addListener(this.scheduler);
        sendHandler = new AudioPlayerSendHandler(this.audioPlayer);
    }
}
