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
                  - Option: `track` (String): The track to be played.
                - `/queue`: Display the tracks in the queue.
                - `/pause`: Stop playing the current track.
                - `/resume`: Resume the current track.
                - `/skip` `[count]`: Skip the current track or a specified number of tracks.
                  - Option: `count` (String): The number of tracks to skip (optional).
                - `/leave`: Make the bot leave the channel.
                - `/now-playing`: Display information about the current playing track.
                - `/play-ratpartymix`: Play the Rat Party Mix 2023™ playlist.
                - `/play-daily-song`: Play today's daily song.
                - `/yeahbuddy`: Get pumped up for the gym!
                
                **Other Commands:**
                - `/help`: Display command list.
                - `/info`: Get information about this music bot.
                - `/twitter`: Get information about the Twitter site.
                """).queue();
    
        LOGGER.info("used /help command in {}", event.getChannel().getName());
    }
}
