package simulator.entity;

import java.io.File;
import java.util.List;

public class SimulatorFile {

	private int id;
	private File file;
	private List<Character> chars;
	// lifespan en milisegundos
	private int size;
	private int lifespan;

	public SimulatorFile(int id, File file, List<Character> chars) {
		this.id = id;
		this.file = file;
		this.chars = chars;
		// Es para siempre
		this.lifespan = 0;
		this.size = chars.size();
	}
	
	public SimulatorFile(int id, File file, List<Character> chars, int lifespan) {
		this.id = id;
		this.file = file;
		this.chars = chars;
		this.lifespan = lifespan;
		this.size = chars.size();
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public List<Character> getChars() {
		return chars;
	}

	public void setChars(List<Character> chars) {
		this.chars = chars;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getLifespan() {
		return lifespan;
	}

	public void setLifespan(int lifespan) {
		this.lifespan = lifespan;
	}
	
}
