package com.bot.modules.discord.commands.music;

import com.bot.modules.audioplayer.PlayerManager;
import com.bot.modules.discord.commands.ISlashCommand;
import com.bot.shared.CommandUtil;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;


public class Play implements ISlashCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(Play.class);
    
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
        
        // set commandEvent for scheduler to make him display a current track being played
        playerManager.getMusicManager(event.getGuild()).getScheduler().setCommandEvent(event);
        
        String trackName = event.getOption("track").getAsString();
        try {
            new URI(trackName);
        }
        catch (URISyntaxException e) {
            trackName = "scsearch: " + trackName;
            System.out.println("trackname " + trackName);
        }
        
        playerManager.play(event.getGuild(), trackName, true, event);
        
        LOGGER.info("used /play command in {}", event.getChannel().getName());
    }
}
