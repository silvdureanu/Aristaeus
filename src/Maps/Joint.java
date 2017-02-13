package Maps;

public class Joint {
	private Bone nextBone;
	private int dir;
	
	public Joint(Bone b, int d) {
		nextBone = b;
		dir = d;
	}
	
	public Bone getNextBone() {
		return nextBone;
	}
	
	public int getNextDir() {
		return dir;
	}
	
	
	

}
