/**
 * This Class holds one DataItem Object from the dataset. So it is one line in the Set
 */
class DataItem {

    enum DataSet {
        IONOSPEHERE, DIGIT, REDWINE, WHITEWINE
    }

    private int itemClass;
    private double featureList[];

    DataItem(int itemClass, double featureList[]) {
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
