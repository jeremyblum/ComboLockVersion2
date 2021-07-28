import java.util.Arrays;
import java.util.Map;

/**
 * Local search heuristic that chooses a random letter from a random word in the dictionary, and
 * ensures that that letter is present in the lock.
 *
 * @author jpa5180
 *
 */
public class MutateSwapCommonLetter extends MutationOperator{
    static int MAX_TRIES = 1000;
    boolean[] isCommon; //keep track of what letters are common in a wheel

    /**
     * Create a new lock configuration
     * @param input the existing configuration
     * @return a new configuration with one letter changed
     */
    @Override
    public Solution run(Solution input) {
        Solution solution = new Solution();
        char letter = '?';
        boolean ok = false;
        int wheel = -1;
        int pos1 = -1;
        int pos2 = -1;
        int tries = 0;

        while(!ok){
            tries++;
            if (tries > MAX_TRIES) {
                System.err.println("Giving up trying to swap letters");
                return solution;
            }

            wheel = Optimizer.prng.nextInt(Solution.WHEEL_COUNT);
            orderLetters(input.getWheel(wheel), wheel);
            int pos = Optimizer.prng.nextInt(Solution.WHEEL_SIZE);
            boolean posCommon = isCommon[pos];
            pos1 = (pos + 1) % Solution.WHEEL_SIZE;
            boolean pos1Common = isCommon[pos1];
            pos2 = (pos1 + 1) % Solution.WHEEL_SIZE;
            boolean pos2Common = isCommon[pos2];
            if(posCommon == pos1Common){
                while(pos2Common == pos1Common && pos != pos2) {
                    pos2 = (pos2 + 1) % Solution.WHEEL_SIZE;
                    pos2Common = isCommon[pos2];
                }
                ok = pos != pos2;
            }
        }
        for (int w = 0; w < Solution.WHEEL_COUNT; w++) {
            String sourceWheel = input.getWheel(w);

            for (int i = 0; i < Solution.WHEEL_SIZE; i++) {
                if (w == wheel && i == pos1) {
                    solution.addLetter(w, sourceWheel.charAt(pos2));
                }
                else if (w == wheel && i == pos2) {
                    solution.addLetter(w, sourceWheel.charAt(pos1));
                }
                else {
                    solution.addLetter(w, sourceWheel.charAt(i));
                }
            }
        }
        return solution;
    }

    /**
     * Orders the letters from least common to most common and creates a boolean
     * array to keep track of which letters are common in a wheel
     * @param sourceWheel letters on a certain wheel of the lock
     * @param wheel the number of the specific wheel of a the lock
     *
     */
    public void orderLetters(String sourceWheel, int wheel){
        int [] letterOrder = new int[sourceWheel.length()];
        isCommon = new boolean[sourceWheel.length()];
        for(int i = 0; i < sourceWheel.length(); i++){
            int letterCount = Dictionary.commonLetters[wheel].getOrDefault(sourceWheel.charAt(i), 0);
            letterOrder[i] = letterCount;
        }
        Arrays.sort(letterOrder);
        for(int l = 0; l < sourceWheel.length(); l++){
            int letterCount = Dictionary.commonLetters[wheel].getOrDefault(sourceWheel.charAt(l), 0);

            for(int n = 0; n < letterOrder.length; n++){
                if(letterOrder[n] == letterCount){
                    isCommon[l] = n >= (sourceWheel.length() / 2);
                    letterOrder[n] = -1;
                    break;
                }
            }
        }
    }
}
