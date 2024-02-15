package com.bot.modules.discord.commands.music;

import com.bot.modules.audioplayer.PlayerManager;
import com.bot.modules.discord.commands.ISlashCommand;
import com.bot.shared.CommandUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.Objects;

public class Forward implements ISlashCommand {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(Forward.class);
    
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
        var track = playerManager.getMusicManager(event.getGuild()).getAudioPlayer().getPlayingTrack();
        var option = event.getOption("seconds");
        if (option == null) {
            event.replyEmbeds(new EmbedBuilder().setDescription("Seconds can't be null.")
                    .setColor(Color.RED).build()).queue();
            return;
        }
        
        var seconds = option.getAsLong();
        track.setPosition(track.getPosition() + (seconds * 1000));
        event.replyEmbeds(new EmbedBuilder()
                .setDescription("Song forwarded by " + seconds + " seconds.")
                .build()).queue();
        
        LOGGER.info("used /forward command in {}", event.getChannel().getName());
    }
}
