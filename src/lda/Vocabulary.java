package lda;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/*
 * This class models the vocabulary used for topic modelling
 */

public class Vocabulary {
	
	// array of words
	private ArrayList<String> words = new ArrayList<String>();
	
	// constructor for vocabulary given a file vocab.txt
	public Vocabulary(File file){
		
	}
	
	// getter for words
	public ArrayList<String> getWords(){
		return this.words;
	}
	

}
