package com.example.shared;


public class Util {
    
    public static String durationFormat(long seconds) {
        long minutes = seconds / 60;
        long remainingSeconds = seconds % 60;
        return minutes + ":" + ((remainingSeconds < 10) ? "0" + remainingSeconds : remainingSeconds);
    }
    
    //todo: checking in commands here
}
