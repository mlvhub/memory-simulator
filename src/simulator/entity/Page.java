package simulator.entity;

import java.util.ArrayList;

public class Page {
	private int fileId;
	private int pageId;
	private Character character;
	private ArrayList<Character>listperpage = new ArrayList<Character>(getSize());
	
	private int size;
	
	public int getFileId() {
		return fileId;
	}
	public void setFileId(int fileId) {
		this.fileId = fileId;
	}
	public Character getCharacter() {
		return character;
	}
	public void setCharacter(Character character) {
		this.character = character;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	
	public ArrayList<Character> getListperpage() {
		return listperpage;
	}
	public void setListperpage(ArrayList<Character> listperpage) {
		this.listperpage = listperpage;
	}
	public int getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}
	
	
}
