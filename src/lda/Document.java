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
	private int docSize;
	
	// Number of unique words
	private int uniqueWords;
	
	// The map of words (vocabulary index) to their frequency
	// Each <K,V> pair is a <unique word index, frequency>
	private Hashtable<Integer, Integer> wordFreqList;
	
	// list of words according to the order in which they appear in document
	// each entry is the index of the word in Vocabulary
	private List<Integer> words;			// this will be of size nbrWords
	
	// Read the document given a list of words and a vocabulary
	// populate the 'words' and 'wordFreqList' data structures
	public void readDoc(List<String> document, Vocabulary vocab){
		
	}
	
	// getters
	public int getDocSize(){
		return this.docSize;
	}
	
	public int getUniqueWords(){
		return this.uniqueWords;
	}
	
	public Hashtable<Integer, Integer> getWordFreqlist(){
		return this.wordFreqList;
	}
	
	public List<Integer> getWords(){
		return this.words;
	}
	
	public int getDocID(){
		return this.docID;
	}

}
