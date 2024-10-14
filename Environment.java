import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Environment {
	public List<Agent> agentList;
	public List<Agent> enemyList;
	private char[][] map;
	private char agentCode;
	private char foodCode;
	private char obstacleCode;
	private char finalCode;
	private char enemyCode;
	
	/**
	 * General constructor for Environment Class
	 * @param path -> String : valid path to txt file
	 * @param agents -> List<Agent>
	 * @param enemies -> List<Agent>
	 * @throws IllegalArgumentException
	 * @throws FileNotFoundException
	 */
	public Environment(String path, int[] mapSize, List<Agent> agents, List<Agent> enemies) throws IllegalArgumentException{ // -> This should import files
		map = new char[mapSize[0]][mapSize[1]];
		agentCode = 'I';
		foodCode = '$';
		finalCode = 'F';
		obstacleCode = 'O';
		enemyCode = 'E';
		this.agentList = new ArrayList<Agent>(agents);
		this.enemyList = new ArrayList<Agent>(enemies);
		try {
			buildMap(path);
		} catch (FileNotFoundException fnfe) {
			System.err.println("File Not Found!");
			fnfe.printStackTrace();
		}
	}
	/**
	 * Specific constructor for Environment Class
	 * @param path -> String : valid path to txt file
	 * @param agents -> List<Agents>
	 * @param enemies -> List<Agent>
	 * @param codes -> char[] : [agent code, food code, obstacle code, end code, enemy code]
	 * @throws IllegalArgumentException
	 * @throws FileNotFoundException
	 */
	public Environment(String path, int[] mapSize, List<Agent> agents, List<Agent> enemies, char[] codes) throws IllegalArgumentException {
		map = new char[mapSize[0]][mapSize[1]];
		this.agentCode = codes[0];
		this.foodCode = codes[1];
		this.obstacleCode = codes[2];
		this.finalCode = codes[3];
		this.enemyCode = codes[4]; 
		this.agentList = new ArrayList<Agent>(agents);
		this.enemyList = new ArrayList<Agent>(enemies);
		try {
			buildMap(path);
		} catch (FileNotFoundException fnfe) {
			System.err.println("File Not Found!");
			fnfe.printStackTrace();
		}
	}
	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//											METHODS															  //
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Builds a map out of an input text file.
	 * @param path -> String : what path to get to the file
	 * @throws FileNotFoundException
	 */
	private void buildMap(String path) throws FileNotFoundException{
		try (BufferedReader file = new BufferedReader(new FileReader(path))) {
			String line = new String();
			int i = 0;
			while((line = file.readLine()) != null) {
				map[i] = line.toCharArray();
				i++;
			}
			if(map.length == 0) throw new IllegalArgumentException();
			if(this.agentList != null) buildAgents(this.agentList, "agent");
			if(this.enemyList != null) buildAgents(this.enemyList, "enemy");
			
		} catch (IOException ioe) {
			System.err.println("File reading went Wrong!\n"
					+ "Check if this is the correct path!");
			ioe.printStackTrace();
		} catch (IllegalArgumentException iae) {
			System.err.println("Invalid Map!");
			iae.printStackTrace();
		}
	}
	
	/**
	 * Builds the agents in the map (agents or enemies)
	 * @param list -> List<Agent>
	 * @param type -> String : either "agent" or "enemy"
	 */
	private void buildAgents(List<Agent> list, String type) {
		for(Agent a : list) {
			if(this.isObstacle(a.coordinate) || this.isFinal(a.coordinate)) { 
				System.err.println("Agent of coordinates " + a.coordinate[0] + ", " + a.coordinate[1] + " is overlapping with an obstacle!");
				a.skip = true;
				continue;
			}
			if(this.isFinal(a.coordinate)) {
				System.err.println("Agent of coordinates " + a.coordinate[0] + ", " + a.coordinate[1] + " is overlapping with an end goal!");
				a.skip = true;
				continue;
			}
			if(this.isAgent(a.coordinate)) {
				System.err.println("Agent of coordinates " + a.coordinate[0] + ", " + a.coordinate[1] + " is overlapping with an Agent!");
				a.skip = true;
				continue;
			}
			if(this.isEnemy(a.coordinate)) {
				System.err.println("Agent of coordinates " + a.coordinate[0] + ", " + a.coordinate[1] + " is overlapping with an Enemy!");
				a.skip = true;
				continue;
			}
			if(type.toLowerCase().equals("agent")) { this.substituteWith(a.coordinate, agentCode); }
			if(type.toLowerCase().equals("enemy")) { this.substituteWith(a.coordinate, enemyCode); }
		}
	}
	
	/**
	 * Updates map with agents' new coordinates.
	 * \n THERES A PROBLEM:
	 * 		Essentially, the decision of movement from both the agent and the enemy should be done at the same time
	 * 		in order to avoid preferences. How? Every agent (and enemy) should receive a global variable "currentStrat"
	 * 		which holds the strategy per iteration. In the beginning of updateMap(), all agents (and enemies) will generate
	 * 		a strat based on the current state of the map and only later they will update their positions simultaneously. 
	 * 		Right now this is not a problem, but it will be problematic when the agents start reacting based on inputs from
	 * 		their environment. OBS: the prediction and update agent position methods from the Agent class are NOT the problem
	 * 		and should probably not need to be modified. Instead, the problem lies in the strategy, which has to be done before
	 * 		and MAP modification. In other words, it is safer to create the strategy right in the beginning of the method.
	 * 
	 * @param agents -> List<Agent>
	 */
	public void updateMap() {
		// ENEMY UPDATE
		for(Agent e : this.enemyList) {
			if(e.skip) {continue;}
			String strat = e.strategy(this);
			//System.out.println(strat); ////////////////////////////DEBUG
			int[] tempPrediction = e.predictionAgentPosition(strat);
			if(this.isObstacle(tempPrediction) || this.isEnemy(tempPrediction) || this.isFinal(tempPrediction)) {continue;}
			this.substituteWith(e.coordinate, ' ');
			
			e.updateAgentPosition(strat);
			// THESE IF AND ELSES UPDATE THE MAP
			if(this.isFood(e.coordinate)) {
				e.foodCount += 1;
				this.substituteWith(e.coordinate, agentCode);
			}
			else if(this.isAgent(e.coordinate)) {
				e.killCount += 1;
				this.substituteWith(e.coordinate, enemyCode);
			}
			else {
				this.substituteWith(e.coordinate, enemyCode);
			}
			
		}
		
		// AGENT UPDATE
		for(Agent a : this.agentList) {
			if(a.skip) { continue; }
			if(this.isEnemy(a.coordinate)) { // in case the enemy ran into an agent.
				a.skip = true;
				a.isDead = true;
			}
			
			String strat = a.strategy(this);
			//System.out.println(strat); ////////////////////////////DEBUG
			int[] tempPrediction = a.predictionAgentPosition(strat);
			if(this.isObstacle(tempPrediction) || this.isAgent(tempPrediction)) { continue; }
			this.substituteWith(a.coordinate, ' ');
			
			a.updateAgentPosition(strat);
			// THESE IF AND ELSES UPDATE THE MAP
			if(this.isFinal(a.coordinate)) {
				a.skip = true;
				a.foundEnd = true;
			}
			else if(this.isEnemy(a.coordinate)) { // in case the agent ran into an enemy lol
				Agent enemy = (Agent) this.findObjectAt(a.coordinate); //This should be safe because of the isEnemy check!
				enemy.killCount++;
				a.skip = true;
				a.isDead = true;
			}
			else if(this.isFood(a.coordinate)) {
				a.foodCount += 1;
				this.substituteWith(a.coordinate, agentCode);
			}
			else {
				this.substituteWith(a.coordinate, agentCode);
			}
		}
		
	}
	
	/**
	 * Prints the current map.
	 */
	public void printMap() {
		// Caution: this following method clears the terminal
		System.out.print("\033c");
		///////////////////////////////////
		String printString = new String();
		for(char[] arr : map) {
			printString += new String(arr);
			printString += "\n";
		}
		System.out.print(printString);
	}
	
	/**
	 * Updates and prints map.
	 * @param newAgents -> List<Agents>
	 */
	public void master() {
		updateMap();
		printMap();
	}
	
	/**
	 * Finds the character at a certain coordinate and substitute it on the map with another character.
	 * @param coordinate -> int[] with two elements
	 * @param subs -> char : what will be substituted with
	 * @return boolean
	 */
	private boolean substituteWith(int[] coordinate, char subs) throws IllegalArgumentException{
		if(coordinate.length != 2) { throw new IllegalArgumentException("Wrong Input Size");}
		try{ map[coordinate[0]][coordinate[1]] = subs; }
		catch(ArrayIndexOutOfBoundsException aiobe) { return false; }
		return true;
	}
	
	/**
	 * Returns the character in the map at a specific coordinate; returns '/' if coordinate is out of bounds
	 * @param coordinate -> int[] : size 2!
	 * @return -> char
	 */
	public char findCharAt(int[] coordinate) {
		if(coordinate.length != 2) { throw new IllegalArgumentException("Wrong Input Size"); }
		try{
			char test = map[coordinate[0]][coordinate[1]];
			return test;
		}
		catch(ArrayIndexOutOfBoundsException aiobe) { return '/'; }
	}
	
	/**
	 * Checks if the object in the given coordinate is an enemy.
	 * @param coordinate -> int[] : size 2!
	 * @return -> boolean
	 * @throws IllegalArgumentException
	 */
	public boolean isEnemy(int[] coordinate) throws IllegalArgumentException{
		return (findCharAt(coordinate) == enemyCode);
	}
	
	/**
	 * General searcher for objects
	 * @param coordinate -> int[] : size 2
	 * @return -> Object : the object at position
	 * @throws IllegalArgumentException
	 */
	public Object findObjectAt(int[] coordinate) throws IllegalArgumentException{
		char type = map[coordinate[0]][coordinate[1]];
		// AGENT OBJECT
		if (type == agentCode) {
			for(Agent a : this.agentList) {
				if(a.skip) {continue;}
				if((a.coordinate[0] == coordinate[0]) && (a.coordinate[1] == coordinate[1])){
					return a;
				}
			}
		}
		// ENEMY OBJECT
		else if (type == enemyCode) {
			for(Agent e : this.enemyList) {
				if(e.skip) {continue;} //currently we don't need this but it's good practice ig
				if((e.coordinate[0] == coordinate[0]) && (e.coordinate[1] == coordinate[1])) {
					return e;
				}
			}
		}
		return null;
		
	}
	
	/**
	 * Checks if the object in the given coordinate is an agent.
	 * @param coordinate -> int[] : size 2!
	 * @return -> boolean
	 * @throws IllegalArgumentException
	 */
	public boolean isAgent(int[] coordinate) throws IllegalArgumentException{
		return (findCharAt(coordinate) == agentCode);
	}
	
	/**
	 * Checks if the object in the given coordinate is an obstacle.
	 * @param coordinate -> int[] : size 2!
	 * @return -> boolean
	 * @throws IllegalArgumentException
	 */
	public boolean isObstacle(int[] coordinate) throws IllegalArgumentException{
		char find = findCharAt(coordinate);
		return (find == obstacleCode || find == '/');
	}
	
	/**
	 * Checks if the object in the given coordinate is food.
	 * @param coordinate -> int[] : size 2!
	 * @return -> boolean
	 * @throws IllegalArgumentException
	 */
	boolean isFood(int[] coordinate) throws IllegalArgumentException{
		return (findCharAt(coordinate) == foodCode);
	}
	
	/**
	 * Checks if the object in the given coordinate is the final destination.
	 * @param coordinate -> int[] : size 2!
	 * @return -> boolean
	 * @throws IllegalArgumentException
	 */
	boolean isFinal(int[] coordinate) throws IllegalArgumentException{
		return (findCharAt(coordinate) == finalCode);
	}
	
	/**
	 * Return the index of all agents that have reached some sort of event 
	 * that prevented them on continuing on the simulation. This should be
	 * used in the manager to keep track of the iterations.
	 * @return
	 */
	public List<Agent> findAgentsAtEnd() { 
		List<Agent> returnList = new ArrayList<>();
		for(Agent a : this.agentList) {
			if(a.skip) returnList.add(a); 
		}
		return returnList;
	}
	
	/**
	 * Prints the general information of all agents and enemies that are or were in the Environment.
	 */
	public void printAllAgents() {
		System.out.println("PRINTING ALL AGENTS\n");
		int i = 0;
		for(Agent a : agentList) {
			System.out.println("Agent of Index " + i + "\n"
					+ "Coordinates = ["+a.coordinate[0]+","+a.coordinate[1]+"]\n"
					+ "Reached End = "+a.foundEnd+"\n"
					+ "Died = "+a.isDead+"\n"
					+ "Food Count = "+a.foodCount+"\n"
					+ "Number of Steps = "+a.steps+"\n");
			i++;
		}
		i=0;
		for(Agent e : enemyList) {
			System.out.println("Enemy of Index " + i + "\n"
					+ "Coordinates = ["+e.coordinate[0]+","+e.coordinate[1]+"]\n"
					+ "Kill Count = "+e.killCount+"\n"
					+ "Food Count = "+e.foodCount+"\n"
					+ "Number of Steps = "+e.steps+"\n");
			i++;
		}
	}
}
