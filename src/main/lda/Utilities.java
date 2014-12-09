package main.lda;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
// For Gamma functions
import org.apache.commons.math3.special.Gamma;
import org.apache.commons.math3.stat.StatUtils;
import org.la4j.vector.Vector;

/*
 * This module implements all the distributions, optimizations,
 * and other math utility functions.
 */

public class Utilities {
	// Evaluate the stationary point of a function using Newton-Raphson method,
	// assuming the form of the likelihood function is known (thus, it is not general)
	// In LDA, this is used to maximize the lower bound of log L, (i.e. function is known)
	// wrt \alpha in the M-step
	// Takes in current iteration alpha and gamma values to estimate the value of alpha for the next iteration
	public RealVector performNR(Configs configs, RealVector initAlpha, List<RealVector> gamma){
		
		//Asserting input parameters and their dimensions
		try{
			// Size of initAlpha
			int alphaSize = initAlpha.getDimension();
			// Checking for matching in the dimensions before starting NR iterations
			for(RealVector gammaMember: gamma){
				if(gammaMember.getDimension() != alphaSize)
					throw new IllegalArgumentException("All the members of gamma in NR should have same dimension\n");	
			}
		}
		catch(IllegalArgumentException e){
			e.printStackTrace();
		}
		
		// declarations of running variables
		double hessianConst;
		
		//New alpha
		//RealVector newAlpha = new ArrayRealVector(initAlpha.getDimension(), 2.0);
		RealVector newAlpha = initAlpha;
		// Holder objects for the variables used in the algorithm 
		RealVector gradient, hessianDiag, alphaIncrement;
		
		// Limiting the number of iterations
		for(int i = 0; i < configs.getMaxNRIterations(); i++){
			// Obtaining the gradient 
			gradient = this.computeGradient(newAlpha, gamma);

			//gradient = this.normalizeL2(gradient);
			
			// Obtaining the Hessian diagonal
			hessianDiag = this.computeHessianDiag(newAlpha, gamma.size());
			
			// Obtaining the Hessian constant
			hessianConst = this.computeHessianConstant(newAlpha);
			
			// Obtaining the increment in alpha
			alphaIncrement = this.computeNRStep(hessianDiag, hessianConst, gradient);
			
			// Checking if increment is within the threshold, exist if yes
			if(alphaIncrement.getNorm()/initAlpha.getNorm() < configs.getAlphaChangeThreshold()){
				System.out.println("NR iterations converged after " + i + " steps!");
				break;
			}
			
			// Updating alpha
			newAlpha = newAlpha.subtract(alphaIncrement);
			
			//Checking if any of the values of updated values of alpha is negative, if yes retry with smaller alpha init
			for(int j = 0; j < newAlpha.getDimension(); j++){
				if(newAlpha.getEntry(j) < 0){
					// Re-try with smaller value of initAlpha
					System.out.println("Using a smaller initial estimate for alpha\n\n");
					return performNR(configs, initAlpha.mapMultiply(10.0), gamma);
				}
			}
			
			// Debug messages
			//System.out.println("Mag: " + gradient.getNorm() + " " + gradient);
			//System.out.println("Gradient Magnitude:" + gradient.getNorm() + " " + gamma.size());
			//System.out.println("Diagonal : " + hessianDiag);
			//System.out.format("Hessian diagonal norm: %f\n", hessianDiag.getNorm());
			//System.out.println("Constant : " + hessianDiag);
			//System.out.format("Hessian constant norm: %f\n", hessianConst);
			//System.out.println("Increment : " + alphaIncrement + "\n");
			//System.out.println("Increment magnitude: " + alphaIncrement.getNorm());
			//System.out.println(newAlpha);
			}

		System.out.println("NR iterations completed or exhausted");
		return newAlpha;
	}
	
