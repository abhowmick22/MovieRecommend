package tests.ldaTests;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import main.lda.Configs;
import main.lda.Corpus;
import main.lda.Model;
import main.lda.Vocabulary;

public class ModelTest {
	
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
		
		// Create the configs
		Configs conf = new Configs();
		conf.setNbrTopics(10);
		
		// Initialize the model
		Model model = new Model();
		model.initModel(movieSummaries, conf, movieVocab);
		System.out.println("Created the model!");
		
		System.out.println("Nbr of topics: " + model.getNbrTopics());
		System.out.println("Vocab size: " + model.getVocabSize());
		
		File dump = new File("src/tests/ldaTests/dump.txt");
		try {
			PrintWriter writer = new PrintWriter(dump, "UTF-8");
			writer.println("Hyperparameters:\n\n");
			writer.println("Initial alpha: " + model.getAlpha());
			writer.println("Initial beta: " + model.getBeta());
			System.out.println("Variational parameters:\n\n");
			System.out.println("Initial gamma: " + model.getGamma());
			System.out.println("Initial phi: " + model.getPhi());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
	}
	/***************************************************************/

}
