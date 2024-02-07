package com.bot.modules.discord.commands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;


public class GlobalCommands {
    public void addGlobalCommands(JDA jda) {
        CommandListUpdateAction globalCommands = jda.updateCommands();
        
        globalCommands.addCommands(
            // other commands
            Commands.slash("info", "Info page for this music bot."),
            Commands.slash("twitter", "Info page for Twitter site."),
            Commands.slash("help", "list of commands"),

            // music
            Commands.slash("play", "plays music track")
                .addOptions(new OptionData(OptionType.STRING, "track", "track to be played", true)),
            Commands.slash("queue", "displays tracks in queue"),
            Commands.slash("clear-queue", "clears the queue"),
            Commands.slash("resume", "resumes current track"),
            Commands.slash("skip", "skips current track or a number of tracks")
                .addOptions(new OptionData(OptionType.STRING, "count", "amount of tracks to skip", false)),
            Commands.slash("leave", "bot leaves the channel"),
            Commands.slash("shuffle-queue", "shuffle the tracks in queue"),
            Commands.slash("play-ratpartymix", "play the Rat Paty Mix 2023™ playlist"),
            //Commands.slash("play-daily-song", "play today's daily song"), // unavailable until tracker is repaired
            Commands.slash("yeahbuddy", "GO TO THE GYM!!!")
        ).queue();
    }
}
