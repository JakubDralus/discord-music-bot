package com.example.modules.discord.commands;

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
                    //admin commands
                    Commands.slash("dupa", "random song from playlist"),
                    Commands.slash("echo", "echo test")
                        .addOptions(new OptionData(OptionType.STRING, "message", "message to be echoed", true)),
                    Commands.slash("info", "Info page for this music bot."),
                    Commands.slash("twitter", "Info page for Twitter site."),
                    
                    Commands.slash("yeahbuddy", "GO TO THE GYM!!!"),
                    Commands.slash("play", "plays music track")
                        .addOptions(new OptionData(OptionType.STRING, "track", "track to be played", true)),
                    Commands.slash("queue", "displays tracks in queue"),
                    Commands.slash("stop", "stops current track"),
                    Commands.slash("resume", "resumes current track"),
                    Commands.slash("skip", "skips current track"),
                    Commands.slash("leave", "bot leaves the channel"),
                    Commands.slash("ratpartymix", "play the Rat Paty Mix 2023™ playlist")
            ).queue();
        }
    }
}
