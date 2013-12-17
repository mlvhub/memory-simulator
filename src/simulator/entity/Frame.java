package simulator.entity;

public class Frame {
	private Page page = null;
	private int size;
	
	public Frame() {
		
	}
	
	public Frame(Page page) {
		this.page = page;
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
	
	
}
