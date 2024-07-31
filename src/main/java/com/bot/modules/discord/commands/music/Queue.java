package com.bot.modules.discord.commands.music;

import com.bot.modules.audioplayer.PlayerManager;
import com.bot.modules.discord.commands.ISlashCommand;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;


public class Queue implements ISlashCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(Queue.class);
    
    /**
     * Maximum number of tracks that will be displayed in the reply embed
     * before printing "and x more...".
     */
    private final int MAX_TRACKS_DISPLAY = 12;
    
    @Override
    public void execute(SlashCommandInteractionEvent event) {
        PlayerManager playerManager = PlayerManager.get();
    
        // set commandEvent for scheduler to make him display a current track being played
        playerManager.getMusicManager(event.getGuild()).getScheduler().setCommandEvent(event);
    
        BlockingQueue<AudioTrack> queue = playerManager.getMusicManager(event.getGuild()).getScheduler().getQueue();
        StringBuilder tracks = new StringBuilder();
        AtomicInteger i = new AtomicInteger();
        
        // show top 15 tracks
        queue.forEach(track -> {
            if (i.get() >= MAX_TRACKS_DISPLAY) {
                return;
            }
            i.getAndIncrement();
            tracks.append(i).append(". ").append(track.getInfo().title).append("\n");
        });
        
        event.replyEmbeds(new EmbedBuilder()
                .setTitle("Tracks in queue:")
                .appendDescription(tracks.toString())
                .appendDescription(queue.size() > MAX_TRACKS_DISPLAY
                        ? ("\n and " + (queue.size() - MAX_TRACKS_DISPLAY) + " more...")
                        : ""
                )
                .build()
        ).queue();
    
        LOGGER.info("used /queue command in {}", event.getChannel().getName());
    }
}
