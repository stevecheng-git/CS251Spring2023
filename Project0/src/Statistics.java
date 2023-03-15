import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

/*
 * Resources and Collaborators:
 */


public class Statistics {
    private static Random gen;
    private static void init_random() {
        gen = new Random(System.currentTimeMillis());
    }


    public static String calculateStatistics(String filename) {
        try {
            BufferedReader bf = new BufferedReader(new FileReader(filename));
            String line = bf.readLine();
            String[] info = line.trim().split("\\s+");
            int numStudents = Integer.parseInt(info[0]);
            int numQuestions = Integer.parseInt(info[1]);
            int[] scores = new int[numStudents];
            for (int i = 0; i < numStudents; i++) {
                line = bf.readLine();
                info = line.trim().split("\\s+");
                int points = 0;
                for (int j = 0; j < numQuestions; j++) {
                    points = points + Integer.parseInt(info[j]);
                }
                scores[i] = points;
            }
            bf.close();
            // used to calculate mean
            int sumOfScores = 0;
            int maximum = 0;
            int minimum = 10 * numQuestions;
            int maxPossScore = 10 * numQuestions;
            for (int k = 0; k < scores.length; k++) {
                if (scores[k] > maximum) {
                    maximum = scores[k];
                }
                if (scores[k] < minimum) {
                    minimum = scores[k];
                }
                sumOfScores = sumOfScores + scores[k];
            }
            double mean = (double) sumOfScores / scores.length;
            double stanDev = 0;
            for (int l = 0; l < scores.length; l++) {
                stanDev = stanDev + Math.pow(scores[l] - mean, 2);
            }
            stanDev = Math.pow(stanDev / numStudents, 0.5);
//            System.out.print(maxPossScore);
//            System.out.print(" " + String.format("%.2f", mean));
//            System.out.print(" " + maximum);
//            System.out.print(" " + minimum);
//            System.out.print(" " + String.format("%.2f", stanDev));
            return maxPossScore + " " + String.format("%.2f", mean) + " " + maximum + " " + minimum + " " +
                    String.format("%.2f", stanDev);
        } catch (IOException e) {
            return "";
        }
    }


    public static void testData() {
        for (int k = 0; k < 10; k++) {
            init_random();
            int numbStudents = gen.nextInt(90) + 10;
            int numbQuestions = gen.nextInt(7) + 3;
            String test = numbStudents + " " + numbQuestions + "\n";
            for (int i = 0; i < numbStudents; i++) {
                for (int j = 0; j < numbQuestions; j++) {
                    int temp = gen.nextInt(10);
                    if (j == 0) {
                        test = test + temp;
                    } else {
                        test = test + " " + temp;
                    }
                }
                test = test + "\n";
            }

            try {
                FileWriter myWriter = new FileWriter("scores_" + k + ".txt");
                myWriter.write(test);
                myWriter.close();
                System.out.println("Successfully wrote to the file.");
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }

    }


    public static void main(String[] args) {
        System.out.println(calculateStatistics("C:\\Users\\jordan\\Downloads\\file_scores_1.txt"));
        System.out.println(calculateStatistics("C:\\Users\\jordan\\IdeaProjects\\CS251Spring2023\\Project0\\src\\TestFiles\\test_0.txt"));
        System.out.println(calculateStatistics("C:\\Users\\jordan\\IdeaProjects\\CS251Spring2023\\Project0\\src\\TestFiles\\test_1.txt"));
        System.out.println(calculateStatistics("C:\\Users\\jordan\\IdeaProjects\\CS251Spring2023\\Project0\\src\\TestFiles\\test_2.txt"));
        System.out.println(calculateStatistics("C:\\Users\\jordan\\IdeaProjects\\CS251Spring2023\\Project0\\src\\TestFiles\\test_3.txt"));
        System.out.println(calculateStatistics("C:\\Users\\jordan\\IdeaProjects\\CS251Spring2023\\Project0\\src\\TestFiles\\test_4.txt"));
    }


}
