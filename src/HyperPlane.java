import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * HyperPlane Object
 */
class HyperPlane {

    private ArrayList<Double> vector;
    private int id;
    private int[] classResponses;


    HyperPlane(ArrayList<Double> vector, int id, int classCount) {
        this.vector = vector;
        this.id = id;
        classResponses = new int[classCount];
    }

    ArrayList<Double> getVector() {
        return vector;
    }

    int getId() {
        return id;
    }

    public String toString() {
        return "Hyperplane for class " + id + ": " +
                vector.stream().map(Object::toString)
                        .collect(Collectors.joining(", ")) +
                System.lineSeparator();
    }

    void setDataItem(DataItem item) {
        classResponses[item.getMappedItemClass()]++;
    }

    int[] getClassResponses() {
        return classResponses;
    }
}
