package com.example.modules.discord.commands.music;

import com.example.modules.audioplayer.PlayerManager;
import com.example.modules.discord.commands.ISlashCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;


public class ClearQueue implements ISlashCommand {
    
    @Override
    public void execute(SlashCommandInteractionEvent event) {
        PlayerManager playerManager = PlayerManager.get();
        playerManager.getMusicManager(event.getGuild()).getScheduler().setEvent(event);
        playerManager.getMusicManager(event.getGuild()).getScheduler().clearQueue();
    }
}
