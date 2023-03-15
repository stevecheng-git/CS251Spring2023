// COLLABORATORS: N/A
// RESOURCES: Javadocs for StringBuilder, Scanner

import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

// Starfield class
public class Starfield {

    private int numRows;
    private int numCols;
    private int numStars;

    // Star nested class
    public static class Star {
        public int row;			// Row index
        public int col;			// Column index
        public double mag;		// Apparent magnitude of the star

        // Links to nearest star in each direction, or null if none
        public Star left;
        public Star right;
        public Star up;
        public Star down;

        // Star constructor
        public Star(int row, int col, double mag) {
            this.row = row;
            this.col = col;
            this.mag = mag;
        }
    }

    // Arrays of links to stars in rows and columns of matrix
    public Star[] RA;
    public Star[] CA;


    // Starfield constructors
    // Create an empty starfield with specified size
    public Starfield(int rows, int cols) {
        RA = new Star[rows];
        CA = new Star[cols];
        numRows = rows;
        numCols = cols;
        numStars = 0;
    }

    // Read starfield from an input stream
    public Starfield(InputStream istr) {
        Scanner scan = new Scanner(istr);
        numRows = scan.nextInt();
        numCols = scan.nextInt();
        numStars = scan.nextInt();
        scan.nextLine();
        RA = new Star[numRows];
        CA = new Star[numCols];
        Star[] FA = new Star[numCols];
        Star[] FARows = new Star[numRows];


        for (int i = 0; i < numStars; i++) {
            Star star = new Star(scan.nextInt(), scan.nextInt(), scan.nextDouble());
            if (RA[star.row] == null) {
                RA[star.row] = star;
            } else {
                FARows[star.row].right = star;
                star.left = FARows[star.row];
            }
            FARows[star.row] = star;
            if (CA[star.col] == null) {
                CA[star.col] = star;
            } else {
                FA[star.col].down = star;
                star.up = FA[star.col];
            }
            FA[star.col] = star;
//            RA[star.row] = star;
//            CA[star.col] = star;

            scan.nextLine();
        }
    }

    // Read starfield from a file (do not modify)
    public Starfield(String filename) throws FileNotFoundException {
        this(new FileInputStream(filename));	// Calls the constructor above
    }


    // Starfield properties
    public int numRows() {
        return numRows;
    }
    public int numCols() {
        return numCols;
    }
    public int numStarsInRow(int r) {
        int count = 0;
        for (Star star = RA[r]; star != null; star = star.right) {
            count++;
        }
        return count;
    }
    public int numStarsInCol(int c) {
        int count = 0;
        for (Star star = CA[c]; star != null; star = star.down) {
            count++;
        }
        return count;
    }
    public float density() {
        return Float.parseFloat(String.format("%.5f", (float) numStars / (numRows * numCols)));
    }

