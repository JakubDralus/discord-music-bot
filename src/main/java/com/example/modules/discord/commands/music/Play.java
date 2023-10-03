package com.example.modules.discord.commands.music;

import com.example.modules.audioplayer.PlayerManager;
import com.example.modules.discord.commands.ISlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;


public class Play implements ISlashCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(Play.class);
    
    @Override
    public void execute(SlashCommandInteractionEvent event) {
        Member member = event.getMember();
        GuildVoiceState memberVoiceState = member.getVoiceState();
    
        if (!memberVoiceState.inAudioChannel()) {
            event.replyEmbeds(new EmbedBuilder().setDescription("Please join a voice channel.")
                    .setColor(Color.RED).build()).queue();
            return;
        }
    
        Member selfMember = event.getGuild().getSelfMember();
        GuildVoiceState selfMemberVoiceState = selfMember.getVoiceState();
    
        if (!selfMemberVoiceState.inAudioChannel()) {
            event.getGuild().getAudioManager().openAudioConnection(memberVoiceState.getChannel());
        }
        
        if (selfMemberVoiceState.getChannel() != memberVoiceState.getChannel()) {
            event.replyEmbeds(new EmbedBuilder().setDescription("Please be in the same voice channel as the bot.")
                    .setColor(Color.RED).build()).queue();
            return;
        }
    
        PlayerManager playerManager = new PlayerManager();
        playerManager.play(event.getGuild(), event.getOption("track").getAsString(), event);
    
        LOGGER.info("used /play command in {}", event.getChannel().getName());
    }
}
