package simulator;

import java.awt.Point;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import simulator.entity.Frame;
import simulator.entity.Page;
import simulator.entity.SimulatorFile;
import simulator.io.FileIO;
import simulator.util.AbstractGenerator;

public class Paginator {
	private int memoryAmount;
	private int frameSize;
	private int frameQuantity;
	private int pageSize;
	private ArrayList<Frame> frame = new ArrayList<Frame>();
	private Page page = new Page();
	private FileIO fileIO = new FileIO();
	private Frame[][] frames;
	private List<Point> freeFramesCoordinates;
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
		this.frameQuantity = (int) Math.ceil( memoryAmount / (double)frameSize ); 
		this.frames = createBidimensionalArray(frameQuantity);
		this.freeFramesCoordinates = calculateFreeFramesCoordinates(frames);
	}
	
	private List<Point> calculateFreeFramesCoordinates(Frame[][] frames) {
		List<Point> freeFramesCoordinates = new ArrayList<Point>();
		for(int x = 0; x < frames.length; x++)
			for(int y = 0; y < frames[0].length; y++)
				if (frames[x][y].isFree())
					freeFramesCoordinates.add(new Point(x, y));
		return freeFramesCoordinates;
	}
	public Point getFreeFrameCoordinate() {
		int index = new Random().nextInt(freeFramesCoordinates.size());
		return freeFramesCoordinates.get(index);
	}

	public Frame[][] createBidimensionalArray(int quantity) {
		int cols = 60;
		int rows = (int) Math.ceil( quantity / (double)cols );
		Frame[][] initFrames = new Frame[cols][rows];
		for(int i = 0; i < cols; i++)
			for(int j = 0; j < rows; j++) 
					initFrames[i][j] = new Frame();
		return initFrames;
	}

	public void requestMemory(SimulatorFile file) {
		if(isMemoryFull()) {
			filesWaiting.add(file);
		} else {
			// Agarro una coordenada de un marco libre
			Point freeFrameCoord = getFreeFrameCoordinate();
			// Asigno
			//frames[freeFrameCoord.x][freeFrameCoord.y] = 
			// Lo borro porque ya está ocupado
			freeFramesCoordinates.remove(freeFrameCoord);
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

	public Frame[][] getFrames() {
		return frames;
	}
	
	
}
