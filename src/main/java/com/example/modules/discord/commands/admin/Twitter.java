package com.example.modules.discord.commands.admin;

import com.example.modules.discord.commands.ISlashCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Twitter implements ISlashCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(Twitter.class);
    
    @Override
    public void execute(SlashCommandInteractionEvent event) {
        event.reply("Check out our tracker on Twitter to keep you updated on playlist changes. " +
                "It also tells you a daily song! every day at 11:00 AM CET \n" +
                "https://twitter.com/RatPartyMix").queue();
        
        LOGGER.info("used /twitter command in {}", event.getChannel().getName());
    }
}
