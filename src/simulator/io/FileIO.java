package simulator.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileIO {

	public FileIO() {
		
	}

	public static List<String> readLines(File file) {
		List<String> lines = new ArrayList<String>();
		BufferedReader br = null;
		 
		try {
			String currentLine;
			br = new BufferedReader(new FileReader(file));
			while ((currentLine = br.readLine()) != null) 
				lines.add(currentLine);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return lines;
	}
	
	public static List<String> readWords(File file) {
		return getWords(readLines(file));
	}
	
	public static List<Character> readCharacters(File file) {
		return getChars(readLines(file));
	}
	
	private static List<Character> getChars(List<String> lines) {
		List<Character> chars = new ArrayList<Character>();
		for(String word: getWords(lines)) {
			for(Character c: word.toCharArray())
				chars.add(c);
		}
		return chars;
	}

	private static List<String> getWords(List<String> lines) {
		List<String> words = new ArrayList<String>();
		for(String line: lines)
			for(String word: line.split(" "))
				words.add(word);
		return words;
	}
}
