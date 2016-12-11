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

    public int getItemClass() {
        return itemClass;
    }

    public double[] getFeatureList() {
        return featureList;
    }

    public double calcLinearDiscriminantFunction(ArrayList<Double> hyperPlaneVector) {

        if(hyperPlaneVector.size() != featureList.length) {
            throw new IndexOutOfBoundsException("Weight size must be the same as feature size");
        }

        double calculatedWeight = 0.0;

        for(int i = 0; i < featureList.length; i++){
            calculatedWeight += featureList[i] * hyperPlaneVector.get(i);
        }

        return calculatedWeight;
    }


}
