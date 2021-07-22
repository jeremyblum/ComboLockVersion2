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

    /**
     * Create a new lock configuration
     * @param input the existing configuration
     * @return a new configuration with one letter changed
     */
    @Override
    public Solution run(Solution input) {
        Solution solution = new Solution();
        char letter = '?';
        int wheel = Optimizer.prng.nextInt(Solution.WHEEL_COUNT);
        int pos = Optimizer.prng.nextInt(Solution.WHEEL_SIZE);
        int pos1 = (pos + 1) % Solution.WHEEL_SIZE;
        int pos2 = getMinIndex(input.getWheel(wheel), wheel, pos);
        while(pos2 == pos1 || pos2 == -1) {
            pos2 = Optimizer.prng.nextInt(Solution.WHEEL_SIZE);
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
     * @param sourceWheel letters on a certain wheel of the lock
     * @param wheel the number of the specific wheel of a the lock
     * @param position the position of the letter whe are trying to avoid
     * @return the position of the letter that is the least common in all the words at a certain position of a certain wheel
     *
     */
    public int getMinIndex(String sourceWheel, int wheel, int position){
        int pos = -1;
        int count = Integer.MAX_VALUE;
        for(int i = 0; i < sourceWheel.length(); i++){
            int letterCount = Dictionary.commonLetters[wheel].getOrDefault(sourceWheel.charAt(i), -1);
            if (letterCount != -1 && i != position && letterCount < count){
                count = letterCount;
                pos = i;
            }
        }
//        System.out.print(letter + " ");
//        System.out.print(count + "\n");
        return pos;
    }
}
