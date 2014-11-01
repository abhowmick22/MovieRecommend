package lda;

import java.util.List;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

/*
 * This module carries out variational inference to learn the
 * optimizing values of the variational parameters \gamma
 * and \phi. This step is used in the E-step of the main 
 * variational EM algorithm in model estimation.
 */

public class InferenceBlock {
	
	// do variational inference on a document, passing the current state of model
	public void infer(Document doc, Model model, Configs conf){
		
		// This is the variationl inference algorithm in the LDA paper
		
		int docIndex = doc.getDocID();
		int nTops = model.getNbrTopics();
		int nWords = doc.getDocSize();
		RealVector alpha = model.getAlpha();
		RealMatrix beta = model.getBeta();
		List<Integer> words = doc.getWords();
		Utilities utils = new Utilities();
		int wordindex;
		
		// initialize \phi and \gamma for this document
		RealVector phiRow = null;
		RealVector gamma = null;
		
		for(int i=0; i<nTops; i++)	{
			phiRow = phiRow.append((double) (1/nTops));
			gamma = alpha.mapAdd((double) (nWords/nTops));
		}
		
		RealMatrix phi = new Array2DRowRealMatrix(nWords, nTops);
		for(int j=0; j<nWords; j++)		phi.add(phiRow);
		
		// Do variational inference until convergence
		while("condition for convergence"){
			for(int n=0; n<nWords; n++){
				phiRow = phi.get(n);
				for(int i=0; i<nTops; i++){
					wordindex = words.get(i);
					phiRow.set(i, beta.get(i).get(wordindex) * 1);
				}
				// normalize phiRow
				
				
				// set phiRow back
				phi.set(n, phiRow);
			}
			// update \gamma
			
		}
		
		
				
	}

}
