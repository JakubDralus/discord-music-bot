package com.example.shared;


public class Util {
    
    public static String durationFormat(long seconds) {
        long minutes = seconds / 60;
        long remainingSeconds = seconds % 60;
        return minutes + ":" + remainingSeconds;
    }
    
    //todo: checking in commands here
}
