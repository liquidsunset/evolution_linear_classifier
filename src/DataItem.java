import java.util.ArrayList;

/**
 * This Class holds one DataItem Object from the dataset. So it is one line in the Set
 */
class DataItem {


    private int itemClass;
    private double featureList[];

    DataItem(int itemClass, double featureList[]) {
        this.itemClass = itemClass;
        this.featureList = featureList;
    }

    int getItemClass() {
        return itemClass;
    }

    double[] getFeatureList() {
        return featureList;
    }

    double calcLinearDiscriminantFunction(ArrayList<Double> hyperPlaneVector) {

        if (hyperPlaneVector.size() != featureList.length) {
            throw new IndexOutOfBoundsException("Weight size must be the same as feature size");
        }

        double calculatedWeight = 0.0;

        for (int i = 0; i < featureList.length; i++) {
            calculatedWeight += featureList[i] * hyperPlaneVector.get(i);
        }

        return calculatedWeight;
    }


}
