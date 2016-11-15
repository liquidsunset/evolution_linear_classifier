import org.jdom2.Element;

import java.util.List;

import evSOLve.JEvolution.Phenotype;
import evSOLve.JEvolution.chromosomes.Chromosome;

/**
 * Phenotype for our Linear Machine problem
 */
public class LinearMachinePhenotype implements Phenotype {
    @Override
    public void doOntogeny(List<Chromosome> list) {

    }

    @Override
    public void calcFitness() {

    }

    @Override
    public double getFitness() {
        return 0;
    }

    @Override
    public void toXml(Element element) {

    }

    @Override
    public Object clone() {
        return null;
    }
}
