package tests.ldaTests;

import java.io.File;
import main.lda.Configs;
import main.lda.Corpus;
import main.lda.Document;
import main.lda.InferenceBlock;
import main.lda.Model;
import main.lda.Vocabulary;

public class InferenceTest {
	
	/************************************************************/
	public static void main(String[] args){
		
		// Path to the file
				String corpusPath = "data/nostemming/summaryfeatures.txt";
				String vocabPath = "data/nostemming/clean_vocabulary.txt";
				
				Corpus movieSummaries;
				Vocabulary movieVocab;
				Model model;
				InferenceBlock inf;
				
				//Creating the file to read the documents from
				File documentFile = new File(corpusPath);
				File vocabFile = new File(vocabPath);
				
				movieSummaries = new Corpus(documentFile);
				movieVocab = new Vocabulary(vocabFile);
				model = new Model();
				inf = new InferenceBlock();
				
				// Create the configs
				Configs conf = new Configs();
				conf.setNbrTopics(10);
				
				// Initialize the model
				model.initModel(movieSummaries, conf, movieVocab);
				System.out.println("Created the model!");
				
				// Get the likelihood of a single document
				//List<Document> docs = movieSummaries.getDocs();
				//for(Document d : docs)	System.out.println(d.getDocId());
				Document doc = movieSummaries.getDocs().get(0);
				
				double likelihood = inf.infer(doc, model, conf);
				
				System.out.println(likelihood);
		
	}
	/************************************************************/
}
