import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class client {

	public static void main(String[] args) {
		int[] size;
		List<Agent> agentList = new ArrayList<>();
		List<Agent> enemyList = new ArrayList<>();
		List<Environment> list = new ArrayList<>();
		
		// Second Simulation
		size = new int[] {9,16};
		agentList.add(new SimpleAgent(new int[] {5,2}, new float[] {20,20,20,20,20}));
		enemyList.add(new SimpleAgent(new int[] {5,9}, new float[] {20,20,20,20,20}));
		list.add(new Environment("C:\\Users\\patri\\OneDrive\\Área de Trabalho\\JavaProjects\\ACES\\src\\test9x16.txt",size, agentList, enemyList));
		agentList.clear();
		enemyList.clear();
		
		Manager manager = new Manager(list);
		
		manager.timedRun(100);
		
	}

}
