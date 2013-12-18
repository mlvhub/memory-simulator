package simulator.entity;

import java.util.ArrayList;
import java.util.List;

import simulator.util.AbstractGenerator;

public class Page {
	private int pageId;
	private int fileId;
	private int size;
	private int currentSize;
	private List<Character> characters = new ArrayList<Character>(getSize());
	
	public Page(AbstractGenerator generator, int fileId, int size, List<Character> chars) {
		this.pageId = generator.nextId();
		this.fileId = fileId;
		this.size = size;
		this.characters = chars;
		this.currentSize = chars.size();
	}
	
	public int getFileId() {
		return fileId;
	}
	public void setFileId(int fileId) {
		this.fileId = fileId;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	
	public List<Character> getCharacters() {
		return characters;
	}
	public void setCharacters(List<Character> characters) {
		this.characters = characters;
	}
	public int getPageId() {
		return pageId;
	}
	public void setPageId(int pageId) {
		this.pageId = pageId;
	}

	public int getCurrentSize() {
		return currentSize;
	}

	public void setCurrentSize(int currentSize) {
		this.currentSize = currentSize;
	}
	
	
}
