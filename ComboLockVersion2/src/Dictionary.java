import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
/**
 * A class with static methods that represent a collection of words. 
 * The maximum word length is indicated by the WHEEL_COUNT field of the Solution class.  All words from the words.txt file with
 * a length less than or equal to the maximum are stored in the collection.
 * Words containing non-alphabetic symbols are ignored.
 * The words themselves are stored as arrays of integers with a 1 corresponding to 'a', 2 to 'b', ..., and 26 to 'z'
 * For words less than the maximum length a 0 is used to represent a space.
 */
public class Dictionary {
	static ArrayList<String> rawWords = new ArrayList<>();
	static Map<Character,Integer>[] commonLetters = new Map[Solution.WHEEL_COUNT];; // frequency of the letters at each index
	
	/**
	 * Load the dictionary
	 */
	static {
		int wordSize = Solution.WHEEL_COUNT;
		Scanner sc;
		try {
			sc = new Scanner(new File("words_with_frequency.txt"));
		} catch (FileNotFoundException e) {
			throw new RuntimeException("Missing word list");
		}
		while (sc.hasNext()) {
			String word = sc.next().toLowerCase();
			Long count = sc.nextLong();
			boolean ok = true;
			if (word.length() > wordSize) { 
				ok = false;
			}
			for (int i = 0; ok && i < word.length(); i++) {
				if (word.charAt(i) < 'a' || word.charAt(i) > 'z') ok = false;
			}
			
			if (ok) {
				while (word.length() < Solution.WHEEL_COUNT)
					word += ' ';
				rawWords.add(word);
				for (int i = 0; i < word.length(); i++) {
					if(commonLetters[i] == null){
						commonLetters[i] = new HashMap<Character,Integer>();
					}
					//keeps the position and the count of how many times a letter has been seen in a word
					commonLetters[i].put(word.charAt(i), commonLetters[i].getOrDefault(word.charAt(i), 0) + 1);
				}
			}
		}
		sc.close();
	}
	
	/**
	 * Return the words in the dictionary that can be made with a given
	 * lock configuration
	 *
	 * @param solution the lock configuration
	 * @return a list of words in the dictionary that can be represented on the lock
	 */
	static ArrayList<String> score(Solution solution) {
		ArrayList<String> list = new ArrayList<>();
		for (String s: rawWords) {
			boolean ok = true;
			for (int j = 0; ok && j < s.length(); j++) {
				if (!solution.hasLetter(j, s.charAt(j))) ok = false;
			}
			if (ok) list.add(s);
		}
		return list;
	}

	
	/**
	 * Get a random word from the dictionary
	 * 
	 * @return a word from the dictionary chosen at random, with letter encoded as described in the 
	 *         header comment for this class
	 */
	static String getRandomWord() {
		int i = Optimizer.prng.nextInt(rawWords.size());
		return rawWords.get(i);
	}
}
