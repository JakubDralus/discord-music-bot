package com.bot.modules.discord.commands.other;

import com.bot.modules.discord.commands.ISlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Info implements ISlashCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(Info.class);
    
    @Override
    public void execute(SlashCommandInteractionEvent event) {
        event.replyEmbeds(new EmbedBuilder()
                .setDescription("""
                This is a bot for playing music from a Rat Party Mix 2024 playlist that you can find
                on Spotify. Or any other music track using `/play` command.
                Use `/help` for commands description.
                
                **related links**:
                - [Twitter/X](https://twitter.com/RatPartyMix) bot tracker for daily songs and changes
                - [Spotify playlist](https://open.spotify.com/playlist/0RHhiQ6hGLKgjE7eqNdXzh)
                - [GitHub repository](https://github.com/JakubDralus/Rat-Party-Mix-discord-music-bot) of this bot
                - (website coming soon)
                """)
                .build()).queue();
        
        LOGGER.info("used /info command in {}", event.getChannel().getName());
    }
}
