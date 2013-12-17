package simulator.entity;

import java.util.ArrayList;

import simulator.util.AbstractGenerator;

public class Page {
	private int pageId;
	private int fileId;
	private ArrayList<Character> characters = new ArrayList<Character>(getSize());
	
	public Page(AbstractGenerator generator) {
		this.pageId = generator.nextId();
	}
	
	private int size;
	
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
	
	public ArrayList<Character> getCharacters() {
		return characters;
	}
	public void setCharacters(ArrayList<Character> characters) {
		this.characters = characters;
	}
	public int getPageId() {
		return pageId;
	}
	public void setPageId(int pageId) {
		this.pageId = pageId;
	}
	
	
}
