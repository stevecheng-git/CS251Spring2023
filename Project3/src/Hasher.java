import com.google.common.hash.Hashing;
import com.google.common.hash.HashFunction;
import java.util.Random;

// Hasher class -- implements different hashing algorithms
public class Hasher {

	// Cyclic redundancy checksum
	public static int crc32(String str) {
		return Hashing.crc32().hashBytes(str.getBytes()).asInt();
	}

	// Adler 32 checksum
	public static int adler32(String str) {
		return Hashing.adler32().hashBytes(str.getBytes()).asInt();
	}

	// Murmur 3 algorithm
	public static int murmur3_32(String str, int seedIdx) {
		return Hashing.murmur3_32_fixed(seeds[seedIdx]).hashBytes(str.getBytes()).asInt();
	}

	// Polynomial rolling hash function (similar to textbook implementation)
	public static int polynomial(String str, int primeIdx) {
		byte[] bytes = str.getBytes();
		int hash_val = 0;
		int p = primes[primeIdx];
		for (int i = 0; i < bytes.length; i++) {
			hash_val = p * (hash_val + bytes[i]);
		}
		return hash_val;
	}

	// Array of 100 random seeds
	public static final int[] seeds = {
		 -673499903, -1475624584,   632581354, -1346455844,
		 -598714308,  1221291782, -1056129704,   164267962,
		 1615820829, -1662238266,  1105567566,   838512192,
		 -263464778,   970887222,   139344590,   623705611,
		-1242010636,  -920132143,  1656128116,   -31716776,
		-1948430864,   573336280,   479885803,   545807709,
		  769143429, -1346337707, -1896419421,  1672692214,
		-1145202655, -1780092836, -1612472896,  1502217092,
		-2141596704,  -662183276,    66709077,   208324626,
		 -345093418,   -12720907,   988716753,  -530945864,
		  171469996,    72712402,  1278779065,   806677572,
		 2094351213, -1294011366,  1342506391,   685279254,
		 -122835807,  1618573018, -1621791694,  -614118582,
		  910462875,  1616893189,  -617384706,  1672075292,
		-1535007473,  -570531721,  1235775445,   -46674040,
		  707306471,  2053603127, -1226898201,  1327551281,
		 2027284310,   294410748, -1262478609,  -930659965,
		  685171244,   913534706,  -893686617, -1908666341,
		-1010437268,   150932669, -1384152707,  -613062555,
		-1573490661,   275904958, -1569981049,  1514138246,
		-1204357542,  1409338260, -1346077517, -2046379395,
		  440229303,  1386394340, -2007405246, -1314079868,
		-1183786817,   356302685,  1388289398,  2104729020,
		 -126084137,   -26288138, -2004455287,   -40656376,
		-1034248885,  1375347578,  1818750689,  1127299128 };

	// Array of 100 consecutive prime numbers
	public static final int[] primes = {
		103613, 103619, 103643, 103651, 103657,
		103669, 103681, 103687, 103699, 103703,
		103723, 103769, 103787, 103801, 103811,
		103813, 103837, 103841, 103843, 103867,
		103889, 103903, 103913, 103919, 103951,
		103963, 103967, 103969, 103979, 103981,
		103991, 103993, 103997, 104003, 104009,
		104021, 104033, 104047, 104053, 104059,
		104087, 104089, 104107, 104113, 104119,
		104123, 104147, 104149, 104161, 104173,
		104179, 104183, 104207, 104231, 104233,
		104239, 104243, 104281, 104287, 104297,
		104309, 104311, 104323, 104327, 104347,
		104369, 104381, 104383, 104393, 104399,
		104417, 104459, 104471, 104473, 104479,
		104491, 104513, 104527, 104537, 104543,
		104549, 104551, 104561, 104579, 104593,
		104597, 104623, 104639, 104651, 104659,
		104677, 104681, 104683, 104693, 104701,
		104707, 104711, 104717, 104723, 104729 };
}