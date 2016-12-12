import java.util.ArrayList;

/**
 * Created by liquidsunset on 23.11.16.
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
