package com.bot.modules.audioplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import dev.lavalink.youtube.YoutubeAudioSourceManager;
import dev.lavalink.youtube.clients.*;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.HashMap;
import java.util.Map;


public class PlayerManager {
    private static PlayerManager INSTANCE;
    private final Map<Long, GuildMusicManager> musicManagers;
    private final AudioPlayerManager audioPlayerManager;
    
    private PlayerManager() {
        this.musicManagers = new HashMap<>();
        this.audioPlayerManager = new DefaultAudioPlayerManager();
        // Web is dev.lavalink.youtube.clients.Web
        Web.setPoTokenAndVisitorData(Dotenv.load().get("PO_TOKEN"), Dotenv.load().get("VISITOR_DATA"));
        
        YoutubeAudioSourceManager ytSourceManager = new dev.lavalink.youtube.YoutubeAudioSourceManager(
                true, new MusicWithThumbnail(), new WebWithThumbnail()
        );
        audioPlayerManager.registerSourceManager(ytSourceManager);
        AudioSourceManagers.registerRemoteSources(audioPlayerManager,
                com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager.class);
        AudioSourceManagers.registerLocalSource(audioPlayerManager);
    }
    
    public static PlayerManager get() {
        if (INSTANCE == null) {
            INSTANCE = new PlayerManager();
        }
        return INSTANCE;
    }
    
    public GuildMusicManager getMusicManager(Guild guild) {
        return this.musicManagers.computeIfAbsent(guild.getIdLong(), (guildId) -> {
            final GuildMusicManager guildMusicManager = new GuildMusicManager(audioPlayerManager);
            
            guild.getAudioManager().setSendingHandler(guildMusicManager.getSendHandler());
            return guildMusicManager;
        });
    }
    
    public void play(Guild guild, String trackURL, boolean reply, SlashCommandInteractionEvent event) {
        GuildMusicManager musicManager = getMusicManager(guild);
        
        audioPlayerManager.loadItemOrdered(musicManager.getAudioPlayer(), trackURL, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                musicManager.getScheduler().queueTrack(track, true);
            }
        
            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                musicManager.getScheduler().queueTrack(playlist.getTracks().get(0), reply);
            }
        
            @Override
            public void noMatches() {
                System.out.println("No match found");
                if (event.isAcknowledged()) {
                    event.getChannel().sendMessage("no match found, try again").queue();
                }
                else {
                    event.reply("no match found, try again").queue();
                }
            }
        
            @Override
            public void loadFailed(FriendlyException exception) {
                System.out.println(exception.severity);
            }
        });
    }
}
