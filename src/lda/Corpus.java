package lda;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.BufferedReader;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

/*
 * This class models the corpus of documents
 */

public class Corpus {
	// Number of documents in the corpus
	private int nbrDocs;
	
	// Number of Words in each document 
	// Each document has different number of words
	// Uninitialized, untouched
	private int nbrWords;
	
	// The list of documents in the corpus
	private List<Document> documents;
	
	// getters
	public int getNbrDocs(){
		return this.nbrDocs;
	}
	
	public int getNbrWords(){
		return this.nbrWords;
	}
	
	public List<Document> getDocs(){
		return this.documents;
	}
	
	/************************************************************/
	// Constructor, takes in the file path and
	// populates all the members
	protected Corpus(File data){
		System.out.println("Reading the corpus...");
		
		// Opening the file and reading each document
		try {
			// Reader initialization for text reading
		    BufferedReader reader = new BufferedReader(new FileReader(data));
		    String text = null;

		    // Initializing the documents
		    this.documents = new ArrayList<Document>();
		    
		    // Keeping track of the current count of movies
		    int movieCount = 0;
		    // Reading each line (each document)
		    while ((text = reader.readLine()) != null) {
		    	movieCount ++;
		    	// Identifying movieId and features part in the text
		    	int delimitPos = text.indexOf(",");
		    	int movieId = Integer.parseInt(text.substring(0, delimitPos-1));

		    	// Extracting the numerical features
		    	String subText = text.substring(delimitPos + 3, text.length()-1);
		    	String[] featureStrings = subText.split(",");

		    	List<Integer> features = new ArrayList<Integer>();
		    	// Collecting all the features ( word indices )
		    	for(String featureStringsMem: featureStrings){
		    		//System.out.println(featureStringsMem.trim());
		    		String trimmedString = featureStringsMem.trim();
		    		// Eliminating spaces
		    		if(trimmedString.length() > 0)
		    			features.add(Integer.parseInt(trimmedString));
		    	}
		    	
		    	// Creating the movie document object
		    	Document movieDocument = new Document(features, movieId, movieCount);
		    	this.documents.add(movieDocument);
		    }
		   
		    // Closing the file
		    reader.close();
		} 
		catch (FileNotFoundException e) {
		    e.printStackTrace();
		} 
		catch (IOException e) {
		    e.printStackTrace();
		}
		
		//Final assignments
		this.nbrDocs = this.documents.size(); 
		
		System.out.println("Successfully read corpus!");
	}

	/************************************************************/
	public static void main(String[] args){
		// Path to the file
		String corpusPath = "data/summariesTrain.txt";
		String vocabPath = "data/sortedVocab.txt";
		
		Corpus movieSummaries;
		Vocabulary movieVocab;
		
		//Creating the file to read the documents from
		File documentFile = new File(corpusPath);
		File vocabFile = new File(vocabPath);
		
		movieSummaries = new Corpus(documentFile);
		movieVocab = new Vocabulary(vocabFile);
		
		System.out.println("Number of movie summaries read : " + movieSummaries.documents.size());
		System.out.println("Successfully read " + movieVocab.getVocabSize() + " words from vocabulary!");
		
		// Debugging Utilities and other functionalities
		Utilities utils = new Utilities();
		
		// Debugging NR solver
		/*int noTopics = 10;
		int noDocuments = 5;
		
		Configs configs = new Configs();
		RealVector initAlpha = new ArrayRealVector(noTopics);
		List<RealVector> gamma = new ArrayList<RealVector>();
		
		// Randomly assigning values to initAlpha and gamma
		Random rand = new Random();
		
		for(int i = 0; i < noTopics; i++)
			initAlpha.setEntry(i, rand.nextDouble());
		
		for(int i = 0; i < noDocuments; i++){
			RealVector documentGamma = new ArrayRealVector(noTopics);
			for(int j = 0; j < noTopics; j++)
				documentGamma.setEntry(j, rand.nextDouble());
			
			gamma.add(documentGamma);
		}
		
		RealVector nextAlpha = utils.performNR(configs, initAlpha, gamma);
		System.out.println(nextAlpha);
		System.out.println(initAlpha);*/
		
		// Debugging each step
		/*RealVector hessianDiag = new ArrayRealVector(new double[]{1, 2, 3, 4, 5, 6});
		RealVector gradient = new ArrayRealVector(new double[]{6, 5, 4, 3, 2, 1});
		double hessianConst = 4.0;
		RealVector nrStep = utils.computeNRStep(hessianDiag, hessianConst, gradient);
		System.out.println(nrStep);*/
	}
	/************************************************************/
}
