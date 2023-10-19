package com.bot.modules.discord.commands.other;

import com.bot.modules.discord.commands.ISlashCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Info implements ISlashCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(Info.class);
    
    @Override
    public void execute(SlashCommandInteractionEvent event) {
        event.reply("This is a bot for playing music from a " +
                "Rat Party Mix 2023 playlist that you can find on Spotify.\n" +
                "https://open.spotify.com/playlist/0RHhiQ6hGLKgjE7eqNdXzh").queue();
        
        LOGGER.info("used /info command in {}", event.getChannel().getName());
    }
}
