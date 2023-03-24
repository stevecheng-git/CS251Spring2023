import java.util.Scanner;

// BloomFilter class
public class BloomFilter {

	public int m;				// Number of bits
	public int k;				// Number of hash functions
	public byte[] B;			// Array of bits
	public String hashType;

	// 8 bits per byte +
	// x1111 = 15 base 10 = 2^3 * 1 + 2^2 * 1 + 2^1 * 1 + 2^0 * 1
	// x1111 = 17 base 8 = 8^1 * 1 + 8^0 * 7
	// an integer is 32 bits or 4 bytes
	// a long is 64 bits or 8 bytes
	// research two's compliment

	// Constructor -- create bitset and pre-calculate needed prime numbers
	public BloomFilter(int m, int k, String hashType) {
		int arraySize = 0;
		arraySize = m / 8;
		if (m % 8 != 0) {
			arraySize++;
		}
		B = new byte[arraySize];
//		B[0] = (byte) 0xd4;
//		B[1] = (byte) 0x48;

		this.m = m;
		this.k = k;
		this.hashType = hashType;
	}

	// Return whether bit b in the bit array is set
	public boolean getBit(int b) {
		int numByte = b / 8;
		int indexLocation = b % 8;
		int number = 1 << indexLocation;
		if ((B[numByte] & number) == number) {
			return true;
		}
		return false;
	}

	// Set the specified bit in the bit array
	public void setBit(int b) {
		int numByte = b / 8;
		int indexLocation = b % 8;
		int number = 1 << indexLocation;
		B[numByte] = (byte) (B[numByte] | number);
	}

	// Hash the String str using the k'th hash function
	public int hash(String str, int k) {
		if (hashType.equals("checksum")) {
			return hashCheckSum(str, k);
		} else {
			return Integer.remainderUnsigned(Hasher.murmur3_32(str, k), m);
		}
	}

	public int hashCheckSum(String str, int k) {
		int hashedBitIndex = Hasher.crc32(str) + (k) * Hasher.adler32(str) + (int) Math.pow(k, 2);
		return Integer.remainderUnsigned(hashedBitIndex, m);
	}
	// Insert the string into the bitset
	public void insert(String str) {
		for (int i = 1; i <= k; i++) {
			int index = hash(str, i);
			setBit(index);
		}
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
		BloomFilter bf2 = new BloomFilter(16, 0, "murmur3");
//		System.out.println(bf2.getBit(0));
//		System.out.println(bf2.getBit(1));
//		System.out.println(bf2.getBit(2));
//		System.out.println(bf2.getBit(3));
//		System.out.println(bf2.getBit(4));
//		System.out.println(bf2.getBit(5));
//		System.out.println(bf2.getBit(6));
//		System.out.println(bf2.getBit(7));
//		System.out.println(bf2.getBit(8));
//		System.out.println(bf2.getBit(9));
//		System.out.println(bf2.getBit(10));
//		System.out.println(bf2.getBit(11));
//		System.out.println(bf2.getBit(12));
//		System.out.println(bf2.getBit(13));
//		System.out.println(bf2.getBit(14));
//		System.out.println(bf2.getBit(15));
//		System.out.println(bf2.getBit(0));
//		bf2.setBit(0);
//		System.out.println(bf2.getBit(0));
//		System.out.println(bf2.getBit(0));
//		bf2.setBit(0);
//		System.out.println(bf2.getBit(0));
		for (int i = 0; i < 16; i++) {
			System.out.println(bf2.getBit(i));
			bf2.setBit(i);
			System.out.println(bf2.getBit(i));
		}

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
