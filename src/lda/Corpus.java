package lda;

import java.util.ArrayList;
import java.util.List;
import java.io.File;

/*
 * This class models the corpus of documents
 */

public class Corpus {
	
	// Constructor, takes in the file path and
	// populates all the members
	protected Corpus(File data){
		
	}
	
	// Number of documents in the corpus
	private int nbrDocs;
	
	// Number of Words in each document
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

}
