import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Class for importing the datasets and splitting into test and training data
 */
public class DataImporter {

    private static final String IONOSPHERE_DATA_PATH = "dataset/ionosphere/ionosphere.data.txt";
    private static final String DIGIT_DATA_PATH = "dataset/digit/semeion.data.txt";

    private ArrayList<DataItem> processedData = new ArrayList<>();

    private ArrayList<DataItem> dataHalfSplitTraining = new ArrayList<>();
    private ArrayList<DataItem> dataHalfSplitTest = new ArrayList<>();

    private ArrayList<DataItem> dataRandomSplitTraining = new ArrayList<>();
    private ArrayList<DataItem> dataRandomSplitTest = new ArrayList<>();


    DataImporter(DataItem.DataSet dataSet) {
        switch (dataSet) {
            case IONOSPEHERE:
                createIonosphereData();
                break;
            case DIGIT:
                createDigitData();
                break;
        }

        splitData();
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

                double[] features = new double[splitLine.length - 1];
                for (int i = 0; i < splitLine.length - 2; i++) {
                    features[i] = Double.valueOf(splitLine[i]);
                }

                int itemClass = "b".equals(itemClassChar) ? 0 : 1;

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

    private void splitData() {
        dataHalfSplitTraining.addAll(processedData.subList(
                0, processedData.size() / 2 + processedData.size() % 2));
        dataHalfSplitTest.addAll(processedData.subList(
                processedData.size() / 2 + processedData.size() % 2, processedData.size()));

        ArrayList<DataItem> shuffledList = new ArrayList<>(processedData);
        Collections.shuffle(shuffledList);

        dataRandomSplitTraining.addAll(shuffledList.subList(
                0, shuffledList.size() / 2 + shuffledList.size() % 2));
        dataRandomSplitTest.addAll(shuffledList.subList(
                shuffledList.size() / 2 + shuffledList.size() % 2, shuffledList.size()));
    }

    public ArrayList<DataItem> getProcessedData() {
        return processedData;
    }

    public ArrayList<DataItem> getDataHalfSplitTraining() {
        return dataHalfSplitTraining;
    }

    public ArrayList<DataItem> getDataHalfSplitTest() {
        return dataHalfSplitTest;
    }

    public ArrayList<DataItem> getDataRandomSplitTraining() {
        return dataRandomSplitTraining;
    }

    public ArrayList<DataItem> getDataRandomSplitTest() {
        return dataRandomSplitTest;
    }
}
