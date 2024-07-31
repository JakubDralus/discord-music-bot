package com.bot.shared;

import com.bot.modules.audioplayer.PlayerManager;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;


// some youtube songs have to be adjusted i.e. the wrong track is played by the provider
public class CustomPlaylistSettings {
    
    //looks for the song in playlist by id (order in Spotify app)
    public static boolean adjustSong(int id, SlashCommandInteractionEvent event) {
        PlayerManager playerManager = PlayerManager.get();
    
        return switch (id) {
            case 2 -> {
                playerManager.play(event.getGuild(), "https://www.youtube.com/watch?v=v4tFZzBrI20", false, event);
                yield true;
            }
            case 3 -> {
                playerManager.play(event.getGuild(), "https://www.youtube.com/watch?v=oujZpIr5id8", false, event);
                yield true;
            }
            case 5 -> {
                playerManager.play(event.getGuild(), "https://www.youtube.com/watch?v=E0cykgYRhQg", false, event);
                yield true;
            }
            default -> false;
        };
    }
}
