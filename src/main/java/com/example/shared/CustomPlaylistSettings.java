package com.example.shared;

import com.example.modules.audioplayer.PlayerManager;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;


// some youtube songs have to be adjusted (skipped certain amount of seconds to not wait for the song)
public class CustomPlaylistSettings {
    
    //looks for the song in playlist by id (order in Spotify UI)
    public static boolean adjustSong(int id, SlashCommandInteractionEvent event) {
        PlayerManager playerManager = PlayerManager.get();
    
        switch (id) {
            case 2 -> {
                playerManager.play(event.getGuild(), "https://www.youtube.com/watch?v=v4tFZzBrI20", false, event);
                return true;
            }
            case 3 -> {
                playerManager.play(event.getGuild(), "https://www.youtube.com/watch?v=oujZpIr5id8", false, event);
                return true;
            }
        }
        return false;
    }
}
