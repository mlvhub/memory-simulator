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
import simulator.io.FileIO;
import simulator.util.Generator;

public class Paginator {
	private int memoryAmount;
	private int frameSize;
	private int frameQuantity;
	private int pageSize;
	private ArrayList<Frame> frame = new ArrayList<Frame>();
	private ArrayList<Page> pageList = new ArrayList<Page>();
	private FileIO fileIO = new FileIO();
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
		int cols = 60;
		int rows = (int) Math.ceil(quantity / (double) cols);
		Frame[][] initFrames = new Frame[cols][rows];
		for (int i = 0; i < cols; i++)
			for (int j = 0; j < rows; j++)
				initFrames[i][j] = new Frame();
		return initFrames;
	}

	public void requestMemory(SimulatorFile file, List<Page> pages) {
		if (isMemoryFull()) {
			filesWaiting.add(file);
		} else {
			// Asigno
			for(Page page: pages)
				loadPage(page);
			filesInMemory.add(file);
			freeMemory -= file.getSize();
		}
	}
	
	private void loadPage(Page page) {
		Point freeFrameCoord = getFreeFrameCoordinate();
		frames[freeFrameCoord.x][freeFrameCoord.y].setPage(page);
		freeFramesCoordinates.remove(freeFrameCoord);
	}

	private boolean isMemoryFull() {
		return freeMemory == 0 || freeMemory < frameSize;
	}

	public void run() {
		paginar();
	}

	public void stop() {
		// TODO
	}
	
	public void paginate() {
		for (SimulatorFile sf: filesToPaginate) {
			List<Page> pagesPerFile = new ArrayList<Page>();
			Generator genPerFile = new Generator();
			Page page = new Page(genPerFile);
				
			for (Character ch : sf.getChars()) {
				for (int i = 0; i < pageSize; i++) {
					if (i < pageSize)
						page.getCharacters().add(ch);
					else {
						pagesPerFile.add(page);
						page = new Page(genPerFile);
						i = 0;
					}
				}
			}
			
			requestMemory(sf, pagesPerFile);
 		}
	}

	/*
	public void paginar() {
		int counter = 0;
		for (SimulatorFile sf : filesToPaginate) {
			for (Character ch : sf.getChars()) {
				for (int i = 0; i < pageSize; i++) {
					if (i < pageSize)
						page.getCharacters().add(ch);
					else {
						//page.setPageNumber(counter);
						pageList.add(page);
						page = new Page(new Generator());
						i = 0;
						counter++;
					}
				}
			}
			counter = 0;
			
		}
	} */

	public Frame[][] getFrames() {
		return frames;
	}

}
