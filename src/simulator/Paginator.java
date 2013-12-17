package simulator;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import simulator.entity.Frame;
import simulator.entity.Page;
import simulator.entity.SimulatorFile;
import simulator.io.FileIO;
import simulator.util.AbstractGenerator;
import simulator.util.Generator;

public class Paginator {
	private int memoryAmount;
	private int frameSize;
	private int frameQuantity;
	private int pageSize;
	private ArrayList<Frame> frame = new ArrayList<Frame>();
	private Page page = new Page();
	private FileIO fileIO = new FileIO();
	private Queue<SimulatorFile> filesWaiting = new LinkedList<SimulatorFile>();
	private List<SimulatorFile> filesInMemory = new ArrayList<SimulatorFile>();
	
	private int freeMemory;
	private List<SimulatorFile> filesToPaginate;
	
	public Paginator(int memoryAmount, int frameSize, List<SimulatorFile> filesToPaginate) {
		this.memoryAmount = memoryAmount;
		this.freeMemory = memoryAmount;
		this.frameSize = frameSize;
		this.pageSize = frameSize;
		this.filesToPaginate = filesToPaginate;
		this.frameQuantity = memoryAmount / frameSize; 
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

	public void run() {
		// TODO Auto-generated method stub
		
	}

	public void stop() {
		// TODO Auto-generated method stub
		
	}
	
	public void paginar(File file){
		AbstractGenerator generator = null;
		ArrayList<Character>total = new ArrayList<Character>();
		while(fileIO.readCharacters(file) != null){
		total.addAll(fileIO.readCharacters(file));
	}
		for(Character cha : total){
			for(int i=0; i<pageSize; i++){
				if(i<pageSize)
					page.getListperpage().add(cha);
				else{
					Page page = new Page();
					i=0;
					generator.nextId();
				}
				SimulatorFile sim = new SimulatorFile(generator, file, page.getListperpage());
				requestMemory(sim);
			}
		}
		}
	
	
}
