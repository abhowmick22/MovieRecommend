package lda;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
// For Gamma functions
import org.apache.commons.math3.special.Gamma;

/*
 * This module implements all the distributions, optimisations,
 * and other math utility functions.
 */

public class Utilities {
	// Evaluate the stationary point of a function using Newton-Raphson method,
	// assuming the form of the likelihood function is known (thus, it is not general)
	// In LDA, this is used to maximize the lower bound of log L, (i.e. function is known)
	// wrt \alpha in the M-step
	public RealVector optimizeNR(RealVector initAlpha){
		return null;
	}
	
	// This method evaluates the gradient of a vector, dy/dx
	public RealVector gradient(double x, RealVector y){
		return null;
	}
	
	// This method evaluates the Hessian of a vector
	public RealMatrix hessian(double x, RealVector y){
		return null;
	}
	
	// Returns the product of gradient and Hessian for special matrices
	// Form of special hessian : H = diag(h) + \vec(1) * z * \vec(1)t
	// where h is any vector
	// Takes vector h, g and scalar z as inputs
	// Returns the product of Hessian Inverse and gradient for the update in Newton Raphson as one vector
	public RealVector computeNRStep(RealVector hessianDiag, double hessianConst, RealVector gradient){
		
		// (inv(H) * g)_i = (g_i - c) / h_i
		// where c = (sum_{j=1}^k g_j/h_j)/(inv(z) + sum_{j=1}^k inv(h_j))  
		
		double numSum = 0, denSum = 0, constantFactor; //
		int dimensions = hessianDiag.getDimension();
		
		// results
		RealVector product = new ArrayRealVector();
		
		//Throwing exception in case the dimensions don't match
		try{
			if(gradient.getDimension() != dimensions)
				throw new IllegalArgumentException("Both gradient and diagonal vector of hessian must be of same dimension\n");
		}
		catch(IllegalArgumentException e){
			e.printStackTrace();
		}
	
		// Computing the numerator and denominator sums
		for (int j = 0; j < dimensions; j++){
			numSum += gradient.getEntry(j) / hessianDiag.getEntry(j);
			denSum += 1/hessianDiag.getEntry(j);
		}
		// Calculating the constant term
		constantFactor = numSum/ (1/hessianConst + denSum);
		
		// Looping to calculate product of inverse of hessian and gradient
		for (int i = 0 ; i < dimensions; i++){
			product = product.append((gradient.getEntry(i) - constantFactor) / hessianDiag.getEntry(i));
		}
		
		return product;
	}
	/*
	 * Methods specific to variational inference
	 */
	
	// This method calculates the log gamma function
	public double logGamma(double input){
		return Gamma.logGamma(input);
	}
	
	// This is the taylor approximation of the first derivative of the log gamma function
	public double diGamma(double input){
		return Gamma.digamma(input);
	}
	
	// This is the taylor approximation of the second derivative of the log gamma function
	public double triGamma(double input){
		return Gamma.trigamma(input);
	}
	
	
	/*
	 * Some Useful matrix operations
	 */
	
	// This method returns the column-wise sum of a 2D array double[][]
	public RealVector matSumByCol(RealMatrix input){
		//double[][] data = input.getData();
		return null;
	}
	
	
	/*
	 * Some helper functions for this classs
	 */
	
	// Sum of a vector
	public double vectorSum(RealVector input){
		return 0;
	}
	
}
