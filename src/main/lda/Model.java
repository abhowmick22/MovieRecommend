package main.lda;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.stat.StatUtils;

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
	public void initModel(Corpus c, Configs conf, Vocabulary vocab){
		this.corpus = c;
		this.nbrTopics = conf.getNbrTopics();
		this.wordsPerTopic = vocab.getVocabSize();
		int nbrDocs = this.corpus.getNbrDocs();
		Utilities utils = new Utilities();
		
		// initialize alpha, with all values set to 1
		this.alpha = new ArrayRealVector(this.nbrTopics, (double) 1);
		// initialize beta
		this.beta = new Array2DRowRealMatrix(this.nbrTopics, this.wordsPerTopic);
		
		// init the values of beta, by randomizing and normalizing over the rows
		Random rand = new Random(10701);
		double[] row;
		for(int i=0; i<beta.getRowDimension(); i++){
			for(int j=0; j<beta.getColumnDimension(); j++){
				beta.setEntry(i, j, rand.nextDouble());
			}
			row = beta.getRow(i);
			row = utils.normalize(row);
			beta.setRow(i, row);
		}	
		
		// initialize phi
		RealMatrix oneuponk = new Array2DRowRealMatrix(this.wordsPerTopic, this.nbrTopics);
		// assuming that all elements are initialized to zero
		for(int i=0; i<oneuponk.getRowDimension(); i++){
			for(int j=0; j<oneuponk.getColumnDimension(); j++){
				oneuponk.setEntry(i, j, (double) (1/this.nbrTopics));
			}
		}
		// initialize gamma
		RealVector nuponk = new ArrayRealVector(this.nbrTopics, (double) (this.wordsPerTopic/this.nbrTopics));
		nuponk = nuponk.add(alpha);
		
		// initialie phi and gamma for all the documents
		for(int i=0; i<nbrDocs; i++){
			phi = new ArrayList<RealMatrix>();
			phi.add(oneuponk);
			gamma = new ArrayList<RealVector>();
			gamma.add(nuponk);
		}
		
	}
	
	// method to return the top K words from each topic
	// TODO : Implement this method
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
	
	public void setGammaSingle(RealVector g, int index){
		this.gamma.set(index, g);
	}
	
	public void setPhi(List<RealMatrix> p){
		this.phi = p;
	}
	
	public void setPhiSingle(RealMatrix p, int index){
		this.phi.set(index, p);
	}
	
	// getters
	public int getNbrTopics(){
		return this.nbrTopics;
	}
	
	public int getVocabSize(){
		return this.wordsPerTopic;
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
