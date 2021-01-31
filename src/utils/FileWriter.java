package utils;

import exerciseOne.AllOneIsFittest.AllOneIsFittestTask;
import exerciseTwo.TravellingSalesman.TravellingSalesmanTask;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FileWriter {

    private static final String PATH = "C:\\Users\\janro\\IdeaProjects\\GenetischeAlgorithmen\\output\\";
    private static final String TIME_FORMAT = "yyyy_MM_dd_HH_mm";
    private static final String REGEX0 = "0000000[0-9]*";
    private static final String REGEX9 = "9999999[0-9]*";

    public static void write(String data, String name) {
        try {
            Files.write(Paths.get(PATH + name + "_" + getTimestamp() + ".txt"), data.getBytes(), StandardOpenOption.CREATE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeAllOneIsFittest(ArrayList<AllOneIsFittestTask> list) {
        StringBuilder stringBuilder = new StringBuilder(10000);

        double lastPc = 0;

        for (AllOneIsFittestTask allOneIsFittestTask : list) {
            if(lastPc != allOneIsFittestTask.getPc()){
                if (lastPc != 0){
                    stringBuilder.append("\n");
                }
                lastPc = allOneIsFittestTask.getPc();
            }
            stringBuilder.append(cleanupString(allOneIsFittestTask.getPm()))
                    .append(" ")
                    .append(cleanupString(allOneIsFittestTask.getPc()))
                    .append(" ")
                    .append(cleanupString(allOneIsFittestTask.getGener()))
                    .append("\n");
        }

        write(stringBuilder.toString(), "allOneIsFittest");
    }

    public static void writeTravellingSalesman(ArrayList<TravellingSalesmanTask> list) {
        StringBuilder stringBuilder = new StringBuilder(10000);

        double lastPc = 0;

        for (TravellingSalesmanTask travellingSalesmanTask : list) {
            if(lastPc != travellingSalesmanTask.getPc()){
                if (lastPc != 0){
                    stringBuilder.append("\n");
                }
                lastPc = travellingSalesmanTask.getPc();
            }
            stringBuilder.append(cleanupString(travellingSalesmanTask.getPm()))
                    .append(" ")
                    .append(cleanupString(travellingSalesmanTask.getPc()))
                    .append(" ")
                    .append(cleanupString(travellingSalesmanTask.getGener()))
                    .append("\n");
        }

        write(stringBuilder.toString(), "travelingSalesman");
    }

    public static String cleanupString(double value) {
        return String.valueOf(value).replaceAll(REGEX0, "").replaceAll(REGEX9, "");
    }

    private static String cleanupString(int value) {
        return String.valueOf(value).replaceAll(REGEX0, "");
    }

    private static String getTimestamp() {
        return new SimpleDateFormat(TIME_FORMAT).format(new Date());
    }

}
