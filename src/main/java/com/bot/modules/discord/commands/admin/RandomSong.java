package com.bot.modules.discord.commands.admin;

import com.bot.modules.discord.commands.ISlashCommand;
import com.bot.modules.spotify.Playlist;
import com.bot.shared.NowPlayingUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class RandomSong implements ISlashCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(RandomSong.class);
    
    @Override
    public void execute(SlashCommandInteractionEvent event) {
        Playlist.getPlaylistsItems_Async();

        int playlistLength = Playlist.getTracks().size();
        int randomId = (int)(Math.random() * playlistLength) + 1;

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("random song: ");
        embedBuilder.appendDescription(randomId + " - " + Playlist.getTracks().get(randomId));
        event.replyEmbeds(embedBuilder.build()).queue();
        
//        event.replyEmbeds(NowPlayingUtil.nowPlayingMessage.build()).queue();
        
        LOGGER.info("used /random-song command in {}", event.getChannel().getName());
    }
}
