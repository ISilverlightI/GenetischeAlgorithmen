package exerciseOne;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import utils.FileWriter;
import utils.Stopwatch;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

public class AllOneIsFittest {

    private final int geneCnt;                                      // Anzahl der Gene
    private final double initRate;                                  // Initiationsrate
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
    private final double acceptRate;                                // akzeptanzrate in Prozent
    private final int protectedGenesCount;
    private final int s;
    private ProgressBar progressBar;
    private double progress;
    private Label resultLabel;

    private final boolean initializeLikeCourse;                     // Initialisierung, wie vom Dozenten (oder von mir)
    private final ExecutorService pool;
    private final ArrayList<AllOneIsFittestTask> threads;           // besitzt alle threads beim Multithreading
    private volatile int readyThreads;                              // threads that finished

    private int overallNeededGenerations;                           // alle Generationen zusammen
    private int maxFitness;                                         // maximal erreichte Fitness

    private final Stopwatch stopwatch;

    public AllOneIsFittest(int geneCnt, double initRate, int geneLen, int maxGenerations, double pc, double pm, int replicationScheme, int recombinationMethod, int numberOfRunsToAverage, boolean protectBest, double acceptRate, boolean initializeLikeCourse, int s, ProgressBar progressBar, Label resultLabel) {
        this.geneCnt = geneCnt;
        this.initRate = initRate;
        this.geneLen = geneLen;
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
        this.acceptRate = acceptRate;

        if (protectBest) {
            this.protectedGenesCount = geneCnt - 1;
        } else {
            this.protectedGenesCount = geneCnt;
        }

        this.s = s;
        this.progressBar = progressBar;
        this.progress = 0;
        this.resultLabel = resultLabel;

        this.initializeLikeCourse = initializeLikeCourse;
        this.pool = null;
        this.threads = null;

        this.overallNeededGenerations = 0;
        this.maxFitness = 0;

        this.stopwatch = new Stopwatch();

        start();
    }

    public AllOneIsFittest(int geneCnt, double initRate, int geneLen, int maxGenerations, double startPc, double endPc, double stepPc, double startPm, double endPm, double stepPm, int replicationScheme, int recombinationMethod, int numberOfRunsToAverage, boolean protectBest, double acceptRate, boolean initializeLikeCourse, int numberOfThreads, int s, ProgressBar progressBar, Label resultLabel) {
        this.geneCnt = geneCnt;
        this.initRate = initRate;
        this.geneLen = geneLen;
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
        this.acceptRate = acceptRate;

        if (protectBest) {
            this.protectedGenesCount = geneCnt - 1;
        } else {
            this.protectedGenesCount = geneCnt;
        }

        this.s = s;
        this.progressBar = progressBar;
        this.progress = 0;
        this.resultLabel = resultLabel;

        this.initializeLikeCourse = initializeLikeCourse;
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

        AllOneIsFittestTask task = new AllOneIsFittestTask(0, this.pc, this.pm);
        task.task();
        progressBar.setProgress(1);
        Controller.setRunning(false);

        this.overallNeededGenerations = task.getOverallNeededGenerations();
        this.maxFitness = task.getMaxFitness();

        // print results
        printResults(task);
        // printAllResults();
    }

    public void startOptimization() {

        int counter = 0;

        for (this.pc = this.startPc; this.pc <= (this.endPc); this.pc += this.stepPc) {
            pm = 0;
            for (this.pm = this.startPm; this.pm <= (this.endPm); this.pm += this.stepPm) {

                threads.add(new AllOneIsFittestTask(counter, pc, pm));
                pool.execute(threads.get(counter));

                counter++;
            }
        }
    }

