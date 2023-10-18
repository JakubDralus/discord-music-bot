package com.example.modules.discord.commands.music;

import com.example.modules.audioplayer.PlayerManager;
import com.example.modules.discord.commands.ISlashCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class Shuffle implements ISlashCommand {
    
    @Override
    public void execute(SlashCommandInteractionEvent event) {
        System.out.println("siema");
        PlayerManager playerManager = PlayerManager.get();
        playerManager.getMusicManager(event.getGuild()).getScheduler().shuffleQueue();
        event.reply("shuffled the queue").queue();
    }
}
