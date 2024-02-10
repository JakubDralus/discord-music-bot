package com.bot.modules.discord.commands.music;

import com.bot.modules.audioplayer.PlayerManager;
import com.bot.modules.discord.commands.ISlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;


public class Play implements ISlashCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(Play.class);
    
    @Override
    public void execute(SlashCommandInteractionEvent event) {
        AudioChannel userChannel = Objects.requireNonNull(Objects
                .requireNonNull(event.getMember()).getVoiceState()).getChannel();
        AudioChannel botChannel = Objects.requireNonNull(Objects.
                requireNonNull(event.getGuild()).getSelfMember().getVoiceState()).getChannel();
    
        if (!event.getMember().getVoiceState().inAudioChannel()) {
            event.replyEmbeds(new EmbedBuilder().setDescription("Please join a voice channel.")
                    .setColor(Color.RED).build()).queue();
            return;
        }
    
        if (!event.getGuild().getSelfMember().getVoiceState().inAudioChannel()) {
            event.getGuild().getAudioManager().openAudioConnection(userChannel);
            botChannel = userChannel;
        }
        
        if (!Objects.equals(botChannel, userChannel)) {
            event.replyEmbeds(new EmbedBuilder().setDescription("Please be in the same voice channel as the bot.")
                    .setColor(Color.RED).build()).queue();
        }
    
        PlayerManager playerManager = PlayerManager.get();
        
        // set event for scheduler to make him display a current track being played
        playerManager.getMusicManager(event.getGuild()).getScheduler().setEvent(event);
        
        String trackName = event.getOption("track").getAsString();
        try {
            new URI(trackName);
        }
        catch (URISyntaxException e) {
            trackName = "ytsearch: " + trackName;
        }
        
        playerManager.play(event.getGuild(), trackName, true, event);
        
        LOGGER.info("used /play command in {}", event.getChannel().getName());
    }
}
