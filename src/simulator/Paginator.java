package simulator;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import simulator.entity.SimulatorFile;

public class Paginator {
	private int memoryAmount;
	private int frameSize;
	private Queue<SimulatorFile> filesWaiting = new LinkedList<SimulatorFile>();
	
	Character[] memory = new Character[memoryAmount];
	private int freeMemory;
	
	public Paginator(int memoryAmount, int frameSize) {
		this.memoryAmount = memoryAmount;
		this.frameSize = frameSize;
	}

	public void requestMemory(SimulatorFile file) {
		if(isMemoryFull()) {
			filesWaiting.add(file);
		}
	}

	private boolean isMemoryFull() {
		return freeMemory == 0 || freeMemory < frameSize;
	}
	
	
}
