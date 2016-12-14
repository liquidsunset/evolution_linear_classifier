import evSOLve.JEvolution.JEvolution;
import evSOLve.JEvolution.JEvolutionException;
import evSOLve.JEvolution.JEvolutionReporter;
import evSOLve.JEvolution.chromosomes.RealChromosome;

/**
 * Main Class
 */
public class LinearMachine {

    private static final int nPerClass = 1;
    private static final double percentagePerClass = 0.8;

    public static void main(String[] args) {

        DataImporter dataImporter = new DataImporter(DataImporter.DataSet.DIGIT,
                DataImporter.DataProcessing.RANDOMHALFSPLIT, nPerClass, percentagePerClass);

        JEvolution EA = JEvolution.getInstance();
        JEvolutionReporter jEvolutionReporter = (JEvolutionReporter) EA.getReporter();
        EA.setMaximization(true);

        RealChromosome chrom = new RealChromosome();

        HyperPlanePhenotype hyperPlanePhenotype = new HyperPlanePhenotype(
                dataImporter.getTrainingData(), dataImporter.getnClasses());

        try {

            chrom.setLength(dataImporter.getnClasses() * dataImporter.getnFeatures());
            chrom.setMutationRate(1.0);

            EA.addChromosome(chrom);
            EA.setPhenotype(hyperPlanePhenotype);

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

        classifier.calcFitnessWithHyperPlanes(dataImporter.getTestData());
        classifier.calcFitness();
        System.out.println(classifier.getFitness());

    }
}
