/**
 * Local search heuristic that chooses a random letter from a random word in the dictionary, and
 * ensures that that letter is present in the lock.  
 * 
 * @author jjb24
 *
 */
public class MutateChooseLetterFromWord extends MutationOperator{
	static int MAX_TRIES = 1000;

	/**
	 * Create a new lock configuration 
	 * @param input the existing configuration
	 * @return a new configuration with one letter changed
	 */
	@Override
	public Solution run(Solution input) {
		Solution solution = new Solution();
		char letter = '?';
		int wheel = -1;
		boolean ok = false;
		int tries = 0;
		while (!ok) {
			tries++;
			if (tries > MAX_TRIES) {
				System.err.println("Giving up trying to find new letter");
				return solution;
			}
			
			String word = Dictionary.getRandomWord();
			wheel = Optimizer.prng.nextInt(Solution.WHEEL_COUNT);
			letter = word.charAt(wheel);
			ok = !input.hasLetter(wheel, letter);
		}
		char oldLetter = '?';
		ok = false;
		tries = 0;
		while (!ok) {
			tries++;
			if (tries > MAX_TRIES) {
				System.err.println("Giving up trying to find old letter");
				return solution;
			}
			oldLetter = Solution.alphabet.charAt(Optimizer.prng.nextInt(27));
			ok = input.hasLetter(wheel, oldLetter);
		}
		for (int w = 0; w < Solution.WHEEL_COUNT; w++) {
			for (int i = 0; i < Solution.WHEEL_SIZE; i++) {
				char currentLtr = input.getWheel(w).charAt(i);
				if (w == wheel && currentLtr == oldLetter) {
					solution.addLetter(w, oldLetter);
				}
				else {
					solution.addLetter(w, currentLtr);
				}
			}
		}
		return solution;
	}

}