	// Newton Raphson implemented as given in matlab package
	RealVector newtonRaphson(Configs configs, RealVector initAlpha, List<RealVector> gamma){
		// Extracting number of topics and documents
		int noTopics = initAlpha.getDimension();
		int noDocs = gamma.size();
		
		// Compute iteration independent terms
		RealVector gradient = new ArrayRealVector(noTopics);
		
		RealVector gammaTopicTerm = new ArrayRealVector(noTopics);
		//For each document
		double docTopicSum;
		double previousTerm, updatedTerm;
		for(int i = 0; i < noDocs; i++){
			docTopicSum = 0;
			//Get the sum of gammas for that document and all topics
			for(int j = 0; j < noTopics; j++)
				docTopicSum += gamma.get(i).getEntry(j);
			
			docTopicSum = this.diGamma(docTopicSum);
			
			// Contribution to each topic
			for(int j = 0; j < noTopics; j++){
				previousTerm = gammaTopicTerm.getEntry(j);
				updatedTerm = previousTerm + this.diGamma(gamma.get(i).getEntry(j)) - docTopicSum;
				gammaTopicTerm.setEntry(j, updatedTerm);	
			}
		}
		
		RealVector iterAlpha = initAlpha.copy();
		
		////////////////////////////////////////////////////////////////////////////////
		// Running the NR iteration
		double alphaSum;
		RealVector previousAlpha = new ArrayRealVector(noTopics);
		RealVector hessianVector = new ArrayRealVector(noTopics);

		double hessianScalar;
		
		for(int i = 0; i < configs.getMaxNRIterations(); i++){
		//for(int i = 0; i < 2; i++){
			System.out.println("Alpha : " + iterAlpha);
			
			alphaSum = 0;
			//Summing the elements of iterAlpha
			for(int j = 0; j < noTopics; j++)
				alphaSum += iterAlpha.getEntry(j); 
			
		
			// Evaluating the gradient
			double gradTerm = 0;
			for(int j = 0; j < noTopics; j++){
				gradTerm = noDocs * (this.diGamma(alphaSum) 
						- this.diGamma(iterAlpha.getEntry(j))) 
						+ gammaTopicTerm.getEntry(j);
				
				gradient.setEntry(j, gradTerm);
			}
			
			// Evaluating the hessian vector component
			for(int j = 0; j < noTopics; j++){
				hessianVector.setEntry(j, -1.0/this.triGamma(alphaSum));
			}
			
			// Evaluating the hessian scalar component
			// Computing hessianVector sum of elements and numerator of hessianScalar
			hessianScalar = 0; 
			double sumHessianVector = 0;
			for(int j = 0; j < noTopics; j++){
				sumHessianVector += hessianVector.getEntry(j);
				hessianScalar += hessianVector.getEntry(j) * gradient.getEntry(j);
			}
			
			hessianScalar = hessianScalar / (sumHessianVector + 1/this.triGamma(alphaSum));
			
			// Updating alpha using NR rule
			double newAlphaComp = 0;
			for(int j = 0; j < noTopics; j++){
				newAlphaComp = iterAlpha.getEntry(j) 
						- hessianVector.getEntry(j) * (gradient.getEntry(j) - hessianScalar ) / noDocs;
				
				// Checking if any of the elements of alpha is negative
				if(newAlphaComp < 0){
					System.out.format("Re-starting with scaled down version of alpha : %f\n", newAlphaComp);
					// Re-start with scaled down value of initAlpha, if yes
					//return newtonRaphson(configs, initAlpha.mapDivide(10.0), gamma);
					return newtonRaphson(configs, new ArrayRealVector(noTopics, 0.1), gamma);
				}
				
				// Else update the element of alpha
				iterAlpha.setEntry(j, newAlphaComp);
			}
				
			// Check for convergence based on increment
			if(iterAlpha.add(previousAlpha).getNorm() / iterAlpha.getNorm() < configs.getEmConvergence()){
				// Return current alpha and exit the method
				System.out.format("Exiting NR method after %d iterations \n", i);
				return iterAlpha;
			}
			
			// Update the previous value of alpha for next iteration 
			previousAlpha = iterAlpha.copy();
		}
		
		System.out.println("Exiting NR method after exhausting iterations!");
		
		return previousAlpha;
	}
	
		
	/*
	 * Methods specific to Newton Raphson
	 */
	// Compute the alpha independent component of gradient and use it for NR iterations
	// This is specially used for batch processing of documents
	// Takes the gammaTerm until now along with different set of Documents and updates the gammaTerm of gradient
	/*RealVector updateGradientGammaTerm(RealVector initGammaTerm, List<RealVector>curGamma){
		int batchSize = curGamma.size();
		int noTopics = initGammaTerm.getDimension();
		RealVector updatedGammaTerm = initGammaTerm.copy();
		
		for(int i = 0; i < batchSize; i++){
			
			// Get the sum of gamma values for a particular document over topics
			double docGammaSum = 0;
			for(int j = 0; j < noTopics; j++)
				docGammaSum += curGamma.get(i).getEntry(j);
				
			// Getting the contribution of each document in the alpha independent term of the gradient
			// to be used in NR iterations
			RealVector docGradient = new ArrayRealVector(noTopics);
			for(int j = 0; j < noTopics; j++)
				docGradient.setEntry(j, this.diGamma(curGamma.get(i).getEntry(j)) - this.diGamma(docGammaSum));
			
			// Adding the contribution of document to the initGammaTerm to get updated gammaTerm
			updatedGammaTerm = updatedGammaTerm.add(docGradient);
		}
		
		return updatedGammaTerm;
	}*/
	
	
	// For the given alpha and gamma, computes the gradient to be used for NR iterations
	public RealVector computeGradient(RealVector alpha, List<RealVector> gamma){
		int noDocuments = gamma.size();
		int noTopics = alpha.getDimension();
		
		// Computing some constant terms 
		double alphaElemSum = 0;
		for(int i = 0; i < noTopics; i++){
			//System.out.println("alpha entry has become " + alpha.getEntry(i));
			alphaElemSum += alpha.getEntry(i);
		}
		
		double diGammaAlphaSum = this.diGamma(alphaElemSum);
		
		
		double diGammaDocSum = 0;
		for (int i = 0; i < noDocuments; i++){
			double gammaSum = 0;
			
			for(int j = 0; j < noTopics; j++)
				gammaSum += gamma.get(i).getEntry(j);
			
			//System.out.format("%f %f\n", gammaSum, 1.0f);
			diGammaDocSum += this.diGamma(gammaSum);
		}
		
		
		// Computing the gradient value
		RealVector gradient = new ArrayRealVector(noTopics);
		
		double diGammaDocTerm, gradComponent;
		for(int i = 0 ; i < noTopics; i++){
			diGammaDocTerm = 0;
			for(int j = 0; j < noDocuments; j++){
				diGammaDocTerm += this.diGamma(gamma.get(j).getEntry(i));
			}
			diGammaDocTerm -= diGammaDocSum;
			
			gradComponent = noDocuments * (diGammaAlphaSum - this.diGamma(alpha.getEntry(i))) + diGammaDocTerm;
			gradient.setEntry(i, gradComponent);
		}
		return gradient;
	}
	
