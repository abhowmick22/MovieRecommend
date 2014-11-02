package lda;

import java.util.List;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

/*
 * This module implements all the distributions, optimisations,
 * and other math utility functions.
 */

public class Utilities {
	
	// Evaluate the stationary point of a function using Newton-Raphson method,
	// assuming the form of the likelihood function is known (thus, it is not general)
	// In LDA, this is used to maximize the loer bound of log L
	// wrt \alpha and \beta in the M-step
	public List<Double> optimizeNR(List<Double > init){
		return null;
	}
	
	
	// This method evaluates the gradient of a vector
	public List<Double> gradient(List<Double> point){
		return null;
	}
	
	
	// This method evaluates the Hessian of a vector
	public List<List<Double> > hessian(List<Double> point){
		return null;
	}
	
	// This method evaluates the psi function -
	// This is the first derivative of the log gamma function
	public double psi(double input){
		return 0;
	}
	
	// This method returns the column-wise sum of a 2D array double[][]
	public RealVector matSumByCol(RealMatrix input){
		double[][] data = input.getData();
		return null;
	}
	
	
}
