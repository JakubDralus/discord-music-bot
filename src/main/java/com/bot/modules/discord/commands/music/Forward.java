package com.bot.modules.discord.commands.music;

import com.bot.modules.audioplayer.PlayerManager;
import com.bot.modules.discord.commands.ISlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.awt.*;
import java.util.Objects;

public class Forward implements ISlashCommand {
    
    @Override
    public void execute(SlashCommandInteractionEvent event) {
        AudioChannel userChannel = Objects.requireNonNull(Objects
                .requireNonNull(event.getMember()).getVoiceState()).getChannel();
        AudioChannel botChannel = Objects.requireNonNull(Objects
                .requireNonNull(event.getGuild()).getSelfMember().getVoiceState()).getChannel();
        
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
    }
}
