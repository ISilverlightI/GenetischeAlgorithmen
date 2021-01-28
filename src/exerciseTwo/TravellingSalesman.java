package exerciseTwo;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import utils.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

public class TravellingSalesman {

    public final double[][] distanceArray;
    public final double[][] sortedDistanceArray;
    private final int geneCnt;                                      // Anzahl der Gene
    private final int geneLen;                                      // Länge der Gene
    private final int maxGenerations;                               // Maximalzahl der Generationen, danach Abbruch
    private double pc;                                              // Rekombinationsrate
    private final double startPc;
    private final double endPc;
    private final double stepPc;
    private double pm;                                              // Mutationsrate
    private final double startPm;
    private final double endPm;
    private final double stepPm;
    private final int replicationScheme;                            // Replikationsschema
    private final int recombinationMethod;                          // general or side specific
    private final int numberOfRunsToAverage;                        // Zahl der Läufe über die gerundet wird
    private final int protectedGenesCount;
    private final int s;
    private final ProgressBar progressBar;
    private double progress;
    private final Label resultLabel;

    private final ExecutorService pool;
    private final ArrayList<TravellingSalesmanTask> threads;        // besitzt alle threads beim Multithreading
    private volatile int readyThreads;                              // threads that finished

    private int overallNeededGenerations;                           // alle Generationen zusammen
    private double maxFitness;                                      // maximal erreichte Fitness

    private final Stopwatch stopwatch;

    public TravellingSalesman(String inputMap, int geneCnt, int maxGenerations, double pc, double pm, int replicationScheme, int recombinationMethod, int numberOfRunsToAverage, boolean protectBest, int s, ProgressBar progressBar, Label resultLabel) throws Exception {
        distanceArray = ReadFile.getDistanceArray(inputMap);
        sortedDistanceArray = getSortedDistanceArray(distanceArray);

        this.geneCnt = geneCnt;
        this.geneLen = distanceArray.length;
        this.maxGenerations = maxGenerations;
        this.pc = pc;
        this.startPc = 0;
        this.endPc = 0;
        this.stepPc = 0;
        this.pm = pm;
        this.startPm = 0;
        this.endPm = 0;
        this.stepPm = 0;
        this.replicationScheme = replicationScheme;
        this.recombinationMethod = recombinationMethod;
        this.numberOfRunsToAverage = numberOfRunsToAverage;

        if (protectBest) {
            this.protectedGenesCount = geneCnt - 1;
        } else {
            this.protectedGenesCount = geneCnt;
        }

        this.s = s;
        this.progressBar = progressBar;
        this.progress = 0;
        this.resultLabel = resultLabel;

        this.pool = null;
        this.threads = null;

        this.overallNeededGenerations = 0;
        this.maxFitness = 0;

        this.stopwatch = new Stopwatch();

        start();
    }

    public TravellingSalesman(String inputMap, int geneCnt, int maxGenerations, double startPc, double endPc, double stepPc, double startPm, double endPm, double stepPm, int replicationScheme, int recombinationMethod, int numberOfRunsToAverage, boolean protectBest, int numberOfThreads, int s, ProgressBar progressBar, Label resultLabel) throws Exception {
        distanceArray = ReadFile.getDistanceArray(inputMap);
        sortedDistanceArray = getSortedDistanceArray(distanceArray);

        this.geneCnt = geneCnt;
        this.geneLen = distanceArray.length;
        this.maxGenerations = maxGenerations;
        this.pc = 0;
        this.startPc = startPc;
        this.endPc = endPc;
        this.stepPc = stepPc;
        this.pm = 0;
        this.startPm = startPm;
        this.endPm = endPm;
        this.stepPm = stepPm;
        this.replicationScheme = replicationScheme;
        this.recombinationMethod = recombinationMethod;
        this.numberOfRunsToAverage = numberOfRunsToAverage;

        if (protectBest) {
            this.protectedGenesCount = geneCnt - 1;
        } else {
            this.protectedGenesCount = geneCnt;
        }

        this.s = s;
        this.progressBar = progressBar;
        this.progress = 0;
        this.resultLabel = resultLabel;

        this.threads = new ArrayList<>();
        if (numberOfThreads == 0) {
            this.pool = null;
        } else {
            this.pool = Executors.newFixedThreadPool(numberOfThreads);
        }

        this.overallNeededGenerations = 0;
        this.maxFitness = 0;

        this.stopwatch = new Stopwatch();

        startOptimization();
    }

