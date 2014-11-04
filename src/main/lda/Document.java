package main.lda;

//import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.ArrayList;

/*
 * This class models a single document
 */

public class Document {
	// Document ID
	private int docId;
	
	// Total number of words
	private int docSize;
	
	// Number of unique words
	private int uniqueWords;
	
	// Meta-data, id of the movie
	private int movieId;
	
	// The map of words (vocabulary index) to their frequency
	// Each <K,V> pair is a <unique word index, frequency>
	private Hashtable<Integer, Integer> wordFreqList;
	
	// list of words according to the order in which they appear in document
	// each entry is the index of the word in Vocabulary
	private List<Integer> wordIds;			// this will be of size nbrWords
	
	// Return the words associated with the document given the vocabulary
	public List<String> readDoc(Vocabulary vocab){
		List<String> wordsInDoc = new ArrayList<String>();
		
		List<String> vocabWords = vocab.getWords();
		for (int wordId: wordIds){
			wordsInDoc.add(vocabWords.get(wordId));
		}
		return wordsInDoc;
	}
	
	//Constructor
	public Document(List<Integer> features, int idMovie, int idDoc){
		//Setting the internal parameters
		this.movieId = idMovie;
		this.docId = idDoc;
		this.docSize = features.size();
		
		// Assigning the features
		this.wordIds = features;
		
		// Functionality of uniqueWords, wordFreqList to be added later
		//private int uniqueWords;
		//private Hashtable<Integer, Integer> wordFreqList;
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
	
	public List<Integer> getWordIds(){
		return this.wordIds;
	}
	
	public int getDocId(){
		return this.docId;
	}

	public int getMovieId() {
		return movieId;
	}
}
