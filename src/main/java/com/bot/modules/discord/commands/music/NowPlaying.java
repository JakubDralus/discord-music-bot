package com.bot.modules.discord.commands.music;

import com.bot.modules.audioplayer.PlayerManager;
import com.bot.modules.discord.commands.ISlashCommand;
import com.bot.shared.NowPlayingUtil;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class NowPlaying implements ISlashCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(Leave.class);
    
    @Override
    public void execute(SlashCommandInteractionEvent event) {
        PlayerManager playerManager = PlayerManager.get();
        AudioPlayer player = playerManager.getMusicManager(event.getGuild()).getAudioPlayer();
        EmbedBuilder embed = NowPlayingUtil.nowPlayingEmbedMsg;
        AudioTrack track = NowPlayingUtil.nowPlayingTrack;
        
        if (playerManager.getMusicManager(event.getGuild()).getScheduler().getPlayer().getPlayingTrack() != null) {
//            event.deferReply().queue(); // reply first so originalMessage type is Message not InteractionHook
            event.replyEmbeds(embed.build())
                    .queue(originalMessage -> NowPlayingUtil.embedThreadInteractionHook(player, originalMessage, track));
        }
        else {
            event.replyEmbeds(new EmbedBuilder().setDescription("no track is being played right now").build()).queue();
        }
        
        LOGGER.info("used /now-playing command in {}", event.getChannel().getName());
    }
}
