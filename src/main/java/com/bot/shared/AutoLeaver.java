package com.bot.shared;

import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class AutoLeaver {
    private static final long INACTIVITY_TIMEOUT = 10; // 10 minutes
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private static ScheduledFuture<?> currentTask;
    
    public static void startInactivityTimer(VoiceChannel channel) {
        currentTask = scheduler.schedule(() -> {
            channel.getGuild().getAudioManager().closeAudioConnection();
            System.out.println("Disconnected due to inactivity");
        }, INACTIVITY_TIMEOUT, TimeUnit.MINUTES);
    }
    
    public static void resetInactivityTimer(VoiceChannel channel) {
        if (currentTask != null && !currentTask.isDone()) {
            System.out.println("cancelled");
            currentTask.cancel(true);
        }
        startInactivityTimer(channel);
    }
    
    public static void stopInactivityTimer() {
        if (currentTask != null) {
            currentTask.cancel(true);
        }
    }
}
