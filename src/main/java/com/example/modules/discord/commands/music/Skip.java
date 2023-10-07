package com.example.modules.discord.commands.music;

import com.example.modules.audioplayer.PlayerManager;
import com.example.modules.discord.commands.ISlashCommand;
import com.example.shared.Util;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.awt.*;
import java.util.Objects;


public class Skip implements ISlashCommand {
    
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
        playerManager.getMusicManager(event.getGuild()).getScheduler().getPlayer().stopTrack();
        
        AudioTrack playingTrack = playerManager.getMusicManager(event.getGuild()).getAudioPlayer().getPlayingTrack();
        if (playingTrack != null) {
            event.replyEmbeds(new EmbedBuilder()
                    .setTitle("Now playing: ")
                    .setDescription(playingTrack.getInfo().title + "\n")
                    .appendDescription(Util.durationFormat(playingTrack.getDuration() / 1000))
                    .setThumbnail("https://img.youtube.com/vi/" + playingTrack.getIdentifier() + "/hqdefault.jpg") // icon
                    .build()
            ).queue();
        }
        else {
            event.reply("skipped to empty queue").queue();
        }
    
        //LOGGER.info("used /skip command in {}", event.getChannel().getName());
    }
}
