import java.util.Scanner;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

// HashTable class -- implements a hash table with linear probing
public class HashTable {

	public String[] table; // Table of inserted elements
	public String hashType;
	public int size;
	public int capacity;
	public int numCollisions;

	// Constructor -- allocate the table
	public HashTable(int capacity, String hashType) {
		table = new String[capacity];
		this.capacity = capacity;
		this.hashType = hashType;
	}

	// update with insertion
	public int size() {
		return size;
	}

	// update with insertion
	public int numCollisions() {
		return numCollisions;
	}

	public int capacity() {
		return capacity;
	}

	// Compute the ratio of inserted elements to capacity
	public double loadFactor() {
		return (double) size / capacity;
	}

	// Compute the primary hash of the given string
	private int hash1(String str) {
		if (hashType.startsWith("crc32")) {
			return Integer.remainderUnsigned(Hasher.crc32(str), capacity);
		} else if (hashType.startsWith("adler32")) {
			return Integer.remainderUnsigned(Hasher.adler32(str), capacity);
		} else if (hashType.startsWith("murmur3")) {
			return Integer.remainderUnsigned(Hasher.murmur3_32(str, 0), capacity);
		} else if (hashType.startsWith("poly")) {
			return Integer.remainderUnsigned(Hasher.polynomial(str, 0), capacity);
		}
		return 0;
	}

	// contingent upon "dh"
	// Compute the secondary hash of the given string
	private int hash2(String str) {
		if (hashType.startsWith("crc32")) {
			return Integer.remainderUnsigned(Hasher.adler32(str), capacity);
		} else if (hashType.startsWith("adler32")) {
			return Integer.remainderUnsigned(Hasher.crc32(str), capacity);
		} else if (hashType.startsWith("murmur3")) {
			return Integer.remainderUnsigned(Hasher.murmur3_32(str, 1), capacity);
		} else if (hashType.startsWith("poly")) {
			return Integer.remainderUnsigned(Hasher.polynomial(str, 1), capacity);
		}
		return 0;
	}

	// Insert a string into the hash table and return whether successful
	public boolean insert(String str) {
		this.numCollisions = 0;
		if (size >= capacity) return false;

		boolean isLp = hashType.endsWith("-lp");
		int iterations = 0;
		int hash1 = hash1(str);
		boolean inserted = false;
		if (isLp) {
			int index = Integer.remainderUnsigned(hash1, capacity);
			do {
				if (this.table[index] == null) {
					this.table[index] = str;
					inserted = true;
					size++;
				} else {
					if (str.equals(this.table[index])) {
						break;
					}
					index++;
					index = index % capacity;
					numCollisions++;
				}
				iterations++;
			} while (iterations < capacity && !inserted);

		} else {
			int hash2 = hash2(str);
			do {
				int index = Integer.remainderUnsigned(hash1 + iterations*hash2, capacity);
				if (this.table[index] == null) {
					this.table[index] = str;
					inserted = true;
					size++;
				} else {
					numCollisions++;
					if (str.equals(this.table[index])) {
						break;
					}
				}
				iterations++;
			} while (iterations < capacity && !inserted);
		}

		return inserted;
	}

	// Return whether the table contains the string
	public boolean contains(String str) {

		boolean isLp = hashType.endsWith("-lp");
		int iterations = 0;
		int hash1 = hash1(str);
		if (isLp) {
			int index = Integer.remainderUnsigned(hash1, capacity);
			do {
				if (str.equals(this.table[index])) {
					return true;
				}
				index++;
				index = index % capacity;
				iterations++;
			} while (iterations < capacity);

		} else {
			int hash2 = hash2(str);
			do {
				int index = Integer.remainderUnsigned(hash1 + iterations*hash2, capacity);
				if (str.equals(this.table[index])) {
					return true;
				}
				iterations++;
			} while (iterations < capacity);
		}

		return false;
	}