	// For given alpha and gamma, it returns the diagonal of the special form of hessian for NR iterations
	public RealVector computeHessianDiag(RealVector alpha, int noDocuments){
		int noTopics = alpha.getDimension();
		
		// hessian diagonal
		RealVector hessianDiag = new ArrayRealVector(noTopics);  
		for(int i = 0; i < noTopics; i++){
			hessianDiag.setEntry(i, noDocuments * this.triGamma(alpha.getEntry(i)));
		}
		
		return hessianDiag;
	}
	
	//For given alpha and gamma, it returns the constant to run NR iterations
	public double computeHessianConstant(RealVector alpha){
		int noTopics = alpha.getDimension();
		
		// Computing some constant terms 
		double alphaElemSum = 0;
		for(int i = 0; i < noTopics; i++)
			alphaElemSum += alpha.getEntry(i);			
		
		return -1 * this.triGamma(alphaElemSum);
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
		RealVector product = new ArrayRealVector(dimensions);
		
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
			product.setEntry(i, -1 * (gradient.getEntry(i) - constantFactor) / hessianDiag.getEntry(i));
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
		//System.out.println(input);
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
	public RealVector matSumAlongDim(RealMatrix input, int dimension){
		//double[][] data = input.getData();
		int rowDimension;
		int colDimension;
		RealVector sum;
		
		if(dimension == 2){
			colDimension = input.getColumnDimension();
			rowDimension = input.getRowDimension();
			sum = new ArrayRealVector(rowDimension);
			for (int i=0; i<colDimension; i++)
				sum = sum.add(input.getColumnVector(i));
		}
		else{
			rowDimension = input.getColumnDimension();
			colDimension = input.getRowDimension();
			sum = new ArrayRealVector(colDimension);
			for (int i=0; i<rowDimension; i++)
				sum = sum.add(input.getRowVector(i));   
		}
		
		return sum;
	}
	
	
	/*
	 * Some helper functions for this classs
	 */
	
	// Sum of a vector
	public double vectorSum(RealVector input){
		double[] data = input.toArray();
		return StatUtils.sum(data);
	}
	
	// Normalize a vector
	public double[] normalize(double[] input){
		double sum = StatUtils.sum(input);
		RealVector ip = new ArrayRealVector(input);
		ip = ip.mapMultiply(1.0/sum);
		return ip.toArray();
	}
	
	// Normalize a vector given realVector input (easier and more frequently used)
	public RealVector normalizeL2(RealVector input){
		return input.mapMultiply(1.0/input.getNorm());
	}
}
