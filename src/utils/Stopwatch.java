package utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Stopwatch {

    private final long startTime;

    public Stopwatch(){
        startTime = System.currentTimeMillis();
    }

    public void getTime(){
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        System.out.println(format.format(new Date((System.currentTimeMillis() - startTime) - 3600000)));
    }

}
