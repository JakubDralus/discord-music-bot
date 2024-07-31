package com.bot.modules.discord.commands.music;

import com.bot.modules.audioplayer.PlayerManager;
import com.bot.modules.discord.commands.ISlashCommand;
import com.bot.shared.CommandUtil;
import com.bot.shared.NowPlayingUtil;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class NowPlaying implements ISlashCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(NowPlaying.class);
    
    @Override
    public void execute(SlashCommandInteractionEvent event) {
        PlayerManager playerManager = PlayerManager.get();
        AudioPlayer player = playerManager.getMusicManager(event.getGuild()).getAudioPlayer();

        if (playerManager.getMusicManager(event.getGuild()).getScheduler().getPlayer().getPlayingTrack() != null) {
            NowPlayingUtil.displayCurrentPlayingTrackEmbedReply(event, player);
        }
        else {
            CommandUtil.replyEmbedErr(event, "No track is being played right now.");
        }
        
        LOGGER.info("used /now-playing command in {}", event.getChannel().getName());
    }
}
