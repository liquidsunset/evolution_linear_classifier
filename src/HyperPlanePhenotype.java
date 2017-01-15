import org.jdom2.Element;

import java.util.ArrayList;
import java.util.Arrays;
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
            hyperPlanes.add(new HyperPlane(hyperPlaneWeights[i], i, nBases));
        }

    }

    @Override
    public void calcFitness() {
        calcFitnessWithAssignedHyperPlanes();
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

    void calcFitnessWithAssignedHyperPlanes() {
        nCorrect = 0;
        nNotCorrect = 0;

        for (DataItem item : trainingData) {
            Double[] values = new Double[nBases];
            double actual = 0.0;
            for (HyperPlane hyperPlane : hyperPlanes) {
                values[hyperPlane.getId()] = item.calcLDF(hyperPlane.getVector());

                if (item.getMappedItemClass() == hyperPlane.getId()) {
                    actual = values[hyperPlane.getId()];
                }
            }

            Arrays.sort(values);
            double maxvalue = values[nBases - 1];

            if (maxvalue == actual) {
                nCorrect++;
            }
        }
    }

    void calcFitnessWithHyperPlanes() {

        int[] itemsPerClass = new int[nBases];
        HyperPlane responseHyperPlane = null;
        for (DataItem item : trainingData) {
            itemsPerClass[item.getMappedItemClass()]++;
            Double oldResponse = null;
            for (HyperPlane hyperPlane : hyperPlanes) {
                double response = item.calcLDF(hyperPlane.getVector());

                if (oldResponse == null) {
                    oldResponse = response;
                    responseHyperPlane = hyperPlane;
                } else {
                    if (oldResponse < response) {
                        responseHyperPlane = hyperPlane;
                    }
                }
            }

            if (responseHyperPlane != null) {
                responseHyperPlane.setDataItem(item);
            }
        }
    }

    void setTrainingData(ArrayList<DataItem> trainingData) {
        this.trainingData = trainingData;
        this.sampleCount = trainingData.size();
    }

    private void assignHyperPlanes() {
        //Todo: Implement an awesome function for assigning the hyperplances to the class-id
    }
}
