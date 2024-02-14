package com.bot.modules.discord.commands.music;

import com.bot.modules.audioplayer.PlayerManager;
import com.bot.modules.discord.commands.ISlashCommand;
import com.bot.shared.NowPlayingUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NowPlaying implements ISlashCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(Leave.class);
    
    @Override
    public void execute(SlashCommandInteractionEvent event) {
        
        PlayerManager playerManager = PlayerManager.get();
        
        if (playerManager.getMusicManager(event.getGuild()).getScheduler().getPlayer().getPlayingTrack() != null) {
            event.replyEmbeds(NowPlayingUtil.nowPlayingMessage.build()).queue();
        }
        else {
            event.replyEmbeds(new EmbedBuilder().setDescription("no track is being played right now").build()).queue();
        }
        
        LOGGER.info("used /now-playing command in {}", event.getChannel().getName());
    }
}
