import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BloomFilterTest {
    @Test
    @DisplayName("1. Small-scale test for getBit() and setBit().")
    public void testCase1() throws IOException {
        new BitsTest("bits_small_unique", 32, "bits_small_unique").run();
    }

    @Test
    @DisplayName("2. Medium-scale tests for getBit() and setBit().")
    public void testCase2() throws IOException {
        for (int m : Arrays.asList(1024, 1009, 1250)) {
            String name = String.format("bits_medium-%s_unique", m);
            new BitsTest(name, m, name).run();
            name = String.format("bits_medium-%s_random", m);
            new BitsTest(name, m, name).run();
        }
    }

    @Test
    @DisplayName("3. Medium-scale tests for hash().")
    public void testCase3() throws IOException {
        for (int m : Arrays.asList(1024, 1009, 1250))
            for (String hashType : Arrays.asList("checksum", "murmur"))
                new HashTest("hash_medium", m, hashType, String.format("hash_medium-%s_%s", m, hashType)).run();
    }

    @Test
    @DisplayName("4. Small-scale special-case tests for insert().")
    public void testCase4() throws IOException {
        new InsertTest("insert_disjoint", 64, 5, "checksum", "insert_disjoint").run();
        new InsertTest("insert_collisions", 64, 5, "checksum", "insert_collisions").run();
        new InsertTest("insert_overload", 64, 5, "checksum", "insert_overload").run();
    }

    @Test
    @DisplayName("5. Medium-scale tests for insert().")
    public void testCase5() throws IOException {
        List<Integer> mList = Arrays.asList(23918, 36425, 47836);
        List<Integer> kList = Arrays.asList(4, 6, 7);

        for (int i = 0; i < mList.size(); i++) {
            for (String hashType : Arrays.asList("checksum", "murmur")) {
                int m = mList.get(i);
                int k = kList.get(i);
                new InsertTest("insert_medium", m, k, hashType,
                        String.format("insert_medium-%s-%s_%s", m, k, hashType)).run();
            }
        }
    }

    @Test
    @DisplayName("6. Medium-scale tests for lookup().")
    public void testCase6() throws IOException {
        List<Integer> mList = Arrays.asList(4784, 7285, 9568);
        List<Integer> kList = Arrays.asList(4, 6, 7);

        for (int i = 0; i < mList.size(); i++) {
            for (String hashType : Arrays.asList("checksum", "murmur")) {
                for (double activeProb : Arrays.asList(0.2, 0.4, 0.6, 0.8)) {
                    int m = mList.get(i);
                    int k = kList.get(i);
                    String name = String.format("lookup_medium-%s-%s_%s_%s", m, k, hashType, (int) (activeProb * 100));
                    new LookupTest(name, m, k, hashType, name).run();
                }
            }
        }
    }

    private static abstract class BaseTest {
        String inName;
        int m;
        int k;
        String hashType;
        String outName;

        BaseTest(String inName, int m, int k, String hashType, String outName) {
            this.inName = inName;
            this.m = m;
            this.k = k;
            this.hashType = hashType;
            this.outName = outName;
        }

        void run() throws IOException {
            System.out.println("Running test: " + this);

            BloomFilter bloomFilter = new BloomFilter(m, k, hashType);
            List<String> inputs = Files.readAllLines(Paths.get("C:\\Users\\jorda\\IdeaProjects\\CS251Spring2023\\Project3Part2\\src", "part2", "input", inName + ".txt"));
            List<String> outputs = Files.readAllLines(Paths.get("C:\\Users\\jorda\\IdeaProjects\\CS251Spring2023\\Project3Part2\\src", "part2", "expected", outName + ".txt"));

            for (int t = 0; t < inputs.size(); t++) {
                runStep(bloomFilter, t, inputs.get(t), outputs.get(t));
            }
        }

        abstract void runStep(BloomFilter bloomFilter, int t, String input, String output);

        @Override
        public String toString() {
            return String.format("inName = %s, m = %s, k = %s, hashType = %s, outName = %s",
                    inName, m, k, hashType, outName);
        }
    }

    private static class BitsTest extends BaseTest {
        private Set<Integer> active;

        BitsTest(String inName, int m, String outName) {
            super(inName, m, 5, "checksum", outName);   // k and hashType arguments are immaterial
            active = null;
        }

        void run() throws IOException {
            active = new HashSet<>();
            super.run();
        }

        @Override
        void runStep(BloomFilter bloomFilter, int t, String input, String output) {
            int b = Integer.parseInt(input);
            active.add(b);
            bloomFilter.setBit(b);

            String[] outputTokens = output.split(" ");
            byte[] expected = new byte[outputTokens.length];
            for (int i = 0; i < outputTokens.length; i++) {
                expected[i] = Byte.parseByte(outputTokens[i]);
            }
            Utils.checkArrayEquals(expected, bloomFilter.B,
                    String.format("Filter state is incorrect after input %s", t + 1));

            for (b = 0; b < bloomFilter.m; b++) {
                Utils.checkEquals(active.contains(b), bloomFilter.getBit(b),
                        String.format("getBit(%s) is incorrect after input %s", b, t + 1));
            }
        }
    }

    private static class HashTest extends BaseTest {
        HashTest(String inName, int m, String hashType, String outName) {
            super(inName, m, 100, hashType, outName);
        }

        @Override
        void runStep(BloomFilter bloomFilter, int t, String input, String output) {
            String[] outputTokens = output.split(" ");
            for (int k = 0; k < 100; k++) {
                int expected = Integer.parseInt(outputTokens[k]);
                int actual = bloomFilter.hash(input, k);
                Utils.checkEquals(expected, actual, String.format("hash(x, %s) is incorrect for input %s", k, t + 1));
            }
        }
    }

    private static class InsertTest extends BaseTest {
        InsertTest(String inName, int m, int k, String hashType, String outName) {
            super(inName, m, k, hashType, outName);
        }

        @Override
        void runStep(BloomFilter bloomFilter, int t, String input, String output) {
            String[] outputTokens = output.split(" ");
            byte[] expected = new byte[outputTokens.length];
            for (int i = 0; i < outputTokens.length; i++) {
                expected[i] = Byte.parseByte(outputTokens[i]);
            }

            bloomFilter.insert(input);
            Utils.checkArrayEquals(expected, bloomFilter.B,
                    String.format("Filter state is incorrect after input %s", t + 1));
        }
    }

    private static class LookupTest extends BaseTest {
        LookupTest(String inName, int m, int k, String hashType, String outName) {
            super(inName, m, k, hashType, outName);
        }

        @Override
        void runStep(BloomFilter bloomFilter, int t, String input, String output) {
            if (t == 0) {
                for (int j = 0; j < input.length(); j++) {
                    if (input.charAt(j) == '1') {
                        bloomFilter.setBit(j);
                    }
                }
            } else {
                boolean expected = Boolean.parseBoolean(output);
                boolean actual = bloomFilter.lookup(input);
                Utils.checkEquals(expected, actual, String.format("lookup() is incorrect for input %s", t + 1));
            }
        }
    }
}
