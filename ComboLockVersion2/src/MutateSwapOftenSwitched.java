/**
 * Local search heuristic that chooses a random letter from a random word in the dictionary, and
 * ensures that that letter is present in the lock.
 *
 * @author jp, eddie, karim, ryan
 *
 */
public class MutateSwapOftenSwitched extends MutationOperator{
    static int MAX_TRIES = 1000;

    /**
     * Create a new lock configuration
     * @param input the existing configuration
     * @return a new configuration with one letter changed
     */
    @Override
    public Solution run(Solution input) {
        Solution solution = new Solution();
        int wheel = Optimizer.prng.nextInt(Solution.WHEEL_COUNT);
        int pos = Optimizer.prng.nextInt(Solution.WHEEL_SIZE);
        char maxLetter = input.getMaxLetter(input.getWheel(wheel).charAt(pos),wheel);
        int pos1 = input.getWheel(wheel).indexOf(maxLetter);
        int pos2 = (Optimizer.prng.nextInt(Solution.WHEEL_SIZE / 2) + 1 + pos) % Solution.WHEEL_SIZE;
//        while(pos2 == pos1) {
//            pos2 = Optimizer.prng.nextInt(Solution.WHEEL_SIZE);
//        }
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

}
