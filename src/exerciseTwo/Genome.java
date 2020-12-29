package exerciseTwo;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Genome implements Comparable<Genome> {

    private int[] route;

    private final double[][] distanceArray;
    private final int geneLen;
    private double fitness;
    private boolean changed;

    public Genome(double[][] distanceArray) {
        this.distanceArray = distanceArray;
        this.geneLen = distanceArray.length;
        this.fitness = 0;
        this.changed = true;

        this.route = new int[this.geneLen];

        initializeRandom();
    }

    public Genome(Genome gen) {
        this.distanceArray = gen.getDistanceArray();
        this.geneLen = gen.getGeneLen();
        this.fitness = gen.getFitness();
        this.changed = gen.isChanged();

        this.route = new int[this.geneLen];

        initializeLikeOld(gen);
    }

    public Genome(int[] route, double[][] distanceArray) {
        this.distanceArray = distanceArray;
        this.geneLen = route.length;
        this.fitness = 0;
        this.changed = true;

        this.route = route;

        updateFitness();
    }

    private void initializeRandom() {
        ArrayList<Integer> checked = new ArrayList<>();
        int index = ThreadLocalRandom.current().nextInt(geneLen);
        for (int i = 0; i < geneLen; i++) {
            while (checked.contains(index)) {
                index = ThreadLocalRandom.current().nextInt(geneLen);
            }
            checked.add(index);
            route[i] = index;
        }
        updateFitness();
    }

    private void initializeLikeOld(Genome gen) {
        System.arraycopy(gen.getRoute(), 0, route, 0, route.length);
    }

    public void updateFitness() {
        if (isChanged()) {
            fitness = 0;

            int previouseCity = -1;

            for (int city : getRoute()) {
                if (previouseCity != -1) {
                    fitness += distanceArray[previouseCity][city];
                }
                previouseCity = city;
            }
            fitness+=distanceArray[route[route.length-1]][route[0]];

            setChanged(false);
        }
    }

    public double getFitness() {
        return fitness;
    }

    public int getGeneLen() {
        return geneLen;
    }

    public int[] getRoute() {
        return route;
    }

    private double[][] getDistanceArray() {
        return this.distanceArray;
    }

    public boolean isChanged() {
        return changed;
    }

    public void setRoute(int[] route) {
        this.route = route;
    }

    public void setChanged(boolean changed) {
        this.changed = changed;
    }


    @Override
    public int compareTo(Genome gen) {
        return Double.compare(this.fitness, gen.getFitness());
    }
}
