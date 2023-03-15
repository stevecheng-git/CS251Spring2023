import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.FileReader;

public class StarfieldTest {
    /*
     *  Part 1
     *    - These test cases are very trivial and do not Check the constructor and the getMagnitude functions.
     *    - They are an indicator of if your implementation is along the right directions or not
     *    - A correct toString method will have certain implications which will help you test your functions
     *    - You are encouraged to develop your own test cases.
     */

    public static void main(String[] args) throws FileNotFoundException {
        int totalScore = 0;

        // make the two starfields.
        String[] testFiles = {"aquarius.txt", "aries.txt", "cancer.txt", "capricornus.txt"};
        String[] ansFiles = {"aquarius_ascii.txt", "aries_ascii.txt", "cancer_ascii.txt", "capricornus_ascii.txt"};
        int[] numRows = {23, 32, 38, 30, 45};
        int[][] rowsNumStars = {
                {2, 3, 3, 2, 1, 0, 2, 2, 1, 3, 5, 2, 0, 3, 3, 2, 3, 2, 4, 4, 2, 1, 1},
                {1, 2, 3, 3, 0, 5, 2, 1, 3, 3, 2, 2, 1, 2, 1, 5, 3, 2, 3, 0, 3, 2, 1, 0, 0, 3, 0, 0, 2, 2, 1, 1},
                {1, 2, 3, 0, 2, 0, 2, 2, 3, 1, 1, 1, 1, 2, 1, 1, 1, 2, 1, 2, 0, 0, 3, 2, 2, 2, 2, 1, 0, 1, 3, 2, 1, 1, 2, 1, 0, 2},
                {0, 4, 4, 0, 0, 2, 3, 2, 1, 2, 4, 0, 4, 2, 3, 3, 2, 2, 2, 3, 3, 1, 2, 0, 0, 2, 1, 2, 2, 1}};
        double[] densities = {0.02772, 0.02305, 0.02368, 0.02375};


        // test for first file - NO TEST FOR CONSTRUCTOR - TEST YOURSELF WITH TOSTRING
        // testing toString()
        for (int i = 0; i < 4; i++) {
            System.out.println(testFiles[i] + ":");
            String ans = "";
            Starfield solution = new Starfield("./inputs/" + testFiles[i]);
            String answer = readFile("./outputs/" + ansFiles[i]);
            if (answer.equals(solution.toString())) {
                System.out.println("    toString: pass");
                totalScore += 4;
            } else {
                System.out.println("    toString: FAIL");
            }
            if (numRows[i] == solution.numRows()) {
                System.out.println("    numRows(): pass");
                boolean flag = true;
                for (int j = 0; j < solution.numRows(); j++) {
                    if (rowsNumStars[i][j] != solution.numStarsInRow(j)) {
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    totalScore += 3;
                    System.out.println("    numStarsInRow(): pass");
                } else {
                    System.out.println("    numStarsInRow(): FAIL");
                }
            } else {
                System.out.println("    numRows(): FAIL");
                System.out.println("    numStarsInRow() NOT CHECKED");
            }

            if (Math.abs(densities[i] - solution.density()) < 0.0001) {
                totalScore += 3;
                System.out.println("    density(): pass");
            } else {
                System.out.println("    density(): FAIL");
            }



        }


        System.out.println("\nTotal Score: " + totalScore + "/40");
    }

    private static String readFile(String s) {
        String answer = "";
        String line = "";

        try (BufferedReader br = new BufferedReader(new FileReader(s))) {
            while ((line = br.readLine()) != null) {
                answer = answer + line + '\n';
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

//        System.out.println(answer);
//        System.out.println("*********************************************************");
        return answer;


    }


}