    // Return a string representation of the starfield
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                double mag = getMagnitude(i, j);
                if (mag == Double.POSITIVE_INFINITY || mag >= 7.0d) {
                    stringBuilder.append((char) 0x20);
//                    stringBuilder.append(' ');
                } else if (mag >= 6.0d) {
                    stringBuilder.append((char) 0x60);
//                    stringBuilder.append('`');
                } else if (mag >= 5.5d) {
                    stringBuilder.append((char) 0x2E);
//                    stringBuilder.append('.');
                } else if (mag >= 5.0d) {
                    stringBuilder.append((char) 0x2C);
//                    stringBuilder.append(',');
                } else if (mag >= 4.0d) {
                    stringBuilder.append((char) 0x27);
//                    stringBuilder.append('‘');
                } else if (mag >= 3.0d) {
                    stringBuilder.append((char) 0x22);
//                    stringBuilder.append('“');
                } else if (mag >= 2.0d) {
                    stringBuilder.append((char) 0x2A);
//                    stringBuilder.append('*');
                } else if (mag >= 1.0d) {
                     stringBuilder.append((char) 0x6F);
//                    stringBuilder.append('o');
                } else {
//             stringBuilder.append((char) 0x40);
                    stringBuilder.append('@');
                }
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    public String toString2() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < numRows; i++) {
            // if padding to the end of line is needed, use numStarsInRow(i)
            // otherwise, numStarsInRow = cols
            int numStarsInRow = numStarsInRow(i);
            int n = 0;
            for (int j = 0; j < numCols && n < numStarsInRow; j++) {
                double magnitude = getMagnitude(i, j);
                if (magnitude == Double.POSITIVE_INFINITY || magnitude >= 7.0d) {
                    stringBuilder.append((char) 0x20);
//                    stringBuilder.append(' ');
                } else if (magnitude >= 6.0d) {
                    stringBuilder.append((char) 0x60);
//                    stringBuilder.append('`');
                    n++;
                } else if (magnitude >= 5.5d) {
                    stringBuilder.append((char) 0x2E);
//                    stringBuilder.append('.');
                    n++;
                } else if (magnitude >= 5.0d) {
                    stringBuilder.append((char) 0x2C);
//                    stringBuilder.append(',');
                    n++;
                } else if (magnitude >= 4.0d) {
                    stringBuilder.append((char) 0x27);
//             stringBuilder.append('‘');
                    n++;
                } else if (magnitude >= 3.0d) {
                    stringBuilder.append((char) 0x22);
//             stringBuilder.append('“');
                    n++;
                } else if (magnitude >= 2.0d) {
                    stringBuilder.append((char) 0x2A);
//                    stringBuilder.append('*');
                    n++;
                } else if (magnitude >= 1.0d) {
                    stringBuilder.append((char) 0x6F);
//                    stringBuilder.append('o');
                    n++;
                } else {
                    stringBuilder.append((char) 0x40);
//                    stringBuilder.append('@');
                    n++;
                }
            }
//       stringBuilder.append('\r'); // this is extra in the ascii file
            stringBuilder.append('\n');
        }
        return stringBuilder.toString();
    }


    // Retrieve the magnitude of the star at row r and column c
    public double getMagnitude(int r, int c) {

        for (Star star = RA[r]; star != null; star = star.right) {
            if (star.col == c) {
                return star.mag;
            }
        }
        return Double.POSITIVE_INFINITY;
    }

    public double getMagnitude2(int r, int c) {

        Star star = RA[r];
        while (star != null) {
            if (star.col == c) {
                return star.mag;
            }
            star = star.right;
        }
        return Double.POSITIVE_INFINITY;
    }

    // check RA[r]
    // row 0 use case
    /// if RA[r] = nothing, then empty row use case
    // row 1 use case (insert left of first star)
    /// else
    //// if c < RA[r].col
    ///// then insert new star at r, c, create link with RA[r], reassign new star as RA[r]
    // Set the magnitude at row r and column c, creating a Star if necessary

    // worst case insertion is if star is inserted at the furthest right and furthest down position
    // only matters if there exists a row that is fully populated and furthest right column is fully populated
    public void setMagnitude(int r, int c, double mag) {
        Star star = new Star(r, c, mag);
        Star prev = null;
        boolean newStarAdded = true;
        if (RA[r] == null) {
            RA[r] = star;
        } else {
            for (Star star2 = RA[r]; star2 != null; star2 = star2.right) {
                if (c < RA[r].col) {
                    RA[r] = star;
                    star.right = star2;
                    star2.left = star;
                    break;
                } else if (c < star2.col) {
                    prev.right = star;
                    star.left = prev;
                    star.right = star2;
                    star2.left = star;
                    break;
                } else if (c == star2.col) {
                    star2.mag = mag;
                    newStarAdded = false;
                    break;
                } else if (c > star2.col && star2.right == null) {
                    star2.right = star;
                    star.left = star2;
                    break;
                }
                prev = star2;
            }
        }
        prev = null;
        if (CA[c] == null) {
            CA[c] = star;
        } else {
            for (Star star2 = CA[c]; star2 != null; star2 = star2.down) {
                if (r < CA[c].row) {
                    CA[c] = star;
                    star.down = star2;
                    star2.up = star;
                    break;
                } else if (r < star2.row) {
                    prev.down = star;
                    star.up = prev;
                    star.down = star2;
                    star2.up = star;
                    break;
                } else if (r == star2.row) {
                    star2.mag = mag;
                    newStarAdded = false;
                    break;
                } else if (r > star2.row && star2.down == null) {
                    star2.down = star;
                    star.up = star2;
                    break;
                }
                prev = star2;
            }
        }
        if (newStarAdded) {
            numStars++;
        }
    }

    // Remove the star at row r and column c
    public void unsetMagnitude(int r, int c) {
        Star prev = null;
        boolean removedStar = true;
        for (Star star = RA[r]; star != null; star = star.right) {
            // first star in row removed
            if (c == RA[r].col) {
                RA[r] = star.right;
                star.right = null;
                RA[r].left = null;
                break;
            } else if (c == star.col) {
                // star that is not first star in row removed but not last star
                if (star.right != null) {
                    star.right.left = prev;
                    prev.right = star.right;
                    break;
                    // last star in row removed
                } else {
                    prev.right = null;
                    star.left = null;
                    break;
                }
                // cannot find star in the row
            } else if (c < star.col) {
                removedStar = false;
                break;
            }
            prev = star;
        }
        prev = null;
        for (Star star = CA[c]; star != null; star = star.down) {
            if (r == CA[c].row) {
                CA[c] = star.down;
                star.down = null;
                CA[c].up = null;
                break;
            } else if (r == star.row) {
                if (star.down != null) {
                    star.down.up = prev;
                    prev.down = star.down;
                    break;
                } else {
                    prev.down = null;
                    star.down = null;
                    break;
                }
                // cannot find star in the column
            } else if (r < star.row) {
                removedStar = false;
                break;
            }
            prev = star;
        }

        if (removedStar) {
            numStars--;
        }
    }

    // swap rows and columns
    // swap left with up, right with down and vice versa
    // Transpose the starfield in-place
    public void transpose2() {
        for (int i = 0; i < numRows; i++) {
            for (Star star = RA[i]; star != null; star = star.down) {
                int temp = star.row;
                star.row = star.col;
                star.col = temp;
                Star starRight = star.right;
                Star starDown = star.down;
                Star starUp = star.up;
                Star starLeft = star.left;
                star.right = starDown;
                star.down = starRight;
                star.left = starUp;
                star.up = starLeft;
            }
        }

        Star[] temp = RA;
        RA = CA;
        CA = temp;
    }

    public void transpose() {
        for (int i = 0; i < numRows; i++) {
            Star starLeft;
            Star starRight;
            Star starUp;
            Star starDown;

            for (Star star = RA[i]; star != null; star = star.down) {
                int temp = star.row;
                star.row = star.col;
                star.col = temp;

                starLeft = star.left;
                starRight = star.right;
                starUp = star.up;
                starDown = star.down;

                star.right = starDown;
                star.down = starRight;
                star.left = starUp;
                star.up = starLeft;
            }
        }

        Star[] oldRA = RA;
        RA = CA;
        CA = oldRA;

        int temp = numRows;
        numRows = numCols;
        numCols = temp;
    }



    // Multiply starfield by dense vector
    public double[] multiplyVector(double[] vector) {
//        for (int i = 0; i < vector.length; i++) {
//            for (int j = 0; j < numRows; j++) {
//
//            }
//        }
        double[] multipliedStarfield = new double[numRows];
        for (int i = 0; i < numRows; i++) {
            double sum = 0;
            for (int j = 0; j < vector.length; j++) {
                double magnitude = getMagnitude(i, j);
                if (magnitude == Double.POSITIVE_INFINITY) {
                    magnitude = 0;
                }
                sum = sum + (magnitude * vector[j]);
            }
            multipliedStarfield[i] = sum;
        }
        return multipliedStarfield;
    }

    // Combine two starfields and return the result
    public static Starfield combine(Starfield S, Starfield T) {
        Starfield W = new Starfield(S.numRows, S.numCols);
        Star[] FA = new Star[S.numRows];
        Star[] FA2 = new Star[S.numCols];
        for (int i = 0; i < S.numRows; i++) {
            Star s = S.RA[i];
            Star t = T.RA[i];
            while (s != null || t != null) {
                // Find the minimum/first value of the 2 arrays
                int si = s == null ? Integer.MAX_VALUE : s.col;
                int ti = t == null ? Integer.MAX_VALUE : t.col;
                int j = Math.min(si, ti);
                Star star = null;

                // does star exist in both starfields?
                if (si == ti) {
                    double bothMagnitudes = -2.5 * Math.log10(Math.pow(10, -s.mag * 0.4) + Math.pow(10, -t.mag * 0.4));
                    star = new Star(i, si, bothMagnitudes);
                    W.setCombinedMagnitude(i, si, star, FA, FA2);
                    s = s.right;
                    t = t.right;
                } else if (si == j) {
                    // add the s star to W
                    star = new Star(s.row, s.col, s.mag);
                    W.setCombinedMagnitude(s.row, s.col, star, FA, FA2);
                    s = s.right;
                } else if (ti == j) {
                    // add the t star to W
                    star = new Star(t.row, t.col, t.mag);
                    W.setCombinedMagnitude(t.row, t.col, star, FA, FA2);
                    t = t.right;
                } else {
                    // no more elements in this row
                    break;
                }
            }
        }
        return W;

    }

    public static Starfield combine2(Starfield S, Starfield T) {
        Starfield W = new Starfield(S.numRows, S.numCols);
        for (int i = 0; i < S.numRows; i++) {
            Star SRows = S.RA[i];
            Star TRows = T.RA[i];
            Star WRows = W.RA[i];
            // sort by column
            if (SRows != null && TRows != null) {
                if (SRows.col < TRows.col) {
                    WRows = SRows;
                } else if (SRows.col > TRows.col) {
                    WRows = TRows;
                } else {
                    //perform combine magnitude formula
                }
            }
        }
        return W;
    }

    public void setCombinedMagnitude(int r, int c, Star newStar, Star[] FA, Star[] FA2) {
        if (RA[r] == null) {
            RA[r] = newStar;
            FA2[r] = newStar;
        } else {
            Star prev = FA2[r];
            prev.right = newStar;
            newStar.left = prev;
            FA2[r] = newStar;
        }

        if (CA[c] == null) {
            CA[c] = newStar;
            FA[c] = newStar;
        } else {
            Star prev = FA[c];
            prev.down = newStar;
            newStar.up = prev;
            FA[c] = newStar;
        }
        numStars++;
    }




    public static void main(String[] args) {
        Starfield s = null;

        if (args.length > 0) {
            try {
                s = new Starfield(args[0]);
//                s.setMagnitude(0, 2, 5);
//                s.unsetMagnitude(0, 15);
//                Starfield sf = new Starfield(2, 2);
//                sf.setMagnitude(0, 0, 1);
//                sf.setMagnitude(0, 1, 2);
//                sf.setMagnitude(1, 0, 3);
//                sf.setMagnitude(1, 1, 4);
//                Starfield sf2 = new Starfield(3, 2);
//                sf2.setMagnitude(0, 0, 1);
//                sf2.setMagnitude(1, 0, 3);
//                sf2.setMagnitude(2, 0, 5);
//                sf2.setMagnitude(0, 1, 2);
//                sf2.setMagnitude(1, 1, 4);
//                sf2.setMagnitude(2, 1, 6);
//                Starfield sf3 = new Starfield(2, 2);
//                sf3.setMagnitude(0, 0, 1);
//                sf3.setMagnitude(0, 1, 2);
//                Starfield sf4 = new Starfield(3, 2);
//                sf4.setMagnitude(1, 0, 3);
//                sf4.setMagnitude(1, 1, 4);
//                Starfield sf5 = combine(sf3, sf4);
//                String starfield1 = s.toString();
//                s.transpose();
//                s.transpose();
//                String starfield2 = s.toString();
//                if (starfield1.equals(starfield2)) {
//                    System.out.println("equals!");
//                } else {
//                    System.out.println("not equals!");
//                }
//                System.out.println(sf2);
//                double[] result = sf.multiplyVector(new double[]{1, 2});
//                System.out.println(result);
//                s.setMagnitude(0, 60, 5);
//                s.setMagnitude(0, 10, 4);
//                s.setMagnitude(0, 20, 6);

//                System.out.println(s.numStarsInRow(0));
//                System.out.println(s.numStarsInRow(1));
//                System.out.println(s.numStarsInCol(15));
//                System.out.println(s.getMagnitude(9, 15));


            } catch (FileNotFoundException e) {
                System.out.println();
                return;
            }
        } else {
            s = new Starfield(System.in);
        }

        System.out.print(s.toString());
    }
}
