package utils;

import exerciseOne.AllOneIsFittest;
import exerciseTwo.TravellingSalesman;

public class Print {

    public static void printRunsStartFitness(int runNumber, exerciseOne.Genome[] gene) {
        //updateFitness(); //Todo: check if this makes a difference and fix it if needed
        int overallFitness = 0;
        for (exerciseOne.Genome gen : gene) {
            overallFitness += gen.getFitness();
        }
        System.out.println("run number: " + (runNumber + 1));
        System.out.println("overallFitness: " + overallFitness);
    }

    public static void printRunsStartFitness(int runNumber, exerciseTwo.Genome[] gene) {
        //updateFitness(); //Todo: check if this makes a difference and fix it if needed
        int overallFitness = 0;
        for (exerciseTwo.Genome gen : gene) {
            overallFitness += gen.getFitness();
        }
        System.out.println("run number: " + (runNumber + 1));
        System.out.println("overallFitness: " + overallFitness);
    }

    public static void printGenerationsMaxFitness(int generation, exerciseOne.Genome[] gene, int geneCnt) {
        System.out.println("generation " + generation + ": \n" + "max fitness ->" + gene[geneCnt - 1].getFitness() + "\n");
    }
    public static void printGenerationsMaxFitness(int generation, exerciseTwo.Genome[] gene, int geneCnt) {
        System.out.println("generation " + generation + ": \n" + "max fitness ->" + gene[geneCnt - 1].getFitness() + "\n");
    }

    public static void printSomeResults(AllOneIsFittest.AllOneIsFittestTask task) {
        System.out.println("id: " + task.getId());
        System.out.println("pm: " + task.getPm());
        System.out.println("pc: " + task.getPc());
        System.out.println("average needed generations: " + task.getGener());
        System.out.println();
    }

    public static void printSomeResults(TravellingSalesman.TravellingSalesmanTask task) {
        System.out.println("id: " + task.getId());
        System.out.println("pm: " + task.getPm());
        System.out.println("pc: " + task.getPc());
        System.out.println("average needed generations: " + task.getGener());
        System.out.println();
    }

    public static void printAllResults(int overallNeededGenerations, int numberOfRunsToAverage, double pm, double pc, int geneLen, int geneCnt, double initRate, int recombinationMethod, int replicationScheme, double acceptRate, int maxFitness) {
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

}
