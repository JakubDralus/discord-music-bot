package com.bot.modules.discord.commands.music;

import com.bot.modules.audioplayer.PlayerManager;
import com.bot.modules.discord.commands.ISlashCommand;
import com.bot.shared.CommandUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;


public class Repeat implements ISlashCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(Repeat.class);
    
    @Override
    public void execute(SlashCommandInteractionEvent event) {
        LOGGER.info("used /repeat command in {}", event.getChannel().getName());
        
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
        var scheduler = playerManager.getMusicManager(event.getGuild()).getScheduler();
        scheduler.setRepeat(!scheduler.isRepeat());
        
        event.replyEmbeds(new EmbedBuilder()
                .setDescription("set repeat to: " + scheduler.isRepeat())
                .build()).queue();
    }
}