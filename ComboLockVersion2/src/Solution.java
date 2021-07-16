import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * A class whose objects represent lock configurations
 */
public class Solution implements Comparable<Solution> {
	static int WHEEL_COUNT = 4; // The target word length
	static int WHEEL_SIZE = 10; // The number of letters on each wheel
	static String alphabet = " abcdefghijklmnopqrstuvwxyz"; // The symbols used on the wheels - note that
															// changing this will break the system	
	String[] wheels = new String[WHEEL_COUNT]; // The actual wheel layouts
	ArrayList<String> words;
	
	Double score; // The fitness of the lock, or null if the fitness still needs to be calculated.

	public Solution() {
		for (int i = 0; i < wheels.length; i++)
			wheels[i] = "";
	}
	
	/**
	 * Get the fitness of the lock configuration
	 * @return a score, which is currently the total weight of words that can be made from the
	 *         lock configuration
	 */
	double getScore() {
		if (score == null) {
			//long time = System.currentTimeMillis();
			words = Dictionary.score(this);
			//System.out.println("Can make " + words.size());
			double[] overallAve = new double[words.size()];
			double aveSoFar = 0.0;
			double adjustment = -1.0;
			for (int i = 0; i < words.size(); i++) {
				int closestDist = Integer.MAX_VALUE;
				int closestNextDist = Integer.MAX_VALUE;
				int closestWord = -1;
				for (int j = 0; j < words.size(); j++) {
					if (j == i) continue;
					int wordDist = distance(i, j);
					if (wordDist < closestDist) {
						closestNextDist = closestDist;
						closestDist = wordDist;
						closestWord = j;
					}
					if (wordDist < closestNextDist && j != closestWord) {
						closestNextDist = wordDist;
					}
					if (closestNextDist == 2) break; // Already found two words very close
				}
				double ave = (closestDist + closestNextDist) / 2.0;
				double aveMinusClsDist = ave - closestDist;
				if(aveMinusClsDist > adjustment){
					adjustment = aveMinusClsDist;
				}
				overallAve[i] = 1 + ave;
			}
			Arrays.sort(overallAve);
			score = 0.0;
			for(int i = 0; i < overallAve.length; i++){
				aveSoFar = overallAve[i] + aveSoFar;
				score += aveSoFar;
			}

			score -= adjustment;
			score /= words.size();
			//System.out.println("Score: " + score + " in " + (System.currentTimeMillis() - time) + " ms");
		}
		return score;
	}
	
	/**
	 * Determine if a wheel contains a specific letter
	 * @param wheel the wheel in question in range [0..wheel count)
	 * @param letter an integer representation of the letter, with 0 = ' ', 1 = 'a', 2 = 'b', etc.
	 * @return true iff wheel has this letter
	 */
	boolean hasLetter(int wheel, char letter) {
		return wheels[wheel].indexOf(letter) >= 0;
	}
	
	/**
	 * Add a letter to a wheel
	 * @param wheel the target wheel
	 * @param letter an integer representation of the letter, with 0 = ' ', 1 = 'a', 2 = 'b', etc.
	 */
	void addLetter(int wheel, char letter) {
		if (alphabet.indexOf(letter) < 0) throw new IllegalArgumentException("Unknown letter: " + letter);
		wheels[wheel] += letter;
		score = null;
	}
	
	/**
	 * Return the String of letters on a wheel
	 * 
	 * @param wheel 
	 * @return
	 */
	String getWheel(int wheel) {
		return wheels[wheel];
	}

	/**
	 * Return the number of letters currently selected for a wheel
	 * @param wheel the wheel in question
	 * @return the number of letters that have been specified for the wheel
	 */
	int getWheelSize(int wheel) {
		return wheels[wheel].length();
	}
	
	/**
	 * @return a string representation of the lock, with the letter that have been selected
	 */
	@Override 
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("From: ");
		sb.append(words.size());
		sb.append(" words. ");
		
		for (int w = 0; w < wheels.length; w++) {
			sb.append("/");
			sb.append(wheels[w]);
			sb.append("/");
		}
		return sb.toString();
	}
	
	/**
	 * @return true iff the two solutions have exactly the same letters selected for each wheel
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (this == obj) return true;
		if (this.getClass() != obj.getClass()) return false;
		Solution that = (Solution) obj;
		for (int w = 0; w < wheels.length; w++) {
			if (!this.wheels[w].equals(that.wheels[w])) return false;
		}
		return true;
	}

	/**
	 * @return an appropriate hash code for the lock
	 */
	@Override
	public int hashCode() {
		int retVal = 0;
		for (int w = 0; w < wheels.length; w++) {
			retVal = retVal * 31 + wheels[w].hashCode();
		}
		return retVal;
	}
	
	
	/** 
	 * Return distance of words using current lock configuration
	 * 
	 * @param word1Ndx
	 * @param word2Ndx
	 * @return
	 */
	int distance(int word1Ndx, int word2Ndx) {
		String word1 = words.get(word1Ndx);
		String word2 = words.get(word2Ndx);
		int retVal = 0;
		for (int i = 0; i < word1.length(); i++) {
			retVal += distance(i, word1.charAt(i), word2.charAt(i));
		}

		return retVal;
	}
	
	/** 
	 * Return number of turns needed to move from one character to another
	 * on a particular wheel
	 * @param wheel Which wheel we are turning
	 * @param c1 The starting character
	 * @param c2 The ending character
	 * @return the number of moves needed
	 */
	private int distance(int wheel, char c1, char c2) {
		int pos1 = wheels[wheel].indexOf(c1);
		int pos2 = wheels[wheel].indexOf(c2);
		
		int steps = Math.abs(pos1-pos2);
		steps = Math.min(steps, Math.abs((wheels[wheel].length() + pos1) - pos2));
		steps = Math.min(steps, Math.abs((wheels[wheel].length() + pos2) - pos1));
		return steps;
	}
	
	@Override
	public int compareTo(Solution that) {
		if (this.getScore() > that.getScore()) return -1;
		else if (this.getScore() < that.getScore()) return 1;
		return 0;
	}
}
