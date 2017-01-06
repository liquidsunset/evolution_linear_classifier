import org.jdom2.Element;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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


    private ArrayList<DataItem> trainingData;

    private ArrayList<HyperPlane> hyperPlanes = new ArrayList<>();

    HyperPlanePhenotype(ArrayList<DataItem> trainingData, int nBases) {
        this.trainingData = trainingData;
        this.nBases = nBases;
        this.sampleCount = trainingData.size();
    }

    @Override
    public void doOntogeny(List<Chromosome> list) {

        RealChromosome chrom = (RealChromosome) list.get(0);
        ArrayList<Double> perm = (ArrayList<Double>) chrom.getBases();

        ArrayList<Double> hyperPlaneWeights[] = splitWeights(perm);
        hyperPlanes.clear();
        for (int i = 0; i < hyperPlaneWeights.length; i++) {
            hyperPlanes.add(new HyperPlane(hyperPlaneWeights[i], i));
        }

    }

    @Override
    public void calcFitness() {
        calcFitnessWithHyperPlanes();
        fitness = (double) nCorrect / (double) sampleCount;
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
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e.toString());
        }

    }

    @Override
    public String toString() {
        return hyperPlanes.stream().map(Object::toString)
                .collect(Collectors.joining(""));
    }

    private ArrayList[] splitWeights(ArrayList<Double> list) {
        int weightsPerClass = list.size() / nBases;

        ArrayList[] output = new ArrayList[nBases];

        for (int i = 0; i < nBases; i++) {
            int start = i * weightsPerClass;

            ArrayList<Double> tempList = new ArrayList<>(list.subList(
                    start, start + weightsPerClass));

            output[i] = tempList;
        }

        return output;
    }

    void calcFitnessWithHyperPlanes() {
        nCorrect = 0;
        nNotCorrect = 0;

        for (DataItem item : trainingData) {
            Double[] values = new Double[nBases];
            double actual = 0.0;
            for (HyperPlane hyperPlane : hyperPlanes) {
                values[hyperPlane.getCorrespondingClass()] = item.calcLinearDiscriminantFunction(hyperPlane.getVector());

                if (item.getItemClass() == hyperPlane.getCorrespondingClass()) {
                    actual = values[hyperPlane.getCorrespondingClass()];
                }
            }

            Arrays.sort(values);
            double maxvalue = values[nBases - 1];

            if (maxvalue == actual) {
                nCorrect++;
            }

        }
    }

    void setTrainingData(ArrayList<DataItem> trainingData) {
        this.trainingData = trainingData;
        this.sampleCount = trainingData.size();
    }
}
