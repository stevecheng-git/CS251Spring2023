public class StatTest {

    public static void main(String[] args) {
        int score = 0;
        String[] answers = new String[5];
        answers[0] = "50 22.28 35 8 5.95";
        answers[1] = "30 13.30 25 3 4.89";
        answers[2] = "80 35.31 61 18 9.37";
        answers[3] = "90 42.23 52 20 8.07";
        answers[4] = "60 26.80 40 12 6.06";
        for (int i = 0; i < 5; i++) {
            String ans = Statistics.calculateStatistics("test_" + i + ".txt");
            String[] ansSplit = ans.split(" ");
            String[] ansActual = answers[i].split(" ");
            if (ansSplit.length != ansActual.length) {
                System.out.println("test_" + i + ".txt : FAIL");
                continue;
            }

            for (int j = 0; j < ansSplit.length; j++) {
                if (ansSplit[j].equals(ansActual[j])) {
                    System.out.println("test_" + i + ".txt TEST + " + j + " : pass");
                    score = score + 1;
                } else {
                    System.out.println("test_" + i + ".txt : FAIL");
                }
            }
        }

        System.out.println("Total Score: " + score + "/25");

    }

}
