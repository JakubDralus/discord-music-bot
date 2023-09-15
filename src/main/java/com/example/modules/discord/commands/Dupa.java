package com.example.modules.discord.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Dupa extends ListenerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(Dupa.class);
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (event.getName().equals("dupa")) {
            event.reply("dupa").queue();
        }
        LOGGER.info("used /dupa command in {}", event.getChannel().getName());
    }
}
