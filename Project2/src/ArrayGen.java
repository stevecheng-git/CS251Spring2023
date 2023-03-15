import java.util.Random;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Collections;

// Integer array generator class
public class ArrayGen {

	// Possible output orders
	enum Order { RANDOM, INCREASING, DECREASING };

	public int N;			// Size of array to generate
	public int rangeLo;		// Minimum value to generate
	public int rangeHi;		// Maximum value to generate
	public Order order;		// Order of generated values
	Random rand;			// Random number generator
	boolean verbose;		// Print argument values as well as array
	boolean doNothing;		// Whether to simply exit (if called with --help)


	// Constructor -- set default parameters
	public ArrayGen() {
		N = 10;
		rangeLo = -N;
		rangeHi = N;
		order = Order.RANDOM;
		rand = new Random();
		doNothing = false;
	}


	// Generate an array of integers
	public ArrayList<Integer> generate() {
		ArrayList<Integer> array = new ArrayList<Integer>();

		// Randomly sample integers from the range
		for (int i = 0; i < N; i++) {
			array.add(rand.nextInt(rangeHi - rangeLo + 1) + rangeLo);
		}

		// Optionally sort the array before returning
		if (order == Order.INCREASING)
			Collections.sort(array);
		else if (order == Order.DECREASING)
			Collections.sort(array, Collections.reverseOrder());

		return array;
	}


	// Print an array to STDOUT
	public static void printArray(ArrayList<Integer> array) {
		for (int i = 0; i < array.size(); i++) {
			System.out.print(array.get(i));
			if (i < array.size() - 1)
				System.out.print(" ");
		}
		System.out.println();
	}


	// Interpret command-line arguments
	public void parseArgs(String[] args) {
		boolean setRange = false;

		for (int i = 0; i < args.length; i++) {
			// Help text
			if (args[i].equals("--help") || args[i].equals("-h")) {
				System.out.println("Usage: java ArrayGen [OPTIONS]");
				System.out.println();
				System.out.println("OPTIONS:");
				System.out.println("  -n N              Size of array to generate");
				System.out.println("                    Default: 10");
				System.out.println("  -r,--range LO HI  Generate numbers between LO and HI inclusive");
				System.out.println("                    Default: -N to +N");
				System.out.println("  --sort-incr       Sort array in increasing order");
				System.out.println("  --sort-decr       Sort array in decreasing order");
				System.out.println("  -s,--seed S       Fixed seed for random number generator");
				System.out.println("  -v,--verbose      Print extra information");
				System.out.println("  -h,--help         Print this message and quit");

				doNothing = true;

			// Number of elements to generate
			} else if (args[i].equals("-n")) {
				N = Integer.parseInt(args[++i]);
				if (N <= 0)
					throw new IllegalArgumentException("N cannot be <= 0");

				if (!setRange) {
					rangeLo = -N;
					rangeHi = N;
				}

			// Possible range of values
			} else if (args[i].equals("--range") || args[i].equals("-r")) {
				rangeLo = Integer.parseInt(args[++i]);
				rangeHi = Integer.parseInt(args[++i]);
				if (rangeLo > rangeHi) {
					int temp = rangeLo;
					rangeLo = rangeHi;
					rangeHi = temp;
				}
				setRange = true;

			// Sort output in increasing order
			} else if (args[i].equals("--sort-incr")) {
				order = Order.INCREASING;

			// Sort output in decreasing order
			} else if (args[i].equals("--sort-decr")) {
				order = Order.DECREASING;

			// Set a fixed seed for the random number generator
			} else if (args[i].equals("--seed") || args[i].equals("-s")) {
				long seed = Long.parseLong(args[++i]);
				rand = new Random(seed);

			// Print argument values (don't pipe to Sorter)
			} else if (args[i].equals("--verbose") || args[i].equals("-v")) {
				verbose = true;

			// Unexpected argument
			} else {
				throw new IllegalArgumentException("Unexpected argument " + args[i]);
			}
		}
	}


	// Program entry point
	public static void main(String[] args) {
		// Create new generator and parse args
		ArrayGen ag = new ArrayGen();
//		ag.parseArgs(args);

		// 9 different combinations of gathering data (varying n, and the way the array was generated)
		if ("generate1000".equals(args[0])) {
			ag.parseArgs(new String[]{"-n", "1000"});
		} else if ("generate10000".equals(args[0])) {
			ag.parseArgs(new String[]{"-n", "10000"});
		} else if ("generate100000".equals(args[0])) {
			ag.parseArgs(new String[]{"-n", "100000"});
		} else if ("generate1000sortIncr".equals(args[0])) {
			ag.parseArgs(new String[]{"-n", "1000", "--sort-incr"});
		} else if ("generate10000sortIncr".equals(args[0])) {
			ag.parseArgs(new String[]{"-n", "10000", "--sort-incr"});
		} else if ("generate100000sortIncr".equals(args[0])) {
			ag.parseArgs(new String[]{"-n", "100000", "--sort-incr"});
		} else if ("generate1000sortDecr".equals(args[0])) {
			ag.parseArgs(new String[]{"-n", "1000", "--sort-decr"});
		} else if ("generate10000sortDecr".equals(args[0])) {
			ag.parseArgs(new String[]{"-n", "10000", "--sort-decr"});
		} else if ("generate100000sortDecr".equals(args[0])) {
			ag.parseArgs(new String[]{"-n", "100000", "--sort-decr"});
		} else {

		// mention why you didn't use lower input values in report
			ag.parseArgs(args);
		}


		if (ag.doNothing)
			return;

		// Optionally print extra info
		if (ag.verbose) {
			System.out.println("N: " + ag.N);
			System.out.println("Range: " + ag.rangeLo + ", " + ag.rangeHi);
			System.out.println("Order: " + ag.order);
			System.out.println();
		}

		// Generate the array and print it
		ArrayList<Integer> array = ag.generate();
		printArray(array);
	}
}
