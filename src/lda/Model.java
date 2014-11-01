package lda;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

/*
 * This class captures all the information about the model learned
 * from a corpus - including \alpha, \beta, list of topics
 * This object is be returned to a client of the TopicModeler API
 */

public class Model {
	
	// Corpus from which this is learned
	private Corpus corpus;
	
	// nbr of topics
	private int nbrTopics;
	
	// words per topic
	private int wordsPerTopic;
	
	// \alpha for the corpus
	//private List<Double> alpha;
	private RealVector alpha;
	
	// \beta for the corpus
	private RealMatrix beta;
	
	// Do we need to store the posterior variational params ?
	
	// \gamma for all the documents
	private List<RealVector> gamma;
	
	// \phi for all the documents
	private List<RealMatrix> phi;
	
	// the vocabulary learned from the corpus
	private Vocabulary vocabulary;
	

	// initialize the model
	public void initModel(Corpus c, Configs conf){
		this.corpus = c;
		this.nbrTopics = conf.getNbrTopics();
		
		// initialize alpha
		this.alpha = conf.getInitAlpha();
		// how to initiqlize beta ?
	}
	
	// method to return the top K words from each topic
	public List<List<String> > getTopics(int wordsPerTopic){
		return null;
	}
	
	// setters
	public void setNbrTopics(int n){
		this.nbrTopics = n;
	}
	
	public void setAlpha(RealVector a){
		this.alpha = a;
	}

	public void setBeta(RealMatrix b){
		this.beta = b;
	}
	
	public void setGamma(List<RealVector> g){
		this.gamma = g;
	}
	
	public void setPhi(List<RealMatrix> p){
		this.phi = p;
	}
	
	// getters
	public int getNbrTopics(){
		return this.nbrTopics;
	}
	
	public RealVector getAlpha(){
		return this.alpha;
	}
	
	public RealMatrix getBeta(){
		return this.beta;
	}
	
	public List<RealVector> getGamma(){
		return this.gamma;
	}
	
	public List<RealMatrix> getPhi(){
		return this.phi;
	}
	
}
