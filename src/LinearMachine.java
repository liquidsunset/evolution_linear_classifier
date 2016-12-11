import evSOLve.JEvolution.JEvolution;
import evSOLve.JEvolution.JEvolutionException;
import evSOLve.JEvolution.chromosomes.RealChromosome;

/**
 * Main Class
 */
public class LinearMachine {


    public static void main(String[] args) {

        DataImporter dataImporter = new DataImporter(DataImporter.DataSet.REDWINE);


        JEvolution EA = JEvolution.getInstance();
        EA.setMaximization(true);

        RealChromosome chrom = new RealChromosome();


        HyperPlanePhenotype hyperPlanePhenotype = new HyperPlanePhenotype(
                dataImporter.getDataHalfSplitTest(), dataImporter.getnClasses());

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

    }
}
