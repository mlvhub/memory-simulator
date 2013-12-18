package simulator.entity;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import simulator.util.AbstractGenerator;

public class SimulatorFile {

	private int id;
	private File file;
	private List<Character> chars;
	private Queue<Character> charsToExtract;
	// lifespan en milisegundos
	private int size;
	private int lifespan;

	public SimulatorFile(AbstractGenerator generator, File file, List<Character> chars) {
		this.id = generator.nextId();
		this.file = file;
		this.chars = chars;
		this.charsToExtract = new LinkedList<Character>(chars);
		// Es para siempre
		this.lifespan = 0;
		this.size = chars.size();
	}
	
	public SimulatorFile(AbstractGenerator generator, File file, List<Character> chars, int lifespan) {
		this.id = generator.nextId();
		this.file = file;
		this.chars = chars;
		this.charsToExtract = new LinkedList<Character>(chars);
		// Es para siempre
		this.lifespan = lifespan;
		this.size = chars.size();
	}
	
	public List<Character> extractChars(int quantityToExtract) {
		List<Character> chars = new ArrayList<Character>();
		int i = 0;
		while(hasCharsToExtract() && i++ < quantityToExtract)
			chars.add(charsToExtract.poll());
 		return chars;
	}
	
	public boolean hasCharsToExtract() {
		return charsToExtract.size() > 0;
	}
	
	public String getName() {
		return this.file.getName();
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
