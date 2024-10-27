import java.util.Random;

public class SimpleAgent extends Agent{

	/**
	 * Constructor
	 * @param coordinate -> int[] : size 2
	 * @param probabilities -> float[] : [UP, DOWN, RIGHT, LEFT, STOP]
	 */
	SimpleAgent(int[] coordinate, float[] prob) {
		super(coordinate);
		pUP = prob[0];
		pDOWN = prob[1];
		pRIGHT = prob[2];
		pLEFT = prob[3];
		pSTOP = prob[4];
	}

	@Override
	public String strategy(Environment env) {
		return this.stratEvaluator();
	}

}
