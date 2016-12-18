import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Class for importing the datasets and splitting into test and training data
 */
class DataImporter {

    enum DataSet {
        IONOSPHERE, DIGIT, REDWINE, WHITEWINE, LEAF
    }

    enum DataProcessing {
        HALFSPLIT, RANDOMHALFSPLIT, N_PER_CLASS, PERCENT_PER_CLASS
    }

    private static final String IONOSPHERE_DATA_PATH = "dataset/ionosphere/ionosphere.data.txt";
    private static final String DIGIT_DATA_PATH = "dataset/digit/semeion.data.txt";
    private static final String RED_WINE_PATH = "dataset/winequality/winequality-red.csv";
    private static final String WHITE_WINE_PATH = "dataset/winequality/winequality-white.csv";
    private static final String LEAF_DATA_PATH = "dataset/leaf/leaf.csv";

    private ArrayList<DataItem> processedData = new ArrayList<>();

    private ArrayList<ArrayList<DataItem>> splitPerClass = new ArrayList<>();
    private ArrayList<DataItem> trainingData = new ArrayList<>();
    private ArrayList<DataItem> testData = new ArrayList<>();

    private int nClasses = 0;
    private int nFeatures = 0;


    DataImporter(DataSet dataSet, DataProcessing dataProcessing, int nPerClass,
                 double nPercentPerClass) {

        switch (dataSet) {
            case IONOSPHERE:
                createIonosphereData();
                break;
            case DIGIT:
                createDigitData();
                break;
            case REDWINE:
                createWineData(RED_WINE_PATH);
                break;
            case WHITEWINE:
                createWineData(WHITE_WINE_PATH);
                break;
            case LEAF:
                createLeafData(LEAF_DATA_PATH);
                break;
        }

        initClassFeatureCount();

        switch (dataProcessing) {
            case HALFSPLIT:
                splitDataHalf(processedData);
                break;
            case RANDOMHALFSPLIT:
                splitDataHalfRandom();
                break;
            case N_PER_CLASS:
                splitDataNPerClass(nPerClass);
                break;
            case PERCENT_PER_CLASS:
                splitDataPercentPerClass(nPercentPerClass);
                break;
        }

        System.out.println("Processed Datasets");
    }


