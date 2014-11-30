package tests.ldaTests;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import org.apache.commons.math3.special.Gamma;

import main.lda.Configs;
import main.lda.Corpus;
import main.lda.EstimatorBlock;
import main.lda.InferenceBlock;
import main.lda.Model;
import main.lda.TopicModeler;
import main.lda.Vocabulary;
import main.lda.Document;

//Class to test the EM algorithm for LDA analysis
public class EMTest {
	
	/************************************************************/
	public static void main(String[] args){
		
		// Path to the file
		//String corpusPath = "data/summariesTrain_debug_nostemming.txt";
		//String corpusPath = "data/summaries_debug_big_nostemming.txt";		
		//String corpusPath = "data/summaries_debug_big_nostemming.txt";
		//String vocabPath = "data/sortedVocab_nostemming.txt";

		// Testing with full fledged, all words
		String corpusPath = "data/summaryFeatures_nostemming_cleaned_debug.txt";
		String vocabPath = "data/cleanedVocab_nostemming_allwords.txt";
		
		Corpus movieSummaries;
		Vocabulary movieVocab;
		Model model;
		TopicModeler tm;

		//Creating the file to read the documents from
		File documentFile = new File(corpusPath);
		File vocabFile = new File(vocabPath);

		movieSummaries = new Corpus(documentFile);
		movieVocab = new Vocabulary(vocabFile);
		model = new Model();
		tm = new TopicModeler();

		// Create the configs
		Configs conf = new Configs();
		conf.setNbrTopics(20);

		// Initialize the model
		model.initModel(movieSummaries, conf, movieVocab);
		System.out.println("Created the model!");

		// Model the corpus
		model = tm.modelCorpus(movieSummaries, conf, movieVocab);

		// Dumping the model and log files along with messages for human interpretation
		model.dumpLogFile("src/tests/ldaTests/logDumpMethod.txt", "Logfile dumped from method");
		model.dumpModeltoFile("src/tests/ldaTests/modelDumpMethod.txt", "Model file dumped from method");

		// Debugging - getting the number of words for each topic
		//model.getTopicWords(30);
		//if(true) return;


	}
	/************************************************************/

}
