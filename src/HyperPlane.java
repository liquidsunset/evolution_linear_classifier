import java.util.ArrayList;

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
}
