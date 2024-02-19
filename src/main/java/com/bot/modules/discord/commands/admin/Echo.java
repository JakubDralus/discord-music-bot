package com.bot.modules.discord.commands.admin;

import com.bot.modules.discord.commands.ISlashCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Echo implements ISlashCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(Echo.class);
    
    @Override
    public void execute(SlashCommandInteractionEvent event) {
        String message = event.getOption("message").getAsString();
        event.reply(message).queue();
        
        LOGGER.info("used /echo command in {} (message: {})", event.getChannel().getName(), message);
    }
}
