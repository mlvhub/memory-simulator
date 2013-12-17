package simulator;

import simulator.entity.Page;
import simulator.util.Generator;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		Generator gen1 = new Generator();
//		Generator gen2 = new Generator();
//		
//		System.out.println(gen1.nextSharedId());
//		System.out.println(gen2.nextSharedId());
//		System.out.println(gen1.nextSharedId());
//		System.out.println(gen2.nextSharedId());
//		
//		System.out.println(gen1.nextId());
//		System.out.println(gen2.nextId());
//		System.out.println(gen1.nextId());
//		System.out.println(gen2.nextId());
		int i = 0;
		int counter = 0;
		while(i < 10) {
			if (counter == 4) {
				counter = 0;
				System.out.println("Reseteo");
			} else if (i + 1 == 10) {
				System.out.println("Agrego sobras");
				i++;
			} else {
				System.out.println("Agrego char");
				counter++;
				i++;
			}
		}
	}

}
