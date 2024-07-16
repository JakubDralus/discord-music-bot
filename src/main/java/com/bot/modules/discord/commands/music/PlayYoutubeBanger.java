package com.bot.modules.discord.commands.music;

import com.bot.Application;
import com.bot.modules.audioplayer.PlayerManager;
import com.bot.modules.discord.commands.ISlashCommand;
import com.bot.shared.CommandUtil;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class PlayYoutubeBanger implements ISlashCommand {
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
        playerManager.getMusicManager(event.getGuild()).getScheduler().setCommandEvent(event);
        
        String ytOnlyBangersChannelId = "1192806142502518865";
        String ratPartyMixServerId = Application.getRatPartyMixServerId();
        Guild testServer = event.getJDA().getGuildById(ratPartyMixServerId);
        TextChannel ytOnlyBangersChannel = testServer.getTextChannelById(ytOnlyBangersChannelId);

        ytOnlyBangersChannel.getHistoryFromBeginning(25).queue(history -> {
            List<SelectOption> youtubeBangers = new ArrayList<>();
            for (var msg: history.getRetrievedHistory()) {
                String[] msgContent = extractTitleAndURL(msg.getContentDisplay());
                youtubeBangers.add(SelectOption.of(msgContent[0], msgContent[1]));
            }
            
            StringSelectMenu menu = StringSelectMenu.create("youtube-only-bangers-select")
                    .setPlaceholder("Choose a track from our list") // shows the placeholder indicating what this menu is for
                    .addOptions(youtubeBangers)
                    .setRequiredRange(1, 1) // must select exactly one
                    .build();

            event.reply("")
                    .addActionRow(menu)
                    .queue();
        });
        
        LOGGER.info("used /play-youtube-banger command in {}", event.getChannel().getName());
    }
    
    private static String[] extractTitleAndURL(String input) {
        String[] parts = input.split(":", 2);
        return new String[]{parts[0], parts[1]};
    }
}
