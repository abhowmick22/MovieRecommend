package main.lda;

import java.util.List;

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
	// This returns the likelihood of the document at which convergence happens
	// The returned likelihood is used by the EM algorithm to test its convergence
	public double infer(Document doc, Model model, Configs conf){
		
		// This is the variational inference algorithm in the LDA paper
		
		int docIndex = doc.getDocId();
		int nTops = model.getNbrTopics();
		int nWords = doc.getDocSize();
		int vocabSize = model.getVocabSize();
		RealVector alpha = model.getAlpha();
		RealMatrix beta = model.getBeta();
		RealMatrix phi = model.getPhi().get(docIndex);
		RealVector gamma = model.getGamma().get(docIndex);
		List<Integer> words = doc.getWordIds();
		Utilities utils = new Utilities();
		int wordindex;
		double likelihood = 0;
		double prevLikelihood = 0;
		double convergence = 1;
		int iters = 0;
		double alphaSum = 0, gammaSum = 0;
		double C1, C2, C3, C4, C5, C6, C7, C8, C9;
		C1 = C2 = C3 = C4 = C5 = C6 = C7 = C8 = C9 = 0;
		
		// Do variational inference until convergence
		RealVector phiRow;
		while((iters < conf.getVarIters()) || (convergence > conf.getVarConvergence())){
			for(int n=0; n<nWords; n++){
				phiRow = phi.getRowVector(n);
				for(int i=0; i<nTops; i++){
					wordindex = words.get(i);
					phiRow.setEntry(i, (beta.getRowVector(i).getEntry(wordindex) * 
											Math.exp(utils.diGamma(gamma.getEntry(i)))));
				}
				// normalize phiRow and set it back to phi
				phi.setRow(n, utils.normalize(phiRow.toArray()));
				
			}
			
			// update \gamma
			gamma = alpha.add(utils.matSumByCol(phi));
			
			// TODO : Reflect changes back in the model
			// Update the model
			model.setGammaSingle(gamma, docIndex);
			model.setPhiSingle(phi, docIndex);
			
			/* 
			 * Calculate likelihood terms
			 */
			
			alphaSum = StatUtils.sum(alpha.toArray());
			gammaSum = StatUtils.sum(gamma.toArray());
			
			// C1
			C1 += utils.logGamma(alphaSum);
			
			// C2
			for(int i=0; i<nTops; i++)	
				C2 += utils.logGamma(alpha.getEntry(i));
			
			// C3
			for(int i=0; i<nTops; i++)
				C3 += (alphaSum-nTops) * (utils.diGamma(gamma.getEntry(i)) - 
							utils.diGamma(gammaSum));
			
			// C4
			for(int n=0; n<nWords; n++){
				for(int i=0; i<nTops; i++){
					C4 += phi.getEntry(n, i) * (utils.diGamma(gamma.getEntry(i))
							- utils.diGamma(gammaSum));
				}
			}
			
			// C5
			for(int n=0; n<nWords; n++){
				for(int i=0; i<nTops; i++){
					for(int j=0; j<vocabSize; j++){
						if(words.get(n) == j)
						C5 += phi.getEntry(n, i) * Math.log10(beta.getEntry(i, j));
						// TODO : Check if above interpretation is correct
					}
				}
			}
			
			// C6
			C6 += utils.logGamma(gammaSum);
			
			// C7
			for(int i=0; i<nTops; i++)
				C7 += Math.log10(gamma.getEntry(i));
			
			// C8
			for(int i=0; i<nTops; i++)
				C8 += (gamma.getEntry(i) - 1) * 
							(utils.diGamma(gamma.getEntry(i)) - utils.diGamma(gammaSum));
			
			// C9
			for(int n=0; n<nWords; n++){
				for(int i=0; i<nTops; i++){
					C9 += phi.getEntry(n, i) * Math.log10(phi.getEntry(n, i));
				}
			}
			
			// Calculate the likelihood
			likelihood += C1 - C2 + C3 + C4 + C5 - C6 + C7 - C8 - C9;
			
			// Find convergence
			convergence = Math.abs((likelihood - prevLikelihood) / prevLikelihood);
			iters++;
			prevLikelihood = likelihood;
		}
		
		return likelihood;
	}

}
