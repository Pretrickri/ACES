import java.util.ArrayList;
import java.util.List;

public class ACES extends Agent{
	ACES(int[] coordinate) {
		super(coordinate);
		this.pUP = 1f;
		this.pDOWN = 1f;
		this.pRIGHT = 1f;
		this.pLEFT = 1f;
		this.pSTOP = 1f;
		// TODO Auto-generated constructor stub
	}

	/**
	 * Takes the vector from the object on the given coordinate to the agent.
	 * @param coord -> int[] : size 2
	 * @return -> int[] : size 2 (vector)
	 */
	public int[] vectorFind(int[] coord) {
		int xCoord = coordinate[0] = coord[0];
		int yCoord = coordinate[1] = coord[1];
		int[] returnCoord = new int[] {xCoord,yCoord};
		return returnCoord;
	}
	
	/**
	 * Calculates the probabilities based on the points that the probabilities had.
	 */
	private void calculateProbabilities() {
		float total = pUP+pDOWN+pRIGHT+pLEFT+pSTOP;
		pUP = pUP/total;
		pDOWN = pDOWN/total;
		pRIGHT = pRIGHT/total;
		pLEFT = pLEFT/total;
		pSTOP = pSTOP/total;
	}
	
	/**
	 * Resets all probabilities to be back to equal.
	 */
	private void resetProbabilities() {
		this.pUP = 1f;
		this.pDOWN = 1f;
		this.pRIGHT = 1f;
		this.pLEFT = 1f;
		this.pSTOP = 1f;
	}
	
	private void radarScan(boolean[][] map, String type) {
		for(int i = -2; i <= 2; i++) {
			for(int j = -2; j <= 2; j++) {
				if(map[this.coordinate[0]+i][this.coordinate[1]+j]) {
					int[] vector = this.vectorFind(new int[] {i,j});
					if(i>0) { // it's kinda weird with matrixes
						if(type.toLowerCase().equals("attract")) {
							pDOWN++;
						}
						else if(type.toLowerCase().equals("reject")) {
							pDOWN--;
						}
						else { System.err.println("INVALID TYPE IN radarScan()"); }
					}
					else if(i<0) {
						if(type.toLowerCase().equals("attract")) {
							pUP++;
						}
						else if(type.toLowerCase().equals("reject")) {
							pUP--;
						}
						else { System.err.println("INVALID TYPE IN radarScan()"); }
					}
					if(j>0) {
						if(type.toLowerCase().equals("attract")) {
							pRIGHT++;
						}
						else if(type.toLowerCase().equals("reject")) {
							pRIGHT--;
						}
						else { System.err.println("INVALID TYPE IN radarScan()"); }
					}
					else if(j<0) {
						if(type.toLowerCase().equals("attract")) {
							pLEFT++;
						}
						else if(type.toLowerCase().equals("reject")) {
							pLEFT--;
						}
						else { System.err.println("INVALID TYPE IN radarScan()"); }
					}
				}
			}
		}
	}

	@Override
	public String strategy(Environment env) {
		radarScan(env.foodMap, "attract");
		System.out.println("pUP = " + pUP +
				"pDOWN = " + pDOWN +
				"pRIGHT = =" + pRIGHT +
				"pLEFT = " + pLEFT +
				"pSTOP = " + pSTOP);
		resetProbabilities();
		return this.stratEvaluator();
	}
}
