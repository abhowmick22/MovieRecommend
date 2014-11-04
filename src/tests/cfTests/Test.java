package tests.cfTests;
import main.collabfilter.*;

public class Test {

	public static void main(String[] args) {

		for (int i=0; i<1; i++) {
			CollaborativeFiltering.main(new String[]{"r" + (i+1) + ".train", "r" + (i+1) + ".test"});
		}
		
	}

}
