package com.bot.modules.discord.commands;

import com.bot.Application;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.stream.IntStream;


public class Listener extends ListenerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(Listener.class);
    
    @Override
    public void onReady(@NotNull ReadyEvent event) {
        JDA jda = event.getJDA();
    
        // Now we can access the fully loaded cache and print out list of all servers where bot is running
        System.out.println("Guilds: " + jda.getGuildCache().size());
        IntStream.range(0, jda.getGuilds().size())
                .forEach(i -> System.out.println((i+1) + " " + jda.getGuilds().get(i).getName()));
        
        System.out.print("\tchannels: ");
        for(var channel: jda.getTextChannels()) {
            System.out.print(channel.getName() + ", ");
        }
        System.out.println();
        
        for(var channel: jda.getTextChannels()) {
            if (channel.getName().equals("bot-test") && channel.getGuild().getId().equals(Application.getRatPartyMixServerId()))
                channel.sendMessage("Let's play some fucking bangers :sunglasses:").queue();
        }
        
        LOGGER.info("I am ready to go!");
    }
    
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        
        if (event.isFromGuild()) {
            onGuildMessageReceived(event);
        }
        else {
            onPrivateMessageReceived(event);
        }
    }
    
    private void onPrivateMessageReceived(MessageReceivedEvent event) {
        String channelName = "<private channel>";
        String authorName = event.getAuthor().getName();
        String nickname = event.getAuthor().getEffectiveName();
        String contentDisplay = event.getMessage().getContentDisplay();
    
        LOGGER.info(String.format("%s [%s - %s]: %s",
                channelName,
                authorName,
                nickname,
                contentDisplay));
        
        event.getChannel().sendMessage(contentDisplay).queue();
    }
    
    private void onGuildMessageReceived(MessageReceivedEvent event) {
        String guild = event.getGuild().getName();
        String channelName = event.getChannel().getName();
        String authorName = event.getAuthor().getName();
        String nickname = Objects.requireNonNull(event.getMember()).getNickname();
        String contentDisplay = event.getMessage().getContentDisplay();
        
        if (nickname == null) nickname = "<no nickname>";
    
        LOGGER.info(String.format("server: %s, channel: #%s by [%s - %s]: %s",
                guild,
                channelName,
                authorName,
                nickname,
                contentDisplay));
    }
    
    @Override
    public void onMessageDelete(@NotNull MessageDeleteEvent event) {
        String channelName = event.getChannel().getName();
        LOGGER.info(String.format("Message deleted on channel: #%s", channelName));
    }
}
