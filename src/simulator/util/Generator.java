package simulator.util;

public class Generator extends AbstractGenerator{
	private static int currentSharedId = 1;
	private int currentId = 1;
	
	public static int nextSharedId() {
		return currentSharedId++;
	}
	
	public int nextId() {
		return currentId++;
	}
}
