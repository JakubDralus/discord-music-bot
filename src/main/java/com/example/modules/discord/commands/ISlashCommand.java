package com.example.modules.discord.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;


public interface ISlashCommand {
    void execute(SlashCommandInteractionEvent event);
}
