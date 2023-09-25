package com.example.modules.discord.commands.admin;

import com.example.modules.discord.commands.ISlashCommand;
import com.example.modules.spotify.Playlist;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Dupa implements ISlashCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(Dupa.class);
    
//    @Override
//    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
//        if (event.getName().equals("dupa")) {
//            event.reply("dupa").queue();
//        }
//        LOGGER.info("used /dupa command in {}", event.getChannel().getName());
//    }
    
    @Override
    public void execute(SlashCommandInteractionEvent event) {
        int playlistLength = Playlist.getTracks().size();
        int randomId = (int)(Math.random() * playlistLength) + 1;
        event.reply("dupa random song: "+ playlistLength + Playlist.getTracks().get(randomId)).queue();
        LOGGER.info("used /dupa command in {}", event.getChannel().getName());
    }
}