    private void printResults(AllOneIsFittestTask bestTask) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                resultLabel.setText("\t\tBeste Ergebnisse bei:\t\npc: " + FileWriter.cleanupString(bestTask.getPc()) + "     pm: " + FileWriter.cleanupString(bestTask.getPm()) + "     Generationen: " + bestTask.getGener());
            }
        });
    }

    private void printSomeResults(AllOneIsFittestTask task) {
        System.out.println("id: " + task.id);
        System.out.println("pm: " + task.pm);
        System.out.println("pc: " + task.pc);
        System.out.println("average needed generations: " + task.gener);
        System.out.println();
    }

    private void printAllResults() {
        System.out.println("average needed generations: " + (overallNeededGenerations / numberOfRunsToAverage));
        System.out.println("pm: " + pm);
        System.out.println("pc: " + pc);
        System.out.println("geneCnt: " + geneLen);
        System.out.println("geneLen: " + geneCnt);
        System.out.println("initRate: " + initRate);
        System.out.println("crossover-method: " + recombinationMethod);
        System.out.println("replication-scheme: " + replicationScheme);
        System.out.println("number of runs: " + numberOfRunsToAverage);
        System.out.println("accept rate: " + acceptRate);
        System.out.println("max gene fitness: " + maxFitness + "\n");
    }

    public class AllOneIsFittestTask implements Runnable {

        private final int id;
        private final double pc;
        private final double pm;

        private Genome[] gene;

        double[] psKum;

        private int generation;                                         // aktuelle Generation

        private int overallNeededGenerations;                           // alle Generationen zusammen
        private int maxFitness;                                         // maximal erreichte Fitness
        private int gener;                                              // benötigte Generationen im durchschnitt

        public AllOneIsFittestTask(int id, double pc, double pm) {
            this.id = id;
            this.pc = pc;
            this.pm = pm;

            this.gene = new Genome[geneCnt];

            if (replicationScheme == 2)
                psKum = generatePsKum();

            this.generation = 0;
            this.overallNeededGenerations = 0;
            this.maxFitness = 0;
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

                if (initializeLikeCourse) {
                    initializeForAll();
                } else {
                    initializeForEach();
                }

//            printRunsStartFitness(i);

                do {
                    // set generation
                    generation += 1;

                    // 1. Mutate
                    mutateGene();

                    // -> sort array with genes and check if max fitness is reached (fitness got automatically updated with it's mutation)
                    Arrays.sort(gene);
                    if (!(gene[geneCnt - 1].getFitness() < (geneLen * acceptRate))) {
                        break;
                    }

                    // 2. Cross-Over
                    crossGenes(recombinationMethod);

                    // -> check if max fitness is reached after the changed genes fitness got updated
                    sortArrayByFitness();
                    if (!(gene[geneCnt - 1].getFitness() < (geneLen * acceptRate))) {
                        break;
                    }

                    // 3. Replicate
                    replicateGenes(replicationScheme);

                    // loop until max fitness or max generations is reached
                } while (generation < maxGenerations);

//                printGenerationsMaxFitness();

                overallNeededGenerations += generation;
                updateOverallMaxFitness();
            }
            gener = (overallNeededGenerations / numberOfRunsToAverage);
        }

        private void initializeForEach() {
            for (int i = 0; i < geneCnt; i++) {
                gene[i] = new Genome(geneLen, initRate);
            }

            generation = 0;
        }

        private void initializeForAll() {
            for (int i = 0; i < geneCnt; i++) {
                gene[i] = new Genome(geneLen);
            }

            int geneIndex;
            int genePos;
            for (int i = 0; i < (geneCnt * geneLen * initRate); i++) {
                geneIndex = ThreadLocalRandom.current().nextInt(geneCnt);
                genePos = ThreadLocalRandom.current().nextInt(geneLen);
                setGenePos(gene[geneIndex], genePos, 1);
            }

            generation = 0;
        }

        private void setGenePos(Genome gen, int pos, int value) {
            switch (value) {
                case 0:
                    gen.getValues()[pos] = 0;
                    gen.setChanged(true);
                    break;
                case 1:
                    gen.getValues()[pos] = 1;
                    gen.setChanged(true);
                    break;
                case -1:
                    if (gen.getValues()[pos] == 1) {
                        gen.getValues()[pos] = 0;
                        gen.changeFitness(-1);
                    } else {
                        gen.getValues()[pos] = 1;
                        gen.changeFitness(+1);
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Value has to get one of the following values: 0, 1, -1");
            }
        }

        private void crossGenes(int recombinationMethod) {
            switch (recombinationMethod) {
                case 1:
                    // Crossover
                    for (int i = 0; i < (protectedGenesCount * pc); i++) {
                        crossTwoGenes(gene[ThreadLocalRandom.current().nextInt(protectedGenesCount)], gene[ThreadLocalRandom.current().nextInt(protectedGenesCount)], ThreadLocalRandom.current().nextInt(geneLen));
                    }
                    break;
                case 2:
                    // Front-rear
                    for (int i = 0; i < (protectedGenesCount * pc); i++) {
                        frontRearTwoGenes(gene[ThreadLocalRandom.current().nextInt(protectedGenesCount)], gene[ThreadLocalRandom.current().nextInt(protectedGenesCount)], ThreadLocalRandom.current().nextInt(geneLen));
                    }
                    break;
                default:
                    throw new IllegalArgumentException("no valid recombinationMethod: " + recombinationMethod);
            }
        }

        private void crossTwoGenes(Genome gen1, Genome gen2, int pos) {
            if (pos > geneLen) {
                throw new IllegalArgumentException("pos has to be lower than " + geneLen + " (\"geneLen\")");
            }

            // copy values of gen1 to make sure that gen2 can get values of gen1 after override
            Genome gen1copy = new Genome(gen1);

            System.arraycopy(gen2.getValues(), pos, gen1.getValues(), pos, gen1.getGeneLen() - pos);
            System.arraycopy(gen1copy.getValues(), pos, gen2.getValues(), pos, gen1.getGeneLen() - pos);

            gen1.setChanged(true);
            gen2.setChanged(true);

        }

        private void frontRearTwoGenes(Genome gen1, Genome gen2, int pos) {
            if (pos > geneLen) {
                throw new IllegalArgumentException("pos has to be lower than " + geneLen + " (\"geneLen\")");
            }

            // copy values of gen1 to make sure that gen2 can get values of gen1 after override
            Genome gen1copy = new Genome(gen1);
            Genome gen2copy = new Genome(gen2);

            //Front-rear gen1
            System.arraycopy(gen2.getValues(), pos, gen1.getValues(), 0, gen2.getGeneLen() - pos);
            System.arraycopy(gen1copy.getValues(), 0, gen1.getValues(), gen1.getGeneLen() - pos, pos);
            //Front-rear gen2
            System.arraycopy(gen1.getValues(), pos, gen2.getValues(), 0, gen1.getGeneLen() - pos);
            System.arraycopy(gen2copy.getValues(), 0, gen2.getValues(), gen2.getGeneLen() - pos, pos);

            gen1.setChanged(true);
            gen2.setChanged(true);
        }

        private void mutateGene() {
            for (int i = 0; i < (protectedGenesCount * geneLen * pm); i++) {
                setGenePos(gene[ThreadLocalRandom.current().nextInt(protectedGenesCount)], ThreadLocalRandom.current().nextInt(geneLen), -1);
            }
        }

        private void updateFitness() {
            for (int i = 0; i < geneCnt; i++) {
                gene[i].updateFitness();
            }
        }

        private void sortArrayByFitness() {
            updateFitness();
            Arrays.sort(gene);
        }

        private void replicateGenes(int replicationScheme) {
            switch (replicationScheme) {
                case 1:
                    // 50x2
                    for (int i = 0; i < geneCnt / 2; i++) {
                        gene[i] = new Genome(gene[i + (geneCnt / 2)]);
                    }
                    break;
                case 2:
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
                            newGeneration[i] = new Genome(gene[chosenOne]);
                        } else {
                            newGeneration[i] = gene[chosenOne];
                            chosen.add(chosenOne);
                        }
                    }
                    gene = newGeneration;
                    break;
                default:
                    throw new IllegalArgumentException("no valid replicationScheme: " + replicationScheme);
            }
        }

        private double[] generatePsKum() {
            double[] psKum = new double[geneCnt];
            psKum[0] = 0;
            for (int i = 1; i < geneCnt; i++) {
                psKum[i] = getPs(i) + psKum[i - 1];
            }
            return psKum;
        }

        private double getPs(int i) {
            int n = geneCnt;
            double res1 = ((2 - (double) s) / (double) n);
            double res2 = (2 * (double) i * ((double) s - 1));
            double res3 = ((double) n * ((double) n - 1));
            double res4 = res2 / res3;

            return res1 + res4;
        }

        private void updateOverallMaxFitness() {
            if (maxFitness < gene[geneCnt - 1].getFitness()) {
                maxFitness = gene[geneCnt - 1].getFitness();
            }
        }

        public int getOverallNeededGenerations() {
            return overallNeededGenerations;
        }

        public int getMaxFitness() {
            return maxFitness;
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

            System.out.print("\r" + readyThreads);
            assert threads != null;
            progressBar.setProgress(progress += (1 / (double) threads.size()));

            if (readyThreads == threads.size()) {
                AllOneIsFittestTask bestTask = threads.get(0);
                for (AllOneIsFittestTask thread : threads) {
                    if (thread.getGener() < bestTask.getGener()) {
                        bestTask = thread;
                    }
                }

                printResults(bestTask);

                Controller.setRunning(false);

                System.out.print("\nBefore writing:\t");
                stopwatch.getTime();

                FileWriter.write(threads);

                System.out.print("\nAfter writing:\t");
                stopwatch.getTime();

                assert pool != null;
                pool.shutdown();
            }
        }

        private void printRunsStartFitness(int runNumber) {
            updateFitness();
            int overallFitness = 0;
            for (Genome gen : gene) {
                overallFitness += gen.getFitness();
            }
            System.out.println("run number: " + (runNumber + 1));
            System.out.println("overallFitness: " + overallFitness);
        }

        private void printGenerationsMaxFitness() {
            System.out.println("generation " + generation + ": \n" + "max fitness ->" + gene[geneCnt - 1].getFitness() + "\n");
        }

    }
}
