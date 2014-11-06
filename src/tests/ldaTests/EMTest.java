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
				String corpusPath = "data/summariesTrain.txt";
				//String corpusPath = "data/summaries_debug_big_nostemming.txt";
				String vocabPath = "data/sortedVocab_nostemming.txt";
				
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
				conf.setNbrTopics(10);
				
				// Initialize the model
				model.initModel(movieSummaries, conf, movieVocab);
				System.out.println("Created the model!");
				
				//Creating the file to read the documents from
				File modelDump = new File("src/tests/ldaTests/modelDump.txt");
				PrintWriter writer = null;
				try {
					writer = new PrintWriter(modelDump, "UTF-8");
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				// Model the corpus
				model = tm.modelCorpus(movieSummaries, conf, movieVocab);
				
				// Debugging - getting the number of words for each topic
				model.getTopicWords(10);
				if(true) return;
			
				// Write out the file to a dump
				writer.println("After ESTIMATION \n----------------\n\n");
				writer.println("Hyperparameters:\n\n");
				writer.println("Initial alpha: " + model.getAlpha());
				writer.println("Initial beta: " + model.getBeta());
				writer.println("Variational parameters:\n\n");
				writer.println("Initial gamma: " + model.getGamma());
				writer.println("Initial phi: " + model.getPhi());

	}
	/************************************************************/

}
