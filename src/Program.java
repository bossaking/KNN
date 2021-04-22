import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Program {


    private float[][] war; //tablica wartości atrybutów warunkowych
    private String[] dec; //tablica wartości atrybutu decyzyjnego
    private String[] fullDec;

    private float[][] ts; //tablica wartości obiektów testowych

    private double[] odl; //tablica odległości

    private Date startTime;

    public static void main(String[] args) throws IOException {

        Program program = new Program();
        program.start();
    }

    private void start() throws IOException {

        startTime = new Date();

        List<String[]> trainingData = readFile("./iris_trening.csv");

        printDuration();

        splitTrainingDataToArrays(trainingData);

        printDuration();

        List<String[]> testData =readFile("./iris_test.csv");

        printDuration();

        splitTestDataToArray(testData);

        printDuration();

        for (float[] t : ts) {
            odl = new double[war.length];
            System.out.println("Wynik: " + startAlgorithm(t));
        }
    }

    private List<String[]> readFile(String path) throws IOException {

        BufferedReader csvReader = new BufferedReader(new FileReader(path));
        String row;
        List<String[]> data = new ArrayList<>();
        while ((row = csvReader.readLine()) != null) {
            String[] rowData = row.split(",");
            data.add(rowData);
        }
        csvReader.close();
        return data;
    }

    private void splitTrainingDataToArrays(List<String[]> data) {

        int rows = data.size();
        int columns = data.get(0).length - 1;
        war = new float[rows][columns];
        dec = new String[rows];
        fullDec = new String[rows];

        for (int i = 0; i < data.size(); i++) {
            for (int j = 0; j < data.get(i).length; j++) {

                if (j == data.get(i).length - 1) {
                    fullDec[i] = data.get(i)[j].split("-")[1];
                    continue;
                }

                war[i][j] = Float.parseFloat(data.get(i)[j]);

            }
        }
    }

    private void splitTestDataToArray(List<String[]> data) {

        int rows = data.size();
        int columns = data.get(0).length;
        ts = new float[rows][columns];

        for (int i = 0; i < data.size(); i++) {
            for (int j = 0; j < data.get(i).length; j++) {
                ts[i][j] = Float.parseFloat(data.get(i)[j]);
            }
        }
    }

    private String startAlgorithm(float[] test){


        for(int i = 0; i < war.length; i++){
            odl[i] = m(war[i], test);
        }

        printDuration();

        System.arraycopy(fullDec, 0, dec, 0, fullDec.length);

        sort();

        printDuration();

        //liczba najbliższych sąsiadów
        int k = 5;
        String[] strk = new String[k];

        System.arraycopy(dec, 0, strk, 0, k);

        int[] intk = new int[k];

        for(int i = 0; i < k; i++){
            for(int j = i; j < k; j++){
                if(strk[i].equals(strk[j])){
                    intk[i]++;
                }
            }
        }

        printDuration();

        int maxValue = 0;
        int index = 0;
        for(int i = 0; i < k; i++){
            if(intk[i] > maxValue){
                maxValue = intk[i];
                index = i;
            }
        }

        printDuration();

        return strk[index];
    }

    private double m(float[] x, float[] y){

        double sum = 0;

        for(int i = 0; i < x.length; i++){
            sum += Math.pow(x[i] - y[i], 2);
        }

        return Math.sqrt(sum);
    }

    //Sortowanie proste, dla dużych danych wejściowych trzeba użyć innego algorytmu sortowania
    private void sort(){

        boolean sorted = false;
        double temp;
        String decTemp;

        while(!sorted){
            sorted = true;

            for(int i = 0; i < odl.length - 1; i++){

                if(odl[i] > odl[i + 1]){
                    temp = odl[i];
                    decTemp = dec[i];

                    odl[i] = odl[i + 1];
                    dec[i] = dec[i + 1];

                    odl[i + 1] = temp;
                    dec[i + 1] = decTemp;

                    sorted = false;
                }

            }

        }

    }

    private void printDuration(){
        Date endTime = new Date();
        long lastDuration = endTime.getTime() - startTime.getTime();
        startTime = new Date();
        System.out.println("Czas, spędzony na przejście kolejnego kroku: " + TimeUnit.MILLISECONDS.toNanos(lastDuration) + " ns");
    }

}
