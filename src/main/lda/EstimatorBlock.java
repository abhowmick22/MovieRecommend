package main.lda;

import java.util.List;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.stat.StatUtils;

/*
 * This module estimates the topic model, i.e. maximises the lower bound on
 * log likelihood using MLE. This will use variational EM algorithm to 
 * estimate \alpha and \beta,  taking the corpus, \phi and \gamma as input.
 */

public class EstimatorBlock {
	
	// do parameter estimation of \alpha and \beta
	public void estimate(Corpus corpus, Model model, Configs conf){
		
		// Extract the relevant info
		int nbrTopics = model.getNbrTopics();
		int vocabSize = model.getVocabSize();
		int nbrDocs = corpus.getNbrDocs();
		int nbrWords;
		double b = 0;
		List<Document> documents = corpus.getDocs();
		Document doc;
		RealMatrix beta = model.getBeta();
		RealVector alpha = model.getAlpha();
		RealMatrix phi;
		List<RealMatrix> phiAll = model.getPhi(); 
		List<Integer> docWords;
		Utilities utils = new Utilities();
		
		// estimate beta
		for(int i=0; i<nbrTopics; i++){
			for(int j=0; j<vocabSize; j++){
				
				b = 0; 
				for(int d=0; d<nbrDocs; d++){
					doc = documents.get(d);
					docWords = doc.getWordIds();
					nbrWords = doc.getDocSize();
					phi = phiAll.get(d);
					
					for(int n=0; n<nbrWords; n++){
						if(docWords.get(n) == j)
							b += phi.getEntry(i, n);
					}
				}
				beta.setEntry(i, j, b);
			}
			//System.out.println("Beta estimation: Topics done : " + i);
			// Normalize the beta row
			beta.setRow(i, utils.normalize(beta.getRow(i)));
		}
		
		// Estimate alpha by Newton-Raphson
		alpha = utils.performNR(conf, alpha, model.getGamma());
		
		// Update the model
		model.setBeta(beta);
		model.setAlpha(alpha);
		
	}
	
}
