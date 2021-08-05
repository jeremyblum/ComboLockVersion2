import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Local search heuristic that chooses a random letter from a random word in the dictionary, and
 * ensures that that letter is present in the lock.
 *
 * @author jpa5180
 *
 */
public class MutateChooseMoreWords extends MutationOperator{
    static int MAX_TRIES = 1000;
    static int MAX_TRIES2 = 5;

    /**
     * Create a new lock configuration
     * @param input the existing configuration
     * @return a new configuration with one letter changed
     */
    @Override
    public Solution run(Solution input) {
        if(Optimizer.sleep > 0){
            //System.out.println(Optimizer.sleep); //delete
            Optimizer.sleep--;
            return new Solution();
        }
        Solution solution = null;
        boolean ok = false;
        int tries2 = 0;
        while(!ok){
            solution = new Solution();
            tries2++;
            if (tries2 > MAX_TRIES2) {
                Optimizer.sleep = 100;
                System.err.println("Giving up trying to find new letter 1");
                return solution;
            }

            char letter = '?';
            int wheel = -1;
            int tries = 0;
            while (!ok) {
                tries++;
                if (tries > MAX_TRIES) {
                    System.err.println("Giving up trying to find new letter 2");
                    return solution;
                }

                wheel = Optimizer.prng.nextInt(Solution.WHEEL_COUNT);
                letter = Solution.alphabet.charAt(Optimizer.prng.nextInt(27));
                ok = !input.hasLetter(wheel, letter);
            }
            char oldLetter = getMinLetter(input, wheel);
            for (int w = 0; w < Solution.WHEEL_COUNT; w++) {
                for (int i = 0; i < Solution.WHEEL_SIZE; i++) {
                    char currentLtr = input.getWheel(w).charAt(i);
                    if (w == wheel && currentLtr == oldLetter) {
                        solution.addLetter(w, letter);
                    }
                    else {
                        solution.addLetter(w, currentLtr);
                    }
                }
            }
            solution.getScore();
            input.getScore();
            ok = solution.words.size() > input.words.size();
        }

        return solution;
    }

    /**
     * @param input the lock we are mutating
     * @param wheel the wheel chosen to be mutated by randomness
     *
     * @return the letter that made the least amount of words in a lock at a certain wheel
     *
     */
    public char getMinLetter(Solution input, int wheel){
        ArrayList<String> words = Dictionary.score(input);
        HashMap<Character, Integer> letterCounts = new HashMap<>();
        for(String w : words){
            char wordLetter = w.charAt(wheel);
            letterCounts.put(wordLetter, letterCounts.getOrDefault(wordLetter, 0) + 1);
        }

        int count = Integer.MAX_VALUE;
        char letter = '?';
        for(Character l : letterCounts.keySet()){
            int letterTotal = letterCounts.getOrDefault(l, 0);
            if(count > letterTotal){
                count = letterTotal;
                letter = l;
            }
        }
        return letter;
    }

}