    public void start() {

        TravellingSalesmanTask task = new TravellingSalesmanTask(0, this.pc, this.pm);
        task.task();
        //task.test(); not good, was just to test a smart idea
        progressBar.setProgress(1);
        //Controller.setRunning(false);

        Arrays.sort(task.genes, Collections.reverseOrder());

        // print results
        // printResults(task);
        Print.printAllResults(task.getOverallNeededGenerations(), numberOfRunsToAverage, task.pm, task.pc, geneLen, geneCnt, recombinationMethod, replicationScheme, task.genes[geneCnt - 1].getFitness(), task.genes[geneCnt - 1].getRoute());
    }

    public void startOptimization() {

        int counter = 0;

        for (this.pc = this.endPc; this.pc >= (this.startPc); this.pc -= this.stepPc) {
            pm = 0;
            for (this.pm = this.endPm; this.pm >= (this.startPm); this.pm -= this.stepPm) {

                threads.add(new TravellingSalesmanTask(counter, pc, pm));
                pool.execute(threads.get(counter));

                counter++;
            }
        }
    }

    private double[][] getSortedDistanceArray(double[][] distanceArray) {

        double[][] sortedDistanceArray = Arrays.stream(distanceArray)
                .map(a -> Arrays.copyOf(a, a.length))
                .toArray(double[][]::new);

        for (int i = 0; i < sortedDistanceArray.length; i++) {
            ArrayList<Integer> usedCities = new ArrayList<>();
            Arrays.sort(sortedDistanceArray[i]);
            for (int j = 0; j < sortedDistanceArray[i].length; j++) {
                for (int k = 0; k < distanceArray[i].length; k++) {
                    if (sortedDistanceArray[i][j] == distanceArray[i][k] && !usedCities.contains(k)) {
                        sortedDistanceArray[i][j] = k;
                        usedCities.add(k);
                        break;
                    }
                }
            }
        }

        return sortedDistanceArray;
    }

    private void printResults(TravellingSalesmanTask bestTask) {
        Platform.runLater(() -> resultLabel.setText("\t\tBeste Ergebnisse bei:\t\npc: " + FileWriter.cleanupString(bestTask.getPc()) + "     pm: " + FileWriter.cleanupString(bestTask.getPm()) + "     Generationen: " + bestTask.getGener()));
    }

    public class TravellingSalesmanTask implements Runnable {

        private final int id;

        private final double pc;
        private final double pm;
        private Genome[] genes;

        double[] psKum;

        private int generation;                                         // aktuelle Generation

        private int overallNeededGenerations;                           // alle Generationen zusammen

        private double maxFitness;                                      // maximal erreichte Fitness
        private int gener;                                              // benötigte Generationen im durchschnitt

        public TravellingSalesmanTask(int id, double pc, double pm) {
            this.id = id;
            this.pc = pc;
            this.pm = pm;

            this.genes = new Genome[geneCnt];

            if (replicationScheme == 2)
                psKum = generatePsKum();

            this.generation = 0;
            this.overallNeededGenerations = 0;
            this.maxFitness = -1;
            this.gener = 0;
        }

        @Override
        public void run() {
            overallNeededGenerations = 0;

            task();

            checkFinished();
        }

