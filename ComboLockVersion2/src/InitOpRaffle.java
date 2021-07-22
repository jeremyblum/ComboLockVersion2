import java.util.HashMap;
import java.util.Map;

/**
 * Create an initial lock configuration by selecting the 10 most common letters for
 * each word position using a raffle method and adding these letters to the configuration
 * @author JP, Eddie, Ryan, Karim
 *
 */
public class InitOpRaffle extends InitializationOperator{

    /**
     * Create an initial solution
     *
     * @return a lock configuration using the strategy described in the class header
     *
     */
    @Override
    public Solution run() {
        Solution retVal = new Solution();
        Map<Character,Integer>[] letterCounts = new Map[Dictionary.commonLetters.length];
        for(int i = 0; i < letterCounts.length; i++) {letterCounts[i] = new HashMap<Character, Integer>(Dictionary.commonLetters[i]);}

        for (int w = 0; w < Solution.WHEEL_COUNT; w++) {
            while (retVal.getWheelSize(w) < Solution.WHEEL_SIZE) {
                Character letter = getRaffle(letterCounts[w]);
                if (!retVal.hasLetter(w, letter)) retVal.addLetter(w, letter);
                letterCounts[w].remove(letter);
            }
        }
        return retVal;
    }

    /**
     * @param letterCounts a HashMap containing all the letters and the frequency seen at certain position in the words
     * @return the letter that won in a raffle contest
     *
     */
    public Character getRaffle(Map<Character,Integer> letterCounts){
        int count = -1;
        for(Character l : letterCounts.keySet()){
            int letterTotal = letterCounts.get(l);
            if(count < letterTotal){
                count = letterTotal;
            }
        }

        int raffleTicket = Optimizer.prng.nextInt(count);
        Character letter = null;

        while(letter == null){
            int position = Optimizer.prng.nextInt(Solution.alphabet.length());
            Character participant = Solution.alphabet.charAt(position);
            if(letterCounts.containsKey(participant)) {
                int tickets = letterCounts.get(participant);
                if (tickets >= raffleTicket) {
                    letter = participant;
                }
            }
        }
        return letter;
    }
}
