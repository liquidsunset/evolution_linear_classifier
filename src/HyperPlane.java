import java.util.ArrayList;

/**
 * Created by liquidsunset on 23.11.16.
 */
public class HyperPlane {

    private ArrayList<Double> vector;
    private int correspondingClass;


    public HyperPlane(ArrayList<Double> vector, int correspondingClass) {
        this.vector = vector;
        this.correspondingClass = correspondingClass;
    }

    public ArrayList<Double> getVector() {
        return vector;
    }

    public int getCorrespondingClass() {
        return correspondingClass;
    }
}
