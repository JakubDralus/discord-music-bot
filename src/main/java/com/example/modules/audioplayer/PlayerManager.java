package com.example.modules.audioplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

// singleton?
public class PlayerManager {
    private final Map<Long, GuildMusicManager> musicManagers;
    private final AudioPlayerManager audioPlayerManager;
    
    public PlayerManager() {
        this.musicManagers = new HashMap<>();
        this.audioPlayerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(this.audioPlayerManager);
        AudioSourceManagers.registerLocalSource(this.audioPlayerManager);
    }
    
    public GuildMusicManager getMusicManager(Guild guild) {
        return this.musicManagers.computeIfAbsent(guild.getIdLong(), (guildId) -> {
            final GuildMusicManager guildMusicManager = new GuildMusicManager(audioPlayerManager);
            
            guild.getAudioManager().setSendingHandler(guildMusicManager.getSendHandler());
            
            return guildMusicManager;
        });
    }
    
    public void play(Guild guild, String trackURL, SlashCommandInteractionEvent event) {
        GuildMusicManager musicManager = getMusicManager(guild);
        EmbedBuilder embedBuilder = new EmbedBuilder();
        audioPlayerManager.loadItemOrdered(musicManager, trackURL, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                embedBuilder.clear();
                event.replyEmbeds(embedBuilder.setDescription("Song added to queue: " + track.getInfo().title
                        + "\n in queue: " + (musicManager.getScheduler().getQueue().size() + 1)).setColor(Color.GREEN).build()).queue();
                musicManager.getScheduler().queue(track);
            }
        
            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
//                List<AudioTrack> tracks = playlist.getTracks();
//
//                for (AudioTrack track : tracks) {
//                    musicManager.getScheduler().queue(track);
//                }
//                event.replyEmbeds(new EmbedBuilder().setDescription(tracks.size() + " song added to queue.")
//                        .setColor(Color.GREEN).build()).queue();
            }
        
            @Override
            public void noMatches() {
                System.out.println("No match found");
            }
        
            @Override
            public void loadFailed(FriendlyException exception) {
                exception.printStackTrace();
            }
        });
    }
}
