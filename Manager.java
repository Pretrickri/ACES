import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;

public class Manager {
// I should have a method to set all the global variables so it is reset every iteration
	private List<Agent> agentList; // help with iteration by deletion of each individual environment
	private List<Environment> environments; // all environments it will run at once.
	
	Manager(List<Environment> environments){
		this.environments = environments;
	}
	
	/**
	 * Runs all the environments initiated in Manager.
	 * @param waitTime -> int : amount of milliseconds between each iteration.
	 */
	public void run(int waitTime) {
		int i = 1;
		for(Environment env : environments) {
			System.out.println("\n///////////////////////\n "
					+ "Simulation " + i + ":");
			List<Agent> agentList = new LinkedList<>(env.agentList);
			while(!agentList.isEmpty()) {
				env.printMap();
				env.updateMap();

				//env.printAllAgents(); // Printing for EACH iteration
				
				List<Agent> skippedAgents = env.findAgentsAtEnd();
				for(Agent a : skippedAgents) { agentList.remove(a); } // This does not remove the agents from the environment!
				try {
					Thread.sleep(waitTime);
				}catch(InterruptedException ie) {
					System.err.println("THREAD.SLEEP PROBLEM");
					ie.printStackTrace();
				}
			}
			env.printAllAgents(); //Printing at the END
			i++;
		}
	}
}
