import java.util.HashMap;
import java.util.Map;

/**
 * Local search heuristic that chooses a random letter from a random word in the dictionary, and
 * ensures that that letter is present in the lock.
 *
 * @author jpa5180
 *
 */
public class MutateChooseCommonLetter extends MutationOperator{
    static int MAX_TRIES = 1000;

    /**
     * Create a new lock configuration
     * @param input the existing configuration
     * @return a new configuration with one letter changed
     */
    @Override
    public Solution run(Solution input) {
        Solution solution = new Solution();
        Map<Character,Integer>[] letterCounts = new Map[Dictionary.commonLetters.length];
        for(int i = 0; i < letterCounts.length; i++) {letterCounts[i] = new HashMap<Character, Integer>(Dictionary.commonLetters[i]);}
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

            wheel = Optimizer.prng.nextInt(Solution.WHEEL_COUNT);
            letter = getMax(letterCounts[wheel]);
            ok = !input.hasLetter(wheel, letter);
            letterCounts[wheel].remove(letter);
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

    /**
     * @param letterCounts a HashMap containing all the letters and the frequency seen at certain position in the words
     * @return the letter that is the most common in all the words at a certain position
     *
     */
    public Character getMax(Map<Character,Integer> letterCounts){
        int count = -1;
        Character letter = null;
        for(Character l : letterCounts.keySet()){
            int letterTotal = letterCounts.get(l);
            if(count < letterTotal){
                count = letterTotal;
                letter = l;
            }
        }
//        System.out.print(letter + " ");
//        System.out.print(count + "\n");
        return letter;
    }

}