        private void task() {
            for (int i = 0; i < numberOfRunsToAverage; i++) {

                // System.out.println("Run: " + (i + 1) + " of " + numberOfRunsToAverage);

                initializeGenes();

                // Print.printRunsStartFitness(i, genes);

                do {
                    // set generation
                    generation += 1;

                    // 1. Mutate
                    mutateGene();

                    // -> sort array with genes and check if fitness is minimal (fitness got automatically updated with it's mutation)
                    Arrays.sort(genes, Collections.reverseOrder());
                    if ((genes[geneCnt - 1].getFitness() <= 612/*41.66*//*geneLen*//*604*/)) {
                        break;
                    }

                    // 2. Cross-Over
                    crossGenes(recombinationMethod);

                    // -> check if max fitness is reached after the changed genes fitness got updated
                    sortArrayByFitness();
                    if ((genes[geneCnt - 1].getFitness() <= 612/*41.66*//*geneLen*//*604*/)) {
                        break;
                    }

                    // 3. Replicate
                    replicateGenes(replicationScheme);

                    Arrays.sort(genes, Collections.reverseOrder());

                    // loop until max fitness or max generations is reached
                } while (generation < maxGenerations);

                // Print.printGenerationsMaxFitness(generation, gene, geneCnt);

                overallNeededGenerations += generation;
                updateOverallMaxFitness();

//                System.out.println("actual best fitness: " + genes[geneCnt - 1].getFitness() + "\n");
            }
            gener = (overallNeededGenerations / numberOfRunsToAverage);
        }

        private void initializeGenes() {
            for (int i = 0; i < geneCnt; i++) {
                genes[i] = new Genome(distanceArray);
            }
            generation = 0;
        }

        private void swapGenePos(Genome gen, int pos1, int pos2) {

            int pos1copy = gen.getRoute()[pos1];
            gen.getRoute()[pos1] = gen.getRoute()[pos2];
            gen.getRoute()[pos2] = pos1copy;

            gen.setChanged(true);
        }

        private void crossGenes(int recombinationMethod) {
            switch (recombinationMethod) {
                case 1:
                    // greedy Crossover
                    Genome[] newGenes = new Genome[geneCnt];
                    // save best - to get sure, that we dont miss it -> gets overwritten if protectBest=false
                    newGenes[geneCnt - 1] = genes[geneCnt - 1];

                    for (int i = 0; i < protectedGenesCount; i++) {
                        if (i < (protectedGenesCount * pc)) {
                            newGenes[i] = new Genome(crossTwoGenes(genes[ThreadLocalRandom.current().nextInt(protectedGenesCount)], genes[ThreadLocalRandom.current().nextInt(protectedGenesCount)]), distanceArray);
                        } else {
                            newGenes[i] = genes[ThreadLocalRandom.current().nextInt(geneCnt)];
                        }
                    }

                    genes = newGenes;
                    break;
                case 2:
                    // Front-rear
                    for (int i = 0; i < (protectedGenesCount * pc); i++) {
                        frontRearTwoGenes(genes[ThreadLocalRandom.current().nextInt(protectedGenesCount)], genes[ThreadLocalRandom.current().nextInt(protectedGenesCount)], ThreadLocalRandom.current().nextInt(geneLen));
                    }
                    break;
                default:
                    throw new IllegalArgumentException("no valid recombinationMethod: " + recombinationMethod);
            }
        }

        private int[] crossTwoGenes(Genome gen1, Genome gen2) {

            int[] newRoute = new int[gen1.getGeneLen()];

            //start with first city of gen1
            int startCity = gen1.getRoute()[0];
            int nextCity1;
            int nextCity2;

            //save first in new routes list
            newRoute[0] = startCity;

            for (int i = 1; i < newRoute.length; i++) {

                nextCity1 = findNextViableCity(gen1.getRoute(), startCity, newRoute, i);
                nextCity2 = findNextViableCity(gen2.getRoute(), startCity, newRoute, i);

                if (distanceArray[startCity][nextCity1] < distanceArray[startCity][nextCity2]) {
                    newRoute[i] = nextCity1;
                } else {
                    newRoute[i] = nextCity2;
                }
                startCity = newRoute[i];
            }

            return newRoute;
        }

        private int findNextViableCity(int[] route, int city, int[] newRoute, int upperBound) {
            int nextCity;
            // go find next city of parent 1
            nextCity = findNextCity(route, city);
            //search until u got a city, that's not in the new routes

            /* TODO: TEST THIS
            if (ArrayUtils.contains(newRoute, nextCity, upperBound)) {
                nextCity = findNextClosestCity(city, newRoute, upperBound);
            }
            */
            while (ArrayUtils.contains(newRoute, nextCity, upperBound)) {
                nextCity = findNextCity(route, nextCity);
            }

            return nextCity;
        }

