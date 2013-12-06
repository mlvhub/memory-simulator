package simulator.util;

public class Generator {
	private static int currentId = 1;
	
	public static int nextId() {
		return currentId++;
	}
}
