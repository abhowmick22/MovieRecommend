package tests.ldaTests;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.math3.linear.RealVector;

import main.lda.Configs;
import main.lda.Corpus;
import main.lda.Document;
import main.lda.InferenceBlock;
import main.lda.Model;
import main.lda.TopicModeler;
import main.lda.Vocabulary;

/*
 * This is the class to perform inference on a held-out document after performing LDA on
 * the corpus
 */

public class PosteriorAnalysis {
	
	/************************************************************/
	public static void main(String[] args){
		//enter corpus path to file containing only training summaries (36,000)
		String corpusPathTrain = "data/nostemming/summaryfeatures_train.txt";		
		String vocabPath = "data/nostemming/clean_vocabulary_train.txt";
		
		Corpus movieSummariesTrain;
		Vocabulary movieVocab;
		Model model;
		TopicModeler tm;

		//Creating the file to read the documents from
		File documentFile = new File(corpusPathTrain);
		File vocabFile = new File(vocabPath);

		movieSummariesTrain = new Corpus(documentFile);
		movieVocab = new Vocabulary(vocabFile);
		model = new Model();
		tm = new TopicModeler();

		// Create the configs
		Configs conf = new Configs();
		conf.setNbrTopics(10);

		// Initialize the model
		model.initModel(movieSummariesTrain, conf, movieVocab);

		// Model the corpus
		model = tm.modelCorpus(movieSummariesTrain, conf, movieVocab);

		// Dumping the model and log files along with messages for human interpretation
		model.dumpLogFile("data/nostemming/logFile_train.txt", "Logfile dumped from method");
		model.dumpModeltoFile("data/nostemming/modelDump_train.txt", "Model file dumped from method");

		// LDA learning done, now to do hold-out testing
		//enter corpus path to file containing test summaries (5,000)
		String corpusPathTest = "data/nostemming/summaryfeatures_test.txt";
		Corpus movieSummariesTest;
		movieSummariesTest = new Corpus(new File(corpusPathTest));
		InferenceBlock inf = new InferenceBlock();;
		
		// choose the doc to do inference on
		Document doc = movieSummariesTest.getDocs().get(0);
		double likelihood = inf.infer(doc, model, conf);
		
		// get the indices of top topics for this document
		int docIndex = doc.getDocId();
		RealVector gamma = model.getGamma().get(docIndex);
		RealVector alpha = model.getAlpha();
		
		// the difference between gamma and alpha gives the expected number of words contributed
		// by a particular topic
		
		double[] gammaArr = gamma.subtract(alpha).toArray();
		double[] copy = Arrays.copyOf(gammaArr,gammaArr.length);
		Arrays.sort(copy);
		double[] temp = Arrays.copyOfRange(copy,copy.length - 4, copy.length);
        int[] topTopicIndices = new int[4];
        int resultPos = 0;
        for(int i = 0; i < gammaArr.length; i++) {
            double onTrial = gammaArr[i];
            int index = Arrays.binarySearch(temp,onTrial);
            if(index < 0) continue;
            topTopicIndices[resultPos++] = i;
        }
		
		// print out the top 4 topics in this document
		List<List<String> > topWords = model.getTopicWords(10);
		System.out.println("Topic 1 - " + topWords.get(topTopicIndices[0]));
		System.out.println("Topic 2 - " + topWords.get(topTopicIndices[1]));
		System.out.println("Topic 3 - " + topWords.get(topTopicIndices[2]));
		System.out.println("Topic 4 - " + topWords.get(topTopicIndices[3]));
		
	}
}
