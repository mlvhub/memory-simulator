package simulator;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import simulator.entity.Frame;
import simulator.entity.Page;
import simulator.entity.SimulatorFile;
import simulator.util.AbstractGenerator;
import simulator.util.Generator;

public class Paginator {
	private int memoryAmount;
	private int frameSize;
	private int frameQuantity;
	private int pageSize;
	private int internalFragmentation;
	private Frame[][] frames;
	private List<Point> freeFramesCoordinates;
	private Queue<SimulatorFile> filesWaiting = new LinkedList<SimulatorFile>();
	private List<SimulatorFile> filesInMemory = new ArrayList<SimulatorFile>();

	private int freeMemory;
	private List<SimulatorFile> filesToPaginate;

	public Paginator(int memoryAmount, int frameSize,
			List<SimulatorFile> filesToPaginate) {
		this.memoryAmount = memoryAmount;
		this.freeMemory = memoryAmount;
		this.frameSize = frameSize;
		this.pageSize = frameSize;
		this.filesToPaginate = filesToPaginate;
		this.frameQuantity = (int) Math.ceil(memoryAmount / (double) frameSize);
		this.frames = createBidimensionalArray(frameQuantity);
		this.freeFramesCoordinates = calculateFreeFramesCoordinates(frames);
	}

	private List<Point> calculateFreeFramesCoordinates(Frame[][] frames) {
		List<Point> freeFramesCoordinates = new ArrayList<Point>();
		for (int x = 0; x < frames.length; x++)
			for (int y = 0; y < frames[0].length; y++)
				if (frames[x][y].isFree())
					freeFramesCoordinates.add(new Point(x, y));
		return freeFramesCoordinates;
	}

	public Point getFreeFrameCoordinate() {
		int index = new Random().nextInt(freeFramesCoordinates.size());
		return freeFramesCoordinates.get(index);
	}

	public Frame[][] createBidimensionalArray(int quantity) {
		AbstractGenerator generator = new Generator();
		int cols = 10;
		int rows = (int) Math.ceil(quantity / (double) cols);
		Frame[][] initFrames = new Frame[cols][rows];
		for (int i = 0; i < cols; i++)
			for (int j = 0; j < rows; j++)
				initFrames[i][j] = new Frame(generator, frameSize);
		return initFrames;
	}

	public void requestMemory(SimulatorFile file, List<Page> pages) {
		if (isMemoryFull()) {
			filesWaiting.add(file);
		} else {
			// Asigno
			for (Page page : pages)
				loadPage(page);
			filesInMemory.add(file);
			setFreeMemory(getFreeMemory()-file.getSize());
		}
	}

	private void log(SimulatorFile file) {
		StringBuffer msg = new StringBuffer("Tengo un archivo con: ");
		for (Character ch : file.getChars()) {
			msg.append(ch + ", ");
		}
		System.out.println(msg.toString() + "\n");
	}

	private void log(List<Page> pages) {
		StringBuffer msg = new StringBuffer();
		for (Page page : pages) {
			msg.append("Tengo una pagina con: ");
			for (Character ch : page.getCharacters())
				msg.append(ch + ", ");
			msg.append("\n");
		}
		System.out.println(msg.toString());
	}

	public void loadPage(Page page) {
		Point freeFrameCoord = getFreeFrameCoordinate();
		frames[freeFrameCoord.x][freeFrameCoord.y].setPage(page);
		freeFramesCoordinates.remove(freeFrameCoord);
		setFreeMemory(getFreeMemory()-page.getCurrentSize());
		internalFragmentation += page.getInternalFragmentation();
	}

	private boolean isMemoryFull() {
		return freeMemory == 0 || freeMemory < frameSize;
	}

	public void run() {
		for (SimulatorFile file : filesToPaginate){
			List<Page> pagesPerFile = paginate(file);
			requestMemory(file, pagesPerFile);
		}
	}

	public void stop() {
		// TODO
	}
	
	public int getInternalFragmentation() {
		return internalFragmentation;
	}

	public List<Page> paginate(SimulatorFile file) {
		List<Page> pagesPerFile = new ArrayList<Page>();
		Generator genPerFile = new Generator();
		
		while(file.hasCharsToExtract()) {
			List<Character> extractedChars = file.extractChars(frameSize);
			Page page = new Page(genPerFile, file.getId(), frameSize, extractedChars);
			pagesPerFile.add(page);
		}
		return pagesPerFile;
	}

	
	public Frame[][] getFrames() {
		return frames;
	}

	public List<Point> getFreeFramesCoordinates() {
		return freeFramesCoordinates;
	}

	public void setFreeFramesCoordinates(List<Point> freeFramesCoordinates) {
		this.freeFramesCoordinates = freeFramesCoordinates;
	}

	public int getFreeMemory() {
		return freeMemory;
	}

	public void setFreeMemory(int freeMemory) {
		this.freeMemory = freeMemory;
	}
	
	public int totalFreeMemory(){
		setFreeMemory(freeFramesCoordinates.size()*pageSize);
		return getFreeMemory();
	}
	
	

}
