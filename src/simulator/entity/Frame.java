package simulator.entity;

import simulator.util.AbstractGenerator;

public class Frame {
	private int frameId;
	private Page page = null;
	private int size;
	private int currentSize;
	
	public Frame(AbstractGenerator generator, int size) {
		this.frameId = generator.nextId();
		this.size = size;
		this.currentSize = size;
		
	}
	
	public Frame(AbstractGenerator generator, int size, Page page) {
		this.frameId = generator.nextId();
		this.page = page;
		this.size = size;
		this.currentSize = page.getCurrentSize();
	}
	
	public String getInfo() {
		return (page != null) ?  "F"+page.getFileId()+"-P"+page.getPageId() : "";
	}
	
	public boolean isUsed() {
		return !isFree();
	}
	
	public boolean isFree() {
		return page == null;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getCurrentSize() {
		return currentSize;
	}

	public void setCurrentSize(int currentSize) {
		this.currentSize = currentSize;
	}
	
	
}
