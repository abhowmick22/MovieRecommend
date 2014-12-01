package main.lda;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.BufferedReader;

/*
 * This class models the corpus of documents
 */

public class Corpus {
	
	// This is the map from wikipedia movie id to the index in docs, gamma, phi etc
	// this is populated when reaidng the corpus
	private ConcurrentHashMap<Integer, Integer> movieToIndexMap;
	
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
	public Corpus(File data){
		System.out.println("Reading the corpus...");
		
		
		this.movieToIndexMap = new ConcurrentHashMap<Integer, Integer>();
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
		    	// Identifying movieId and features part in the text
		    	int delimitPos = text.indexOf(",");
		    	int movieId = Integer.parseInt(text.substring(0, delimitPos));
		    	// Extracting the numerical features
		    	// ASSUMPTION : we have '"[' following delimitPos and ']"' at the end
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
		    	
		    	this.movieToIndexMap.put(movieId, movieCount);
		    	movieCount++;
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
	
	public ConcurrentHashMap<Integer, Integer> getMovieToIndexMap(){
		return this.movieToIndexMap;
	}

}
