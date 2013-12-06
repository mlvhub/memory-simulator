package simulator;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import simulator.entity.SimulatorFile;

public class Paginator {
	private int memoryAmount;
	private int frameSize;
	private int pageSize;
	private Queue<SimulatorFile> filesWaiting = new LinkedList<SimulatorFile>();
	private List<SimulatorFile> filesInMemory = new ArrayList<SimulatorFile>();
	
	private int freeMemory;
	
	public Paginator(int memoryAmount, int frameSize) {
		this.memoryAmount = memoryAmount;
		this.freeMemory = memoryAmount;
		this.frameSize = frameSize;
		this.pageSize = frameSize;
	}

	public void requestMemory(SimulatorFile file) {
		if(isMemoryFull()) {
			filesWaiting.add(file);
		} else {
			// Ver como representamos asignación de recursos
			filesInMemory.add(file);
			freeMemory -= file.getSize();
		}
	}

	private boolean isMemoryFull() {
		return freeMemory == 0 || freeMemory < frameSize;
	}
	
	
}
