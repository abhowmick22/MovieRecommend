package lda;

import java.util.ArrayList;
import java.util.List;

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
	private List<Double> alpha;
	
	// \beta for the corpus
	private List<List<Double> > beta;
	
	// Do we need to store the posterior variational params ?
	
	// \gamma for all the documents
	private List<List<Double> > gamma;
	
	// \phi for all the documents
	private List<List<List<Double> > > phi;
	
	// the vocabulary learned from the corpus
	private Vocabulary vocabulary;
	
	// initialize the model
	public void initModel(){
		
	}
	
	// method to return the top K words from each topic
	public List<List<String> > getTopics(int wordsPerTopic){
		return null;
	}
	
	// setters
	public void setNbrTopics(int n){
		this.nbrTopics = n;
	}
	
	public void setAlpha(List<Double> a){
		this.alpha = a;
	}

	public void setBeta(List<List<Double> > b){
		this.beta = b;
	}
	
	public void setGamma(List<List<Double> > g){
		this.gamma = g;
	}
	
	public void setPhi(List<List<List<Double> > > p){
		this.phi = p;
	}
	
	// getters
	public int getNbrTopics(){
		return this.nbrTopics;
	}
	
	public List<Double> getAlpha(){
		return this.alpha;
	}
	
	public List<List<Double> > getBeta(){
		return this.beta;
	}
	
	public List<List<Double> > getGamma(){
		return this.gamma;
	}
	
	public List<List<List<Double> > > getPhi(){
		return this.phi;
	}
	
}
