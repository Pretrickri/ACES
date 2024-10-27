import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class client {

	public static void main(String[] args) {
		int[] size;
		List<Agent> agentList = new ArrayList<>();
		List<Agent> enemyList = new ArrayList<>();
		List<Environment> environmentList = new ArrayList<>();
		
		// Simulation
//		size = new int[] {6,6}; // MAP SIZE
//		agentList.add(new SimpleAgent(new int[] {1,0}, new float[] {20,20,20,20,20}));
//		enemyList.add(new SimpleAgent(new int[] {1,5}, new float[] {20,20,20,20,20}));
//		
//		environmentList.add(new Environment("C:\\Users\\patri\\OneDrive\\Área de Trabalho\\JavaProjects\\ACES\\src\\test6x6.txt",size, agentList, enemyList));
//		
//		agentList.clear();
//		enemyList.clear();
		
		size = new int[] {9,16}; // MAP SIZE
		agentList.add(new SimpleAgent(new int[] {1,6}, new float[] {100,0,0,0,0}));
		agentList.add(new SimpleAgent(new int[] {3,4}, new float[] {25,25,25,25,0}));
		agentList.add(new SimpleAgent(new int[] {6,4}, new float[] {33,12,35,20,0}));
		enemyList.add(new SimpleAgent(new int[] {7,2}, new float[] {25,25,25,25,0}));
		
		environmentList.add(new Environment("C:\\Users\\patri\\OneDrive\\Área de Trabalho\\JavaProjects\\ACES\\src\\test9x16.txt",size, agentList, enemyList));
		
		agentList.clear();
		enemyList.clear();
		
		Manager manager = new Manager(environmentList);
		
		manager.timedRun(10);
		
	}

}
