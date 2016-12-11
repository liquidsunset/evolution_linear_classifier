import org.jdom2.Element;

import java.util.ArrayList;
import java.util.List;

import evSOLve.JEvolution.Phenotype;
import evSOLve.JEvolution.chromosomes.Chromosome;
import evSOLve.JEvolution.chromosomes.RealChromosome;

/**
 * Phenotype for our Linear Machine problem
 */
public class HyperPlanePhenotype implements Phenotype {

    private double fitness;
    private int nBases;
    private int nCorrect;
    private int nNotCorrect;
    private int sampleCount;


    private ArrayList<DataItem> testData;

    private ArrayList<HyperPlane> hyperPlanes = new ArrayList<>();

    public HyperPlanePhenotype(ArrayList<DataItem> testData, int nBases) {
        this.testData = testData;
        this.nBases = nBases;
        this.sampleCount = nBases * testData.size();
    }

    @Override
    public void doOntogeny(List<Chromosome> list) {

        RealChromosome chrom = (RealChromosome) list.get(0);
        ArrayList<Double> perm = (ArrayList<Double>) chrom.getBases();

        ArrayList<Double>[] hyperPlaneWeights = splitWeights(perm);
        hyperPlanes.clear();
        for (int i = 0; i < hyperPlaneWeights.length; i++) {
            hyperPlanes.add(new HyperPlane(hyperPlaneWeights[i], i));
        }

    }

    @Override
    public void calcFitness() {
        calcFitnessWithHyperPlanes();
        fitness = (double)nNotCorrect / (double)sampleCount;
    }

    @Override
    public double getFitness() {
        return fitness;
    }

    @Override
    public void toXml(Element element) {

    }

    @Override
    public Object clone() {
        try {
            HyperPlanePhenotype clone = (HyperPlanePhenotype) super.clone();
            return (clone);
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e.toString());
        }

    }

    private ArrayList<Double>[] splitWeights(ArrayList<Double> list) {
        int weightsPerClass = list.size() / nBases;

        ArrayList<Double>[] output = new ArrayList[nBases];

        for (int i = 0; i < nBases; i++) {
            int start = i * weightsPerClass;

            ArrayList<Double> tempList = new ArrayList<>(list.subList(
                    start, start + weightsPerClass));

            output[i] = tempList;
        }

        return output;
    }

    private void calcFitnessWithHyperPlanes() {
        nCorrect = 0;
        nNotCorrect = 0;

        for(int i = 0; i < hyperPlanes.size(); i++) {
            int actualClass = hyperPlanes.get(i).getCorrespondingClass();

            for(DataItem item : testData) {
                Double calculatedWeight = item.calcLinearDiscriminantFunction(
                        hyperPlanes.get(i).getVector());

                if(item.getItemClass() == actualClass) {
                    if(calculatedWeight > 0.0) {
                        nCorrect++;
                    } else {
                        nNotCorrect++;
                    }
                } else {
                    if(calculatedWeight < 0.0) {
                        nCorrect++;
                    } else {
                        nNotCorrect++;
                    }
                }

            }

        }
    }


}