	public static void main(String[] args) {
		// Program arguments
		int m;				// Table capacity
		String inputFile;	// Input file

		try {
			// Read arguments
			inputFile = args[0];
			m = Integer.parseInt(args[1]);
//			HashTable ht = new HashTable(3, "crc32-dh");
//			ht.insert("bob");
//			ht.insert("billy");
//			ht.insert("joe");
//			ht.insert("joseph");
//			System.out.println(Integer.remainderUnsigned(4, 2));
//			System.out.println(Integer.remainderUnsigned(5, 3));
			if (m < 1) throw new IllegalArgumentException("M must be positive");

		} catch (RuntimeException e) {
			System.out.println(e.getMessage());

			// Print usage and exit
			System.out.println("Usage: java HashTable INPUT M");
			System.out.println();
			System.out.println("ARGS:");
			System.out.println("  INPUT           Name of input file to read from");
			System.out.println("  M               Table capacity, must be positive");
			return;
		}


		// TODO: Implement experiments from section 1.3 in the handout here
//		String filename = "C:\\Users\\jordan\\IdeaProjects\\CS251Spring2023\\Project3\\src\\TestFiles\\randstrs.txt"; //filepath
		HashTable ht = new HashTable(m, "crc32-lp");
		HashTable ht2 = new HashTable(m, "crc32-dh");
		HashTable ht3 = new HashTable(m, "adler32-lp");
		HashTable ht4 = new HashTable(m, "adler32-dh");
		HashTable ht5 = new HashTable(m, "murmur3-lp");
		HashTable ht6 = new HashTable(m, "murmur3-dh");
		HashTable ht7 = new HashTable(m, "poly-lp");
		HashTable ht8 = new HashTable(m, "poly-dh");
		Scanner scan = null;
		double threshold = 0.01;
		int binNumber = 1;
		// separate int array that keep track of the load factors
		// accumulate total number of collisions
		int[] binIndex = new int[8];
		int[][] bins = new int[8][90]; //num collisions
		double[] loadFactorThreshold = new double[8];
		for (int i = 0; i < loadFactorThreshold.length; i++) {
			loadFactorThreshold[i] = 0.01;
		}
		HashTable[] hashTables = new HashTable[]{ht, ht2, ht3, ht4, ht5, ht6, ht7, ht8};
//		String[] hashTypes = new String[]{"crc32-lp", "crc32-dh", "adler32-lp", "adler32-dh", "murmur3-lp", "murmur3-dh",
//		"poly-lp", "poly-dh"};
		try {
			scan = new Scanner(new FileInputStream(inputFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		while (scan.hasNextLine()) {
			String currentString = scan.nextLine();
			boolean continueProcessing = false;
			for (int i = 0; i < hashTables.length; i++) {
				hashTables[i].insert(currentString);
				double currLoadFactor = hashTables[i].loadFactor();
				if (currLoadFactor >= 0.9) {
					continue;
				}
				continueProcessing = true;
				if (currLoadFactor >= loadFactorThreshold[i]) {
					loadFactorThreshold[i] = loadFactorThreshold[i] + 0.01;
				}
				int currentBin = (int) (currLoadFactor * 100);
				bins[i][currentBin] = bins[i][currentBin] + hashTables[i].numCollisions();
			}
			if (!continueProcessing) {
				break;
			}
		}

		double loadUpperBound = 0.01;
		System.out.println("bin,crc32-lp,crc32-dh,adler32-lp,adler32-dh,murmur3-lp,murmur3-dh,poly-lp,poly-dh,");
		for (int i = 0; i < bins[0].length; i++) {
			for (int j = 0; j < hashTables.length; j++) {
				if (j == 0) {
					System.out.printf("%.2f,", loadUpperBound);
					loadUpperBound = loadUpperBound + 0.01;
//					System.out.printf("%.2f", i + 1);
				}
				System.out.print(bins[j][i] + ",");
			}
			System.out.println();
		}

			while (scan.hasNextLine() && ht.loadFactor() < 0.9) {
//			while (ht.loadFactor() < threshold) {
////				System.out.print(threshold + ",");
//				for (int i = 0; i < hashTypes.length; i++) {
//
//					bins[i][binNumber] =
//				}
//				binNumber++;
//			}
			String str = scan.nextLine();

			ht.insert(str);
			System.out.println(str);
		}
//		while (ht.loadFactor() < 0.9) {
//			while (ht.loadFactor() < threshold) {
//				ht.insert(scan.nextLine());
//				ht.numCollisions();
//			}
//			threshold = threshold + 0.01;
//		}

	}

//	int[] processHashTable(int capacity, String hashType, String filePath) {
//		HashTable ht = new HashTable(capacity, hashType);
//		int[] bins = new int[90];
//		int binNumber = 0;
//		while (ht.loadFactor() < 0.9) {
//
//		}
//		return bins;
//	}
}