        //Todo: not implemented or tested yet
        private int findNextClosestCity(int city, int[] newRoute, int upperBound) {
            int i = 1;

            int nextCity = (int) sortedDistanceArray[city][i];
            while (ArrayUtils.contains(newRoute, nextCity, upperBound)) {
                i++;
                nextCity = (int) sortedDistanceArray[city][i];
            }
            return nextCity;
        }

        private int findNextCity(int[] route, int searchCity) {
            int index = findIndexOf(route, searchCity);
            if (index + 1 < route.length) {
                return route[index + 1];
            } else {
                return route[0];
            }
        }

        private int findIndexOf(int[] array, int value) {
            for (int i = 0; i < array.length; i++) {
                if (array[i] == value) {
                    return i;
                }
            }
            return -1;
        }

        private void frontRearTwoGenes(Genome gen1, Genome gen2, int pos) {
            if (pos > geneLen) {
                throw new IllegalArgumentException("pos has to be lower than " + geneLen + " (\"geneLen\")");
            }

            // copy values of gen1 to make sure that gen2 can get values of gen1 after override
            Genome gen1copy = new Genome(gen1);
            Genome gen2copy = new Genome(gen2);

            //Front-rear gen1
            System.arraycopy(gen2.getRoute(), pos, gen1.getRoute(), 0, gen2.getGeneLen() - pos);
            System.arraycopy(gen1copy.getRoute(), 0, gen1.getRoute(), gen1.getGeneLen() - pos, pos);
            //Front-rear gen2
            System.arraycopy(gen1.getRoute(), pos, gen2.getRoute(), 0, gen1.getGeneLen() - pos);
            System.arraycopy(gen2copy.getRoute(), 0, gen2.getRoute(), gen2.getGeneLen() - pos, pos);

            gen1.setChanged(true);
            gen2.setChanged(true);
        }

        private void mutateGene() {
            for (int i = 0; i < (protectedGenesCount * geneLen * pm); i++) {
                swapGenePos(genes[ThreadLocalRandom.current().nextInt(protectedGenesCount)], ThreadLocalRandom.current().nextInt(geneLen), ThreadLocalRandom.current().nextInt(geneLen));
            }
        }

        private void updateFitness() {
            for (int i = 0; i < geneCnt; i++) {
                genes[i].updateFitness();
            }
        }

        private void sortArrayByFitness() {
            updateFitness();
            Arrays.sort(genes, Collections.reverseOrder());
        }

        private void replicateGenes(int replicationScheme) {
            switch (replicationScheme) {
                case 0:
                    // 50x2
                    for (int i = 0; i < geneCnt / 2; i++) {
                        genes[i] = new Genome(genes[i + (geneCnt / 2)]);
                    }
                    break;
                case 1:
                    // 2x50
                    Genome best1 = genes[geneCnt - 1];
                    Genome best2 = genes[geneCnt - 2];
                    for (int i = 0; i < geneCnt / 2; i++) {
                        genes[i] = new Genome(genes[ThreadLocalRandom.current().nextInt(geneCnt)]);
                    }
                    for (int i = 0; i < geneCnt / 4; i++) {
                        genes[i + (geneCnt / 2)] = new Genome(best2);
                    }
                    for (int i = 0; i < geneCnt / 4; i++) {
                        genes[i + (geneCnt * 3 / 4)] = new Genome(best1);
                    }
                    break;
                case 2: //DEPRECATED Todo: reimplement
                    // rank based
                    Genome[] newGeneration = new Genome[geneCnt];
                    int chosenOne;
                    List<Integer> chosen = new ArrayList<>();
                    for (int i = 0; i < geneCnt; i++) {
                        chosenOne = Arrays.binarySearch(psKum, ThreadLocalRandom.current().nextDouble(1));
                        if (chosenOne < 0) {
                            chosenOne = -chosenOne - 1;
                        }
                        if (chosen.contains(chosenOne)) {
                            newGeneration[i] = new Genome(genes[chosenOne-1]);
                        } else {
                            newGeneration[i] = genes[chosenOne-1];
                            chosen.add(chosenOne);
                        }
                    }
                    genes = newGeneration;
                    break;
                default:
                    throw new IllegalArgumentException("no valid replicationScheme: " + replicationScheme);
            }
        }

