package com.bot.modules.discord.commands.other;

import com.bot.modules.discord.commands.ISlashCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Help implements ISlashCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(Help.class);
    
    @Override
    public void execute(SlashCommandInteractionEvent event) {
        event.reply("""
                **Music Commands:**
                - `/play` `[track]`: Play a specific music track (paste url or provide a youtube search prompt).
                  - arg: `track` (String): The track to be played.
                - `/now-playing`: display the current playing track info (this embed does not disappear after track stops playing).
                - `/queue`: Display the tracks in the queue.
                - `/clear-queue`: Removes all tracks from the queue.
                - `/shuffle`: Shuffles all tracks in the queue.
                - `/pause`: Stop playing the current track.
                - `/resume`: Resume the current track.
                - `/forward` `[seconds]`: forwards the song by amount of seconds provided.
                  - arg: `seconds` (String): seconds to be skipped/forwarded in current playing track.
                - `/skip` `[count]`: Skip the current track or a specified number of tracks.
                  - arg (optional): `count` (String): The number of tracks to skip (optional).
                - `/leave`: Make the bot leave the channel.
                - `/play-ratpartymix`: Play the Rat Party Mix 2023™ playlist.
                - `/play-daily-song`: Play today's daily song.
                - `/play-youtube-banger`: play a song from #youtube-only-bangers channel using a selection menu.
                - `/yeahbuddy`: Get pumped up for the gym!
                
                **Other Commands:**
                - `/help`: Display command list.
                - `/info`: Get information about this music bot and link to the playlist.
                - `/twitter`: Get information about the Twitter site.
                """).queue();
    
        LOGGER.info("used /help command in {}", event.getChannel().getName());
    }
}
