package exerciseOne;

import java.util.concurrent.ThreadLocalRandom;

public class Genome implements Comparable<Genome> {

    private final int geneLen;
    private final double initRate;

    private final int[] values;
    private int fitness;

    private boolean changed;

    public Genome(int geneLen) {
        this.geneLen = geneLen;
        this.initRate = 0;
        this.fitness = 0;
        this.changed = false;

        values = new int[this.geneLen];

        initializeZero();
    }

    public Genome(int geneLen, double initRate) {
        this.geneLen = geneLen;
        this.initRate = initRate;
        this.fitness = 0;
        this.changed = false;

        values = new int[this.geneLen];

        initializeRandom();
    }

    public Genome(Genome gen) {
        this.geneLen = gen.getGeneLen();
        this.initRate = gen.getInitRate();
        this.fitness = gen.getFitness();
        this.changed = false;

        values = new int[this.geneLen];

        initializeLikeOld(gen);
    }

    private void initializeZero() {
        for (int i = 0; i < geneLen; i++) {
            values[i] = 0;
        }
    }

    private void initializeRandom() {
        for (int i = 0; i < geneLen; i++) {
            if (ThreadLocalRandom.current().nextFloat() <= initRate) {
                values[i] = 1;
                this.setChanged(true);
            }
        }
    }

    private void initializeLikeOld(Genome gen) {
        System.arraycopy(gen.getValues(), 0, values, 0, values.length);
    }

    public void updateFitness() {
        if (isChanged()) {
            fitness = 0;

            for (int value : getValues()) {
                fitness +=value;
            }
        }
    }

    public int getFitness() {
        return fitness;
    }

    public int getGeneLen() {
        return geneLen;
    }

    public double getInitRate() {
        return initRate;
    }

    public int[] getValues() {
        return values;
    }

    public boolean isChanged() {
        return changed;
    }

    public void changeFitness(int fitnessToAdd){
        fitness += fitnessToAdd;
    }

    public void setChanged(boolean changed) {
        this.changed = changed;
    }

    @Override
    public int compareTo(Genome gen) {
        return Integer.compare(this.fitness, gen.getFitness());
    }
}
