package lda;

import java.util.List;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.stat.StatUtils;

/*
 * This module carries out variational inference to learn the
 * optimizing values of the variational parameters \gamma
 * and \phi. This step is used in the E-step of the main 
 * variational EM algorithm in model estimation.
 */

public class InferenceBlock {
	
	// do variational inference on a document, passing the current state of model
	public void infer(Document doc, Model model, Configs conf){
		
		// This is the variational inference algorithm in the LDA paper
		
		int docIndex = doc.getDocID();
		int nTops = model.getNbrTopics();
		int nWords = doc.getDocSize();
		RealVector alpha = model.getAlpha();
		RealMatrix beta = model.getBeta();
		RealMatrix phi = model.getPhi().get(docIndex);
		RealVector gamma = model.getGamma().get(docIndex);
		List<Integer> words = doc.getWords();
		Utilities utils = new Utilities();
		int wordindex;
		

		// Do variational inference until convergence
		RealVector phiRow;
		while(true){									// condition for convergence
			for(int n=0; n<nWords; n++){
				phiRow = phi.getRowVector(n);
				for(int i=0; i<nTops; i++){
					wordindex = words.get(i);
					phiRow.setEntry(i, (beta.getRowVector(i).getEntry(wordindex) * Math.exp(utils.psi(gamma.getEntry(i)))));
				}
				// normalize phiRow and set it back to phi
				phi.setRow(n, StatUtils.normalize(phiRow.toArray()));
				
			}
			// update \gamma
			gamma = alpha.add(utils.matSumByCol(phi));
			
		}
		
		
				
	}

}
