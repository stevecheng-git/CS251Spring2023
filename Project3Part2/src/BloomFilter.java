import java.util.Scanner;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

// BloomFilter class
public class BloomFilter {

    public int m;				// Number of bits
    public int k;				// Number of hash functions
    public byte[] B;			// Array of bits

    // Constructor -- create bitset and pre-calculate needed prime numbers
    public BloomFilter(int m, int k, String hashType) {
    }

    // Return whether bit b in the bit array is set
    public boolean getBit(int b) {
        return false;
    }

    // Set the specified bit in the bit array
    public void setBit(int b) {
    }

    // Hash the String str using the k'th hash function
    public int hash(String str, int k) {
        return 0;
    }

    // Insert the string into the bitset
    public void insert(String str) {
    }

    // Lookup the string in the bitset
    // Can return false-positives
    public boolean lookup(String str) {
        return false;
    }

    // Print the bitset with 1s and 0s
    public void printBits() {
        for (int i = 0; i < m; i++) {
            System.out.print(getBit(i) ? 1 : 0);
        }
        System.out.println();
    }


    // Program entry point
    public static void main(String[] args) {

        // No arguments -- start interactive mode
        if (args.length == 0) {
            // Bloom filter parameters
            int m = 40;		// Number of bits
            int k = 3;		// Number of hash functions
            String hashType = "polynomial";		// Type of hash function to use

            // Create new empty bloom filter
            BloomFilter bf = new BloomFilter(m, k, hashType);
            bf.printBits();

            // Read keyboard input line-by-line
            Scanner sc = new Scanner(System.in);
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (line.isBlank()) continue;

                // Lookup line in bitset
                if (bf.lookup(line)) {
                    System.out.println("May already contain string");

                    // If not already in bitset, insert it
                } else {
                    bf.insert(line);
                    bf.printBits();
                }
            }

            return;


        } else if (args.length == 2) {

            // TODO: Implement experiments from section 2.4.1 in the handout here

        } else if (args.length == 3) {

            // TODO: Implement experiments from section 2.4.2 in the handout here

        }
    }
}
