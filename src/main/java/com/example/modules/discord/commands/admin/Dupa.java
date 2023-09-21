package com.example.modules.discord.commands.admin;

import com.example.modules.discord.commands.ISlashCommand;
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
        event.reply("dupa reply").queue();
        LOGGER.info("used /dupa command in {}", event.getChannel().getName());
    }
}
