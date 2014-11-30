package tests.ldaTests;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

import main.lda.Configs;
import main.lda.Corpus;
import main.lda.Model;
import main.lda.Vocabulary;

import org.apache.commons.math3.linear.RealMatrix;

public class TopicRepresentations {
	/************************************************************/
	public static void main(String[] args){
		
				// Path to the file
				String corpusPath = "data/summaries_debug_big_nostemming.txt";
				String vocabPath = "data/cleanedVocab_nostemming_allwords.txt";
				
				Corpus movieSummaries;
				Vocabulary movieVocab;
				
				//Creating the file to read the documents from
				File documentFile = new File(corpusPath);
				File vocabFile = new File(vocabPath);
				
				movieSummaries = new Corpus(documentFile);
				movieVocab = new Vocabulary(vocabFile);
				
				// Create the configs
				Configs conf = new Configs();
				conf.setNbrTopics(20);
		
				Model model = new Model();
				model.initModel(movieSummaries, conf, movieVocab);
		
				// Reading from the file
				model.readModelFromFile("src/tests/ldaTests/modelDumpMethod.txt");
				
				
				List<List<String> > topics = model.getTopicWords(10);
				ListIterator<List<String>> topicIt = topics.listIterator();
				int i=0;
				while(topicIt.hasNext()){
					List<String> topic = topicIt.next();
					System.out.println("\n\nWords for topic " + i + " are ");
					ListIterator<String> it = topic.listIterator();
					while(it.hasNext()){
						String s = it.next();
						System.out.print(s + " ");
					}
					
					i++;
				}
				
				
				/*
				// get the beta
				RealMatrix beta = model.getBeta();
				double[] topic;
				
				// for each topic
				for(int j=0;j<20;j++){
					
					topic = beta.getRowVector(j).toArray();
					
					double[] copy = Arrays.copyOf(topic,topic.length);
					Arrays.sort(copy);
					double[] honey = Arrays.copyOfRange(copy,copy.length - 10, copy.length);
			        int[] topTopicIndices = new int[10];
			        int resultPos = 0 ;
			        //System.out.println(topic.length);
			        for( int i=0;i<topic.length;i++) {
			            double onTrial = topic[i];
			            
			            int index = Arrays.binarySearch(honey,onTrial);
			            System.out.println(index);
			            if(index < 0) continue;
			            topTopicIndices[resultPos++] = i;
			        
			 
			        }
				
			        String[] words = new String[10];
			        System.out.println("\n\nWords for topic " + j + " are ");
			        for(int k=0;k<topTopicIndices.length;k++){
			        	System.out.print(movieVocab.getWordAtIndex(topTopicIndices[k]) + " ");
			        }
			        
			        
				}
				*/
	}

}
