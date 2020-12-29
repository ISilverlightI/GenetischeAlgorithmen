package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class ReadFile {

    private static final String FILE_PATH = "C:\\Users\\janro\\IdeaProjects\\GenetischeAlgorithmen\\input\\";

    public static int[][] getArrayFromString(String fileName) throws Exception {
        int[][] cities;
        String readLine;
        ArrayList<int[]> values = new ArrayList<>();
        try {
            File file = new File(FILE_PATH + fileName);
            BufferedReader reader = new BufferedReader(new FileReader(file));

            // read file
            while ((readLine = reader.readLine()) != null) {
                //save line into arraylist
                values.add(Arrays.stream(readLine.trim().split("\\s+")).mapToInt(Integer::parseInt).toArray());
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        cities = new int[values.size()][values.get(0).length];

        for (int i = 0; i < cities.length; i++) {
            System.arraycopy(values.get(i), 0, cities[i], 0, cities.length);
        }

        return cities;
    }

    private static int countCities(int[][] cityMap) {
        int count = 0;
        for (int[] cities : cityMap) {
            for (int city : cities) {
                if (city != 0 && city > count) {
                    count = city;
                }
            }
        }
        return count;
    }

    public static double[][] getDistanceArray(String fileName) throws Exception {
        int[][] cityMap = getArrayFromString(fileName);
        int cityCount = countCities(cityMap);
        int[][] cities = new int[cityCount][2];
        double[][] distanceArray = new double[cityCount][cityCount];

        for (int i = 0; i < cityMap.length; i++) {
            for (int j = 0; j < cityMap[0].length; j++) {
                int cityNumber = cityMap[i][j];
                if (cityNumber != 0) {
                    cities[cityNumber - 1][0] = i;
                    cities[cityNumber - 1][1] = j;
                }
            }
        }

        //System.out.println("1: x = " + cities[0][0] + ",y = " + cities[0][1]);
        //System.out.println("2: x = " + cities[1][0] + ",y = " + cities[1][1]);

        for (int i = 0; i < distanceArray.length; i++) {
            for (int j = 0; j < distanceArray[0].length; j++) {
                distanceArray[i][j] = getDistance(cities[i], cities[j]);
            }
        }

        //System.out.println(distanceArray[128][110]);
        //System.out.println(distanceArray[110][128]);

        return distanceArray;
    }

    private static double getDistance(int[] cityA, int[] cityB) {
        return pythagoras(Math.abs(cityA[0] - cityB[0]), Math.abs(cityA[1] - cityB[1]));
    }

    private static double pythagoras(int x, int y) {
        return Math.sqrt((x * x) + (y * y));
    }
}