package com.bot.modules.discord.commands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;


public class TestCommands {
    public void addTestCommands(JDA jda, String TEST_SERVER) {
        while (jda.getGuildById(TEST_SERVER) == null) {
            try {
                //noinspection BusyWait
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        Guild testServer = jda.getGuildById(TEST_SERVER);
        CommandListUpdateAction testServerCommands = null;
        if (testServer != null) {
            testServerCommands = testServer.updateCommands();
        }
        
        if (testServerCommands != null) {
            testServerCommands.addCommands(
                // admin commands
                Commands.slash("random-song", "random song from playlist (test command)"),
                Commands.slash("echo", "echo test (test command)")
                        .addOptions(new OptionData(OptionType.STRING, "message", "message to be echoed (test command)", true)),
                
                // other commands
                Commands.slash("info", "Info page for this music bot. (test command)"),
                Commands.slash("twitter", "Info page for Twitter site. (test command)"),
                Commands.slash("help", "list of commands (test command)"),
                
                // music
                Commands.slash("play", "plays music track (test command)")
                        .addOptions(new OptionData(OptionType.STRING, "track", "track to be played (test command)", true)),
                Commands.slash("now-playing", "display the current playing track info (test command)"),
                Commands.slash("queue", "displays tracks in queue (test command)"),
                Commands.slash("shuffle-queue", "shuffle the queue content (test command)"),
                Commands.slash("pause", "stops playing current track (test command)"),
                Commands.slash("resume", "resumes current track (test command)"),
                Commands.slash("skip", "skips current track or a number of tracks (test command)")
                        .addOptions(new OptionData(OptionType.INTEGER, "count", "amount of tracks to skip (test command)", false)),
                Commands.slash("forward", "forward the track (seconds) (test command)")
                        .addOptions(new OptionData(OptionType.STRING, "seconds", "amount of seconds to skip (test command)", true)),
                Commands.slash("leave", "bot leaves the channel (test command)"),
                Commands.slash("play-ratpartymix", "play the Rat Paty Mix 2023™ playlist (test command)"),
                Commands.slash("play-daily-song", "play today's daily song (test command)"),
                Commands.slash("play-youtube-banger", "play a song from #youtube-only-bangers (test command)"),
                Commands.slash("yeahbuddy", "GO TO THE GYM!!! (test command)")
            ).queue();
        }
    }
}
