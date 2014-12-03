package tests.ldaTests;

import java.io.File;
import main.lda.Configs;
import main.lda.Corpus;
import main.lda.Model;
import main.lda.TopicModeler;
import main.lda.Vocabulary;

//Class to test the EM algorithm for LDA analysis
public class EMTest {
	
	/************************************************************/
	public static void main(String[] args){
		
		// Path to the file

		// Testing with full fledged, all words
		String corpusPath = "data/nostemming/summaryfeatures.txt";
		String vocabPath = "data/nostemming/clean_vocabulary.txt";
		
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
		//model.dumpLogFile("data/nostemming/logDumpMethod_1.txt", "Logfile dumped from method");
		//model.dumpModeltoFile("data/nostemming/modelDumpMethod_1.txt", "Model file dumped from method");

		// Debugging - getting the number of words for each topic
		//model.getTopicWords(30);
		//if(true) return;


	}
	/************************************************************/

}
