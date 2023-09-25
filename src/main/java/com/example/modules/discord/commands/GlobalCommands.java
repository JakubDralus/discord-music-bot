package com.example.modules.discord.commands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;


public class GlobalCommands {
    public void addGlobalCommands(JDA jda) {
        CommandListUpdateAction globalCommands = jda.updateCommands();
        
        globalCommands.addCommands(
                //Commands.slash("info", "Info page for this music bot.")
//                Commands.slash("mhelp", "Info page for the music commands."),
//
//                //Music Commands
//                Commands.slash("play", "Play a song on your voice channel.")
//                        .addOptions(new OptionData(OptionType.STRING, "query", "Song url or name.")
//                                .setRequired(true)),
//                Commands.slash("skip", "Skip the current song."),
//                Commands.slash("forward", "Forward the current song x seconds.")
//                        .addOptions(new OptionData(OptionType.INTEGER, "sec", "seconds")
//                                .setRequired(true)),
//                Commands.slash("pause", "Pause the current song."),
//                Commands.slash("resume", "Resume the paused song."),
//                Commands.slash("leave", "Make bot leave the voice channel."),
//                Commands.slash("queue", "List the song queue.")
//                        .addOptions(new OptionData(OptionType.INTEGER, "page", "Displayed page of the queue.")
//                                .setRequired(true)),
//                Commands.slash("shuffle", "Shuffle the queue."),
//                Commands.slash("loop", "Loop the current song."),
//                Commands.slash("nowplaying", "Show the currently playing song.")
        ).queue();
    }
}
