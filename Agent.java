
public abstract class Agent{
	public int[] coordinate;
	public boolean skip;
	public boolean isDead;
	public boolean foundEnd;
	public int foodCount;
	public int killCount;
	public int steps;
	public String currentStrat;
	protected float pUP, pDOWN, pRIGHT, pLEFT, pSTOP;
	
	/**Constructor
	 * Simple constructor for the Agent class
	 * @param coordinate -> int[] : only size 2!
	 */
	Agent(int[] coordinate){
		try{
			if(coordinate.length != 2) { throw new IllegalArgumentException(); }
		}
		catch(IllegalArgumentException iae) {
			System.err.println("INITIATING AGENT: Coordinates should be an integer array of length 2!");
		}
		this.isDead = false;
		this.foundEnd = false;
		this.coordinate = coordinate;
		this.steps = 0;
		this.killCount = 0;
		this.currentStrat = "STOP";
	}
	
	/**
	 * Returns a string dictating the agent's planned move. These could be:
	 * "UP", "DOWN", "RIGHT", "LEFT", and "STOP"
	 * @return String
	 */
	public abstract String strategy(Environment env);
	
	/**
	 * Predicts the position that the agent will be if they make a certain move. This should be used
	 * in the Game Manager to avoid collisions.
	 * @param movement -> String
	 * @return -> int[] : size 2.
	 */
	public int[] predictionAgentPosition(String movement) {
		if(skip) { return this.coordinate.clone(); }
		int[] returnCoordinate = this.coordinate.clone();
		if(movement.equals("RIGHT")) {
			returnCoordinate[1] += 1;
		}
		else if(movement.equals("LEFT")) {
			returnCoordinate[1] -= 1;
		}
		else if(movement.equals("UP")) {
			returnCoordinate[0] -= 1;
		}
		else if(movement.equals("DOWN")) {
			returnCoordinate[0] += 1;
		}
		return returnCoordinate;
	}

	/**
	 * Updates this agent current coordinate to match the instructed move.
	 * CAUTION: this class do not keep track of past positions. That is why, be careful using this method.
	 * @param movement -> String
	 */
	public void updateAgentPosition(String movement) {
		if(skip) {
			return;
		}
		if(movement.equals("RIGHT")) {
			this.coordinate[1] += 1;
			this.steps++;
		}
		else if(movement.equals("LEFT")) {
			this.coordinate[1] -= 1;
			this.steps++;
		}
		else if(movement.equals("UP")) {
			this.coordinate[0] -= 1;
			this.steps++;
		}
		else if(movement.equals("DOWN")) {
			this.coordinate[0] += 1;
			this.steps++;
		}
		return;
	}


	
}
