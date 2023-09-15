package com.example.modules.discord;

import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;


public class Listeners extends ListenerAdapter {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(Listeners.class);
    
    @Override
    public void onReady(@NotNull ReadyEvent event) {
        LOGGER.info("I am ready to go!");
    }
    
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.isFromGuild()) {
            onGuildMessageReceived(event);
        }
        else {
            onPrivateMessageReceived(event);
        }
    }
    
    private void onPrivateMessageReceived(MessageReceivedEvent event) {
        String authorName = event.getAuthor().getName();
        String contentDisplay = event.getMessage().getContentDisplay();
        String nickname = event.getAuthor().getEffectiveName();
        String channelName = "<private channel>";
    
        LOGGER.info(String.format("%s [%s - %s]: %s",
                channelName,
                authorName,
                nickname,
                contentDisplay));
    }
    
    private void onGuildMessageReceived(MessageReceivedEvent event) {
        String guild = event.getGuild().getName();
        String authorName = event.getAuthor().getName();
        String nickname = Objects.requireNonNull(event.getMember()).getNickname();
        String channelName = event.getChannel().getName();
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
