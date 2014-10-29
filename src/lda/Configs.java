package lda;

/*
 * This class contains all the configurations for the estimation and inference
 * algorithms. These can be set externally through the TopicModeler
 */

public class Configs {
	
	// Max number of iterations for variational inference
	private int varIters;
	
	// Convergence threshold for variational inference
	private double varConvergence;
	
	// Max number of iterations for E-M
	private int emIters;
	
	// Convergence threshold for E-M
	private double emConvergence;
	
	// setters
	public void setVarIters(int iters){
		varIters = iters;
	}
	
	public void setVarConvergence(double conv){
		varConvergence = conv;
	}
	
	public void setEmIters(int iters){
		emIters = iters;
	}

	public void setEmConvergence(double conv){
		emConvergence = conv;
	}
	
	// getters
	public int getVarIters(){
		return varIters;
	}
	
	public double setVarConvergence(){
		return varConvergence;
	}
	
	public int setEmIters(){
		return emIters;
	}

	public double setEmConvergence(){
		return emConvergence;
	}
}
