import java.util.Random;

public class SimpleEnemy extends Agent {

	
	/**
	 * Constructor
	 * @param coordinate -> int[] : size 2
	 * @param probabilities -> float[] : [UP, DOWN, RIGHT, LEFT]
	 */
	SimpleEnemy(int[] coordinate, float[] prob) {
		super(coordinate);
		pUP = prob[0];
		pDOWN = prob[1];
		pRIGHT = prob[2];
		pLEFT = prob[3];
	}

	@Override
	public String strategy(Environment env) {
		Random randomObject = new Random();
		float randomNumber = randomObject.nextFloat(100f);
		float lowBound = 0f;
		float highBound = pUP;
		
		// pUP
		if(lowBound <= randomNumber && randomNumber < highBound) { return "UP"; }
		lowBound = highBound;
		highBound += pDOWN;
		// pDOWN
		if(lowBound <= randomNumber && randomNumber < highBound) { return "DOWN"; }
		lowBound = highBound;
		highBound += pRIGHT;
		// pRIGHT
		if(lowBound <= randomNumber && randomNumber < highBound) { return "RIGHT"; }
		lowBound = highBound;
		highBound += pLEFT;
		// pLEFT
		if(lowBound <= randomNumber && randomNumber < highBound) { return "LEFT"; }
		
		return "STOP";
	}
}
