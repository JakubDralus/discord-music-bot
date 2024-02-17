package com.bot.modules.discord.commands.music;

import com.bot.modules.audioplayer.PlayerManager;
import com.bot.modules.discord.commands.ISlashCommand;
import com.bot.shared.CommandUtil;
import com.bot.shared.NowPlayingUtil;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;


public class Skip implements ISlashCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(Skip.class);
    
    @Override
    public void execute(SlashCommandInteractionEvent event) {
        AudioChannel userChannel = CommandUtil.getUserVoiceChannel(event);
        AudioChannel botChannel = CommandUtil.getBotVoiceChannel(event);
        
        if (userChannel == null) {
            CommandUtil.replyEmbedErr(event, "Please join a voice channel.");
            return;
        }
        
        if (botChannel == null) {
            CommandUtil.connectToUserChannel(event, userChannel);
            botChannel = userChannel;
        }
        
        if (!Objects.equals(botChannel, userChannel)) {
            CommandUtil.replyEmbedErr(event, "Please be in the same voice channel as the bot.");
            return;
        }
        
        PlayerManager playerManager = PlayerManager.get();
        
        int count = 1;
        OptionMapping message = event.getOption("count");
        if (message != null) {
            count = event.getOption("count").getAsInt();
        }
        for (int i = 0; i < count; i++) {
            playerManager.getMusicManager(event.getGuild()).getScheduler().getPlayer().stopTrack();
        }
        
        AudioTrack playingTrack = playerManager.getMusicManager(event.getGuild()).getAudioPlayer().getPlayingTrack();
        if (playingTrack != null) {
            NowPlayingUtil.displayCurrentPlayingTrackEmbedReply(event, playerManager.getMusicManager(event.getGuild()).getAudioPlayer());
        }
        else {
            event.reply("skipped to empty queue").queue();
        }
    
        LOGGER.info("used /skip command in {}", event.getChannel().getName());
    }
}
