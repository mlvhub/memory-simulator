package simulator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import simulator.entity.SimulatorFile;
import simulator.gui.SimulatorGUI;
import simulator.io.FileIO;

public class Main {


	public static void main(String[] args) {
		
		//Tiene que ser configurable
		int MEMORY_AMOUNT = 40;
		int FRAME_SIZE = 2;
		
		File file = new File("/home/mlopez/Desktop/helloworld.txt");
		List<Character> chars = FileIO.readCharacters(file);
		SimulatorFile simFile = new SimulatorFile(file, chars, 5000);
		
		Paginator paginator = new Paginator(MEMORY_AMOUNT, FRAME_SIZE);
		paginator.requestMemory(simFile);
		
		 SimulatorGUI view = new SimulatorGUI();
		 view.setVisible(true);
	}
	

}