    private void createIonosphereData() {
        try (
                InputStream fis = new FileInputStream(IONOSPHERE_DATA_PATH);
                InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
                BufferedReader br = new BufferedReader(isr)
        ) {
            for (String line; (line = br.readLine()) != null; ) {
                String[] splitLine = line.split(",");

                String itemClassChar = splitLine[splitLine.length - 1];
                int itemClass = "b".equals(itemClassChar) ? 0 : 1;

                double[] features = new double[splitLine.length - 1];
                for (int i = 0; i < splitLine.length - 2; i++) {
                    features[i] = Double.valueOf(splitLine[i]);
                }

                processedData.add(new DataItem(itemClass, features));

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createDigitData() {
        try (
                InputStream fis = new FileInputStream(DIGIT_DATA_PATH);
                InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
                BufferedReader br = new BufferedReader(isr)
        ) {
            for (String line; (line = br.readLine()) != null; ) {
                String[] splitLine = line.split(" ");
                String[] itemClassArray = Arrays.copyOfRange(splitLine,
                        splitLine.length - 10, splitLine.length);

                int itemClass = -1;
                for (int i = 0; i < itemClassArray.length; i++) {
                    if ("1".equals(itemClassArray[i])) {
                        itemClass = i;
                        break;
                    }
                }

                double[] features = new double[splitLine.length - 10];
                for (int i = 0; i < splitLine.length - 10; i++) {
                    features[i] = Double.valueOf(splitLine[i]);
                }

                processedData.add(new DataItem(itemClass, features));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createWineData(String filePath) {
        try (
                InputStream fis = new FileInputStream(filePath);
                InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
                BufferedReader br = new BufferedReader(isr)
        ) {
            for (String line; (line = br.readLine()) != null; ) {
                String[] splitLine = line.split(";");


                int itemClass;
                try {
                    itemClass = Integer.valueOf(splitLine[splitLine.length - 1]);
                } catch (NumberFormatException e) {
                    continue;
                }

                double[] features = new double[splitLine.length - 1];
                for (int i = 0; i < splitLine.length - 2; i++) {
                    features[i] = Double.valueOf(splitLine[i]);
                }

                processedData.add(new DataItem(itemClass, features));

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createLeafData(String filePath) {
        try (
                InputStream fis = new FileInputStream(filePath);
                InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
                BufferedReader br = new BufferedReader(isr)
        ) {
            for (String line; (line = br.readLine()) != null; ) {
                String[] splitLine = line.split(",");


                int itemClass;
                try {
                    itemClass = Integer.valueOf(splitLine[0]);
                } catch (NumberFormatException e) {
                    continue;
                }

                double[] features = new double[splitLine.length - 1];
                for (int i = 1; i < splitLine.length - 1; i++) {
                    features[i] = Double.valueOf(splitLine[i]);
                }

                processedData.add(new DataItem(itemClass, features));

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initClassFeatureCount() {
        Set<Integer> classCount = new HashSet<>();

        for (DataItem dataItem : processedData) {
            classCount.add(dataItem.getItemClass());
        }

        nClasses = classCount.size();
        nFeatures = processedData.get(0).getFeatureList().length;

        ArrayList<Integer> classArray = new ArrayList<>(classCount);
        Collections.sort(classArray);

        for (int i = 0; i < classCount.size(); i++) {
            splitPerClass.add(new ArrayList<>());
        }

        for (DataItem dataItem : processedData) {
            splitPerClass.get(classArray.indexOf(dataItem.getItemClass())).add(dataItem);
        }
    }

    private void splitDataHalf(ArrayList<DataItem> data) {
        for (int i = 0; i < data.size(); i++) {
            if (i % 2 == 0) {
                testData.add(data.get(i));
            } else {
                trainingData.add(data.get(i));
            }
        }
    }

    private void splitDataHalfRandom() {
        ArrayList<DataItem> shuffledList = new ArrayList<>(processedData);
        Collections.shuffle(shuffledList);
        splitDataHalf(shuffledList);
    }

    private void splitDataNPerClass(int numberElements) {
        for (ArrayList<DataItem> classEntry : splitPerClass) {
            ArrayList<DataItem> testData = new ArrayList<>(classEntry);
            Collections.shuffle(testData);

            ArrayList<DataItem> trainingData;

            if (testData.size() != 1 && numberElements >= testData.size()) {
                trainingData = new ArrayList<>(testData.subList(0, testData.size() - 2));
            } else {
                trainingData = new ArrayList<>(testData.subList(0, numberElements));
            }

            if (testData.size() != 1) {
                testData.removeAll(trainingData);
            }

            this.trainingData.addAll(trainingData);
            this.testData.addAll(testData);
        }
    }

    private void splitDataPercentPerClass(double percentage) {
        int length;
        for (ArrayList<DataItem> classEntry : splitPerClass) {
            length = (int) Math.floor(classEntry.size() * percentage);
            ArrayList<DataItem> testData = new ArrayList<>(classEntry);
            Collections.shuffle(testData);
            ArrayList<DataItem> trainingData = new ArrayList<>();

            if (length == 0) {
                trainingData.add(testData.get(0));
                if (testData.size() != 1) {
                    testData.removeAll(trainingData);
                }
            } else {
                trainingData.addAll(testData.subList(0, length));
                testData.removeAll(trainingData);
            }

            this.testData.addAll(testData);
            this.trainingData.addAll(trainingData);
        }
    }

    int getnClasses() {
        return nClasses;
    }

    int getnFeatures() {
        return nFeatures;
    }

    ArrayList<DataItem> getTrainingData() {
        return trainingData;
    }

    ArrayList<DataItem> getTestData() {
        return testData;
    }
}
