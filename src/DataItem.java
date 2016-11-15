/**
 * This Class holds one DataItem Object from the dataset. So it is one line in the Set
 */
public class DataItem {

    public enum DataSet {
        IONOSPEHERE, DIGIT
    }

    private int itemClass;
    private double featureList[];

    public DataItem(int itemClass, double featureList[]) {
        this.itemClass = itemClass;
        this.featureList = featureList;
    }

    public int getItemClass() {
        return itemClass;
    }

    public double[] getFeatureList() {
        return featureList;
    }
}
