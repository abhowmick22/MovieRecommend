package main.lda;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
		this.vocabulary = vocab;
		Utilities utils = new Utilities();
		phi = new ArrayList<RealMatrix>();
		gamma = new ArrayList<RealVector>();
		
		// initialize alpha, with all values set to 1
		this.alpha = new ArrayRealVector(this.nbrTopics, (double) 2.0);
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
		
		//int entries = 0; 
		// Adding dummy phi and gamma for access later in the pipeline
		for(int i = 0; i < this.corpus.getNbrDocs(); i++){
			// Reading the number of words in the document
			int docSize = this.corpus.getDocs().get(i).getDocSize();
			
			// Initialize phi for each document
			RealMatrix phiSingle = new Array2DRowRealMatrix(nbrTopics, docSize);
			
			// Initialize gamma for each document
			RealVector gammaSingle = new ArrayRealVector(nbrTopics);
			
			this.phi.add(phiSingle);
			this.gamma.add(gammaSingle);
			
			// Counting the space taken by phi and gamma
			//entries += nbrTopics * docSize;
		}
		//System.out.println("Size : " + entries * 8/(1024 * 1024));
	}
	
	// method to return the top K words from each topic
	// TODO : Implement this method
	public List<List<String> > getTopicWords(int wordsPerTopic){
		
		//List of top words for each topic;
		List<List<String> > topWords = new ArrayList<List<String> >();
		List<List<Double> > topWordVals = new ArrayList<List<Double> >();
		
		// Iterating over the words for each topic to get the top probability words
		for(int topicId = 0 ; topicId < this.nbrTopics; topicId++){
			// Checking for non-zero entries and extracting corresponding words
			List<String> topicWords = new ArrayList<String>();
			List<Double> topicWordVals = new ArrayList<Double>();
			List<String> topicTopWords = new ArrayList<String>(); 
			List<Double> topicTopWordVals = new ArrayList<Double>();
			
			// Iterating over beta for words
			//System.out.println(this.vocabulary.getVocabSize());
			for(int i = 0; i < this.vocabulary.getVocabSize(); i++){
				if(this.beta.getEntry(topicId, i) > 0){
					topicWords.add(this.vocabulary.getWordAtIndex(i));
					topicWordVals.add(this.beta.getEntry(topicId, i));  
				}	
			}
			
			
			//Get the top K elements using brute force
			for(int iter = 0; iter < wordsPerTopic; iter++){
				int topId = 0;
				double topVal = topicWordVals.get(topId);
				
				for(int cur = 1; cur < topicWordVals.size(); cur++){
					if(topVal < topicWordVals.get(cur)){
						// Replace the top element 
						topVal = topicWordVals.get(cur);
						topId = cur;
					}
				}
					
				//Adding the current top word
				topicTopWords.add(topicWords.get(topId));
				topicTopWordVals.add(topicWordVals.get(topId));
				
				// Remove the particular topMember
				topicWords.remove(topId);
				topicWordVals.remove(topId);
			}
			
			topWords.add(topicTopWords);
			topWordVals.add(topicTopWordVals);
		}

		// Printing the top words
		for(int i = 0; i < topWords.size(); i++){
			System.out.println(topWords.get(i));
			//System.out.println(topWordVals.get(i));
			//System.out.println("\n");
		}
		
		//System.out.println(topWords);
		//System.out.println(topWordVals);
		//System.out.println("\n\n");
		
		return topWords;
	}
	
	// Method to dump model into a text file
	public void dumpModeltoFile(String filename, String message){
		File modelDump = new File(filename);
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(modelDump, "UTF-8");
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		// Write out the file to a dump
		// First line message - for human inference
		writer.println(message);
		// Second line is the number of topics and number of documents
		//writer.println(this.nbrTopics + ' ' + this.corpus.getNbrDocs() + '\n');
		writer.format("%d %d\n\n", this.nbrTopics, this.corpus.getNbrDocs());
		//Alpha
		writer.println(alpha);
		writer.println();
		
		//Beta 
		for(int i = 0; i < beta.getRowDimension(); i++){
			RealVector row = beta.getRowVector(i);
			writer.println(row);
		}
		
		System.out.format("Model dumped at : %s\n", filename);
	}
	
	// Method to dump log file:
	// Contains alpha, beta, gammas and phis for all the movies
	public void dumpLogFile(String filename, String message){
		File modelDump = new File(filename);
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(modelDump, "UTF-8");
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		// Write out the file to a dump
		// First line message - for human inference
		writer.println(message);
		// Second line is the number of topics and number of documents
		//writer.println(this.nbrTopics + ' ' + this.corpus.getNbrDocs() + '\n');
		writer.format("%d %d\n\n", this.nbrTopics, this.corpus.getNbrDocs());
		
		//Alpha
		writer.println(alpha);
		writer.println();
		
		//Beta 
		for(int i = 0; i < beta.getRowDimension(); i++){
			RealVector row = beta.getRowVector(i);
			writer.println(row);
		}
		writer.println();
		
		// Gamma
		for(int i = 0; i < gamma.size(); i++){
			RealVector docGamma = gamma.get(i);
			writer.println(docGamma);
		}
		writer.println();
		
		// Phi
		for(int i = 0; i < phi.size(); i++){
			for(int j = 0; j < phi.get(i).getRowDimension(); j++){
				RealVector row = phi.get(i).getRowVector(j); 
				writer.println(row);
			}
			writer.println();
		}
		
		System.out.format("Logfile dumped at : %s\n", filename);
	}
	
	// Method to read dumped model from a text file
	
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