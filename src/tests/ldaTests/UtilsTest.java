package tests.ldaTests;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

import main.lda.Configs;
import main.lda.Utilities;

public class UtilsTest {
	
	/************************************************************/
	public static void main(String[] args){
	
	// Debugging Utilities and other functionalities
	Utilities utils = new Utilities();
	
	// Debugging NR solver
	int nbrTopics = 10;
	int nbrDocuments = 5;
	
	Configs configs = new Configs();
	RealVector initAlpha = new ArrayRealVector(nbrTopics);
	List<RealVector> gamma = new ArrayList<RealVector>();
	
	// Randomly assigning values to initAlpha and gamma
	Random rand = new Random();
	
	for(int i = 0; i < nbrTopics; i++)
		initAlpha.setEntry(i, rand.nextDouble());
	
	for(int i = 0; i < nbrDocuments; i++){
		RealVector documentGamma = new ArrayRealVector(nbrTopics);
		for(int j = 0; j < nbrTopics; j++)
			documentGamma.setEntry(j, rand.nextDouble());
		
		gamma.add(documentGamma);
	}
	
	RealVector nextAlpha = utils.performNR(configs, initAlpha, gamma);
	System.out.println(nextAlpha);
	System.out.println(initAlpha);
	
	// Debugging each step
	RealVector hessianDiag = new ArrayRealVector(new double[]{1, 2, 3, 4, 5, 6});
	RealVector gradient = new ArrayRealVector(new double[]{6, 5, 4, 3, 2, 1});
	double hessianConst = 4.0;
	RealVector nrStep = utils.computeNRStep(hessianDiag, hessianConst, gradient);
	System.out.println(nrStep);

	/************************************************************/
	
	}
}
