import java.util.ArrayList;
import java.util.List;

public abstract class PresenceMap {
	public boolean[][] map;
	public char[][] ogMapReference;
	private int[] mapSize;
	private List<int[]> objCoordinates;
	private char code;
	
	PresenceMap(Environment env, int[] mapSize, char code){
		this.ogMapReference = env.map;
		this.map = new boolean[mapSize[0]][mapSize[1]];
		this.mapSize = mapSize.clone();
		this.objCoordinates = new ArrayList<int[]>();
		this.code = code;
		buildMap();
	}
	
	public void buildMap() {
		for(int i = 0; i<this.mapSize[0]; i++) {
			for(int j = 0; j<this.mapSize[0]; j++) {
				if(ogMapReference[i][j] == code) {
					buildObj(new int[] {i,j});
				}
			}
		}
	}
	
	public void buildObj(int[] coord) {
		for(int i = -2; i<=2; i++) {
			for(int j = -2; j<=2; j++) {
				map[coord[0]+i][coord[1]+j] = true;
				this.objCoordinates.add(coord); //my question is, if the obj moves, will this reference also change?
			}
		}
	}
	
	public void clearObj(int[] coord) {
		for(int i = -2; i<=2; i++) {
			for(int j = -2; j<=2; j++) {
				map[coord[0]+i][coord[1]+j] = false;
			}
		}
	}
}
