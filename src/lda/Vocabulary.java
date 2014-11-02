package lda;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
//import java.util.List;

/*
 * This class models the vocabulary used for topic modeling
 */

public class Vocabulary {
	
	// size of vocabulary 
	private int vocabSize;
	
	// array of words
	private ArrayList<String> words;
	
	// constructor for vocabulary given a file vocab.txt
	public Vocabulary(File dictionary){
		System.out.println("Reading the vocabulary...");
		
		// Opening the file and reading the vocabulary
		try {
			// Reader initialization for text reading
		    BufferedReader reader = new BufferedReader(new FileReader(dictionary));
		    String text = null;

		    // Initializing the words list
		    this.words = new ArrayList<String>();
		    
		    String[] splitString;
		    // Reading each line (each word in vocabulary)
		    while ((text = reader.readLine()) != null) {
		    	// Splitting the string based on the comma, reading the word
		    	splitString = text.split(",");
		    	this.words.add(splitString[1]);
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
		this.vocabSize = this.words.size();
		System.out.println("Successfully read vocabulary!");
	}
	
	// getter for words
	public ArrayList<String> getWords(){
		return this.words;
	}
	
	public int getVocabSize(){
		return this.vocabSize;
	}
	
	// setter
	public void setVocabSize(int vsize){
		this.vocabSize = vsize;
	}
}
