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
		size = new int[] {3,20};
		agentList.add(new SimpleAgent(new int[] {1,4}, new float[] {0,0,0,0}));
		enemyList.add(new SimpleAgent(new int[] {1,0}, new float[] {0,0,100,0}));
//		enemyList.add(new SimpleAgent(new int[] {1,6}, new float[] {0,0,0,0}));
//		enemyList.add(new SimpleAgent(new int[] {1,8}, new float[] {0,0,0,0}));
		list.add(new Environment("C:\\Users\\patri\\OneDrive\\Área de Trabalho\\JavaProjects\\ACES\\src\\test2_3x20.txt",size, agentList, enemyList));
		agentList.clear();
		enemyList.clear();
		
		Manager manager = new Manager(list);
		
		manager.run(1000);
		
	}

}
