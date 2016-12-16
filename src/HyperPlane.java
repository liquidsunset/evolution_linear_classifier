import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * HyperPlane Object
 */
class HyperPlane {

    private ArrayList<Double> vector;
    private int correspondingClass;


    HyperPlane(ArrayList<Double> vector, int correspondingClass) {
        this.vector = vector;
        this.correspondingClass = correspondingClass;
    }

    ArrayList<Double> getVector() {
        return vector;
    }

    int getCorrespondingClass() {
        return correspondingClass;
    }

    public String toString() {
        return "Hyperplane for class " + correspondingClass + ": " +
                vector.stream().map(Object::toString)
                        .collect(Collectors.joining(", ")) +
                System.lineSeparator();
    }
}
