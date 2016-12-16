import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import evSOLve.JEvolution.JEvolution;
import evSOLve.JEvolution.JEvolutionException;
import evSOLve.JEvolution.JEvolutionReporter;
import evSOLve.JEvolution.chromosomes.RealChromosome;

/**
 * Main Class
 */
public class LinearMachine {

    private static final int N_PER_CLASS = 100;
    private static final double PERCENT_PER_CLASS = 0.8;
    private static final boolean TWO_FOLD_STRATEGY = true;
    private static final int NUMBER_RUNS = 10;

    public static void main(String[] args) {

        ArrayList<Double> fitnessValuesTraining = new ArrayList<>();
        ArrayList<Double> fitnessValuesTest = new ArrayList<>();
        ArrayList<Double> evolutionTime = new ArrayList<>();

        long startTime = System.nanoTime();

        for (int i = 0; i < NUMBER_RUNS; i++) {

            DataImporter dataImporter = new DataImporter(DataImporter.DataSet.IONOSPEHERE,
                    DataImporter.DataProcessing.RANDOMHALFSPLIT, N_PER_CLASS, PERCENT_PER_CLASS);

            ArrayList<DataItem> trainingData = dataImporter.getTrainingData();
            ArrayList<DataItem> testData = dataImporter.getTestData();

            JEvolution EA = JEvolution.getInstance();

            JEvolutionReporter jEvolutionReporter = (JEvolutionReporter) EA.getReporter();

            EA.setMaximization(true);

            RealChromosome chrom = new RealChromosome();

            HyperPlanePhenotype hyperPlanePhenotype = new HyperPlanePhenotype(trainingData,
                    dataImporter.getnClasses());
            EA.setPhenotype(hyperPlanePhenotype);

            try {

                jEvolutionReporter.setReportLevel(1);

                chrom.setLength(dataImporter.getnClasses() * dataImporter.getnFeatures());
                chrom.setMutationRate(1.0);

                EA.addChromosome(chrom);

                EA.setPopulationSize(20, 50);
                EA.setFitnessThreshold(1.0);

                EA.setMaximalGenerations(100);

            } catch (JEvolutionException e) {
                System.out.println(e.toString());
                System.out.println("Continuing with default values.");
            }

            EA.doEvolve();

            HyperPlanePhenotype classifier = (HyperPlanePhenotype) jEvolutionReporter
                    .getBestIndividual().getPhenotype().clone();

            evolutionTime.add(EA.getEvolutionTime());
            fitnessValuesTraining.add(classifier.getFitness());

            classifier.setTrainingData(testData);
            classifier.calcFitnessWithHyperPlanes();
            classifier.calcFitness();

            fitnessValuesTest.add(classifier.getFitness());

            if (TWO_FOLD_STRATEGY) {
                EA.setPhenotype(hyperPlanePhenotype);
                EA.doEvolve();

                classifier = (HyperPlanePhenotype) jEvolutionReporter
                        .getBestIndividual().getPhenotype().clone();

                evolutionTime.add(EA.getEvolutionTime());
                fitnessValuesTraining.add(classifier.getFitness());

                classifier.setTrainingData(trainingData);
                classifier.calcFitnessWithHyperPlanes();
                classifier.calcFitness();

                fitnessValuesTest.add(classifier.getFitness());

            }


        }

        long estimatedTime = System.nanoTime() - startTime;

        System.out.println("Average Fitness Training: " + calcAverage(fitnessValuesTraining));
        System.out.println("Average Fitness Test: " + calcAverage(fitnessValuesTest));
        System.out.println("Average Evolution Time: " + calcAverage(evolutionTime) + " seconds");
        System.out.println("Time elapsed: " + TimeUnit.MILLISECONDS.convert(
                estimatedTime, TimeUnit.NANOSECONDS) / 1000.0 + " seconds");

    }

    private static double calcAverage(ArrayList<Double> list) {
        double average = 0.0;

        for (Double item : list) {
            average += item;
        }

        return average / list.size();
    }

}