        @Deprecated
        private double[] generatePsKum() {
            double[] psKum = new double[geneCnt];
            psKum[0] = 0;
            for (int i = 1; i < geneCnt; i++) {
                psKum[i] = getPs(i) + psKum[i - 1];
            }
            return psKum;
        }

        @Deprecated
        private double getPs(int i) {
            int n = geneCnt;
            double res1 = ((2 - (double) s) / (double) n);
            double res2 = (2 * (double) i * ((double) s - 1));
            double res3 = ((double) n * ((double) n - 1));
            double res4 = res2 / res3;

            return res1 + res4;
        }

        private void updateOverallMaxFitness() {
            if (maxFitness < 0 || genes[geneCnt - 1].getFitness() < maxFitness) {
                maxFitness = genes[geneCnt - 1].getFitness();
            }
        }

        public int getOverallNeededGenerations() {
            return overallNeededGenerations;
        }

        public double getMaxFitness() {
            return maxFitness;
        }

        public int getId() {
            return id;
        }

        public double getPc() {
            return pc;
        }

        public double getPm() {
            return pm;
        }

        public int getGener() {
            return gener;
        }

        private synchronized void checkFinished() {
            readyThreads++;

            System.out.print("\r" + readyThreads + "/" + threads.size() + " - estimated time: " + stopwatch.getEstimatedTime(readyThreads, threads.size()) + " - best fitness: " + genes[geneCnt - 1].getFitness());
            assert threads != null;
            progressBar.setProgress(progress += (1 / (double) threads.size()));

            if (readyThreads == threads.size()) {
                TravellingSalesmanTask bestTask = threads.get(0);
                for (TravellingSalesmanTask thread : threads) {
                    if (thread.genes[geneCnt - 1].getFitness() < bestTask.genes[geneCnt - 1].getFitness()) {
                        bestTask = thread;
                    } else if (thread.genes[geneCnt - 1].getFitness() == bestTask.genes[geneCnt - 1].getFitness()) {
                        if (thread.getGener() < bestTask.getGener()) {
                            bestTask = thread;
                        }
                    }
                }

                printResults(bestTask);

                Print.printAllResults(bestTask.overallNeededGenerations, numberOfRunsToAverage, bestTask.pm, bestTask.pc, geneLen, geneCnt, recombinationMethod, replicationScheme, bestTask.maxFitness, bestTask.genes[geneCnt - 1].getRoute());

                //Controller.setRunning(false);

                System.out.print("\nBefore writing:\t");
                stopwatch.getTime();

                FileWriter.writeTravellingSalesman(threads);

                System.out.print("\nAfter writing:\t");
                stopwatch.getTime();

                assert pool != null;
                pool.shutdown();
            }
        }

        public void test() {
            for (int i = 0; i < numberOfRunsToAverage; i++) {

                System.out.println("Run: " + (i + 1) + " of " + numberOfRunsToAverage);

                initializeGenes();

                for (Genome gene : genes) {
                    gene.setRoute(smartCheck(gene));
                }

                // -> check if max fitness is reached after the changed genes fitness got updated
                sortArrayByFitness();

                overallNeededGenerations += generation;
                updateOverallMaxFitness();

                System.out.println("actual best fitness: " + genes[geneCnt - 1].getFitness() + "\n");
            }
            gener = (overallNeededGenerations / numberOfRunsToAverage);
        }

        private int[] smartCheck(Genome gen) {
            int[] newRoute = new int[geneLen];

            //start with first city of gen1
            int startCity = gen.getRoute()[0];

            //save first in new routes list
            newRoute[0] = startCity;

            for (int i = 1; i < newRoute.length; i++) {
                newRoute[i] = findNextClosestCity(startCity, newRoute, i);
                startCity = newRoute[i];
            }

            return newRoute;
        }
    }
}
