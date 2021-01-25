package utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Stopwatch {

    private final SimpleDateFormat format;
    private final long startTime;

    public Stopwatch(){
        startTime = System.currentTimeMillis();
        format = new SimpleDateFormat("HH:mm:ss");
    }

    public void getTime(){
        System.out.println(format.format(new Date((System.currentTimeMillis() - startTime) - 3600000)));
    }

    public String getEstimatedTime(int readyThreads, int totalThreads){
        long timeLeft;
        long runningTime = System.currentTimeMillis() - startTime;

        timeLeft = ((runningTime/readyThreads)*totalThreads)-runningTime;
        return format.format(new Date((timeLeft) - 3600000));
    }

}