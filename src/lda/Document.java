package lda;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/*
 * This class models a single document
 */

public class Document {
	
	// Document ID
	private int docID;
	
	// Total number of words
	private int nbrWords;
	
	// Number of unique terms
	private int uniqueTerms;
	
	// The map of words (vocabulary index) to their frequency
	private Hashtable<Integer, Integer> words;
	
	// Read the document given a list of words and a vocabulary
	public void readDoc(List<String> document, Vocabulary vocab){
		
	}
	
	// getters
	public int getNbrWords(){
		return this.nbrWords;
	}
	
	public int getUniqueTerms(){
		return this.uniqueTerms;
	}
	
	public Hashtable<Integer, Integer> getWords(){
		return this.words;
	}

}
