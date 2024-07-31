package com.bot.shared;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.awt.*;
import java.util.Objects;

public class CommandUtil {
    
    public static void connectToUserChannel(SlashCommandInteractionEvent event, AudioChannel userChannel) {
        Objects.requireNonNull(event.getGuild()).getAudioManager().openAudioConnection(userChannel);
    }
    
    public static AudioChannel getUserVoiceChannel(SlashCommandInteractionEvent event) {
        return Objects.requireNonNull(Objects.requireNonNull(event.getMember()).getVoiceState()).getChannel();
    }
    
    public static AudioChannel getBotVoiceChannel(SlashCommandInteractionEvent event) {
        return Objects.requireNonNull(Objects.requireNonNull(event.getGuild()).getSelfMember().getVoiceState()).getChannel();
    }
    
    public static void replyEmbedErr(SlashCommandInteractionEvent event, String message) {
        event.replyEmbeds(new EmbedBuilder()
                .setDescription(message)
                .setColor(Color.RED)
                .build()).queue();
    }
    
    public static void replyEmbed(SlashCommandInteractionEvent event, String message) {
        event.replyEmbeds(new EmbedBuilder()
                .setDescription(message)
                .build()).queue();
    }
}
