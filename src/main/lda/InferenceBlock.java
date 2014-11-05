package main.lda;

import java.util.List;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.special.Gamma;
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
		List<Integer> words = doc.getWordIds();
		Utilities utils = new Utilities();
		int wordindex;
		double likelihood = 0;
		double prevLikelihood = 0;
		double convergence = 1;
		int iters = 0;
		double alphaSum = 0, gammaSum = 0;
		double C1, C2, C3, C4, C5, C6, C7, C8, C9;
	
		// Get alpha and beta
		RealVector alpha = model.getAlpha();
		RealMatrix beta = model.getBeta();
		
		// Initialize phi
		RealMatrix phi = new Array2DRowRealMatrix(nTops, nWords);
		phi = phi.scalarAdd(1.0/(double) nTops);
		
		// Initialize gamma
		RealVector gamma = new ArrayRealVector(nTops, nWords/(double)nTops);
		gamma = alpha.add(gamma);
		
		// Do variational inference until convergence
		RealVector phiCol;
		while((iters < conf.getVarIters()) && (convergence > conf.getVarConvergence())){
			C1 = C2 = C3 = C4 = C5 = C6 = C7 = C8 = C9 = 0.0;

			for(int n = 0; n < nWords; n++){
				wordindex = words.get(n);
				
				phiCol = phi.getColumnVector(n);
				for(int i=0; i<nTops; i++){
					phiCol.setEntry(i, (beta.getEntry(i, wordindex) *
											Math.exp(utils.diGamma(gamma.getEntry(i)))));
				}
				// normalize phiCol and set it back to phi
				phi.setColumn(n, utils.normalize(phiCol.toArray()));
				
			}
			
			// update \gamma
			gamma = alpha.add(utils.matSumAlongDim(phi, 2));
			//System.out.println("alpha " + alpha + " \n");
			//System.out.println(gamma + " \n" + iters);
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
			for(int i=0; i<nTops; i++){	
				C2 += utils.logGamma(alpha.getEntry(i));
			}
			
			// C3
			for(int i=0; i<nTops; i++)
				C3 += (alpha.getEntry(i)-1) * (utils.diGamma(gamma.getEntry(i)) - 
							utils.diGamma(gammaSum));
			
			// C4
			for(int n=0; n<nWords; n++){
				for(int i=0; i<nTops; i++){
					C4 += phi.getEntry(i, n) * (utils.diGamma(gamma.getEntry(i))
							- utils.diGamma(gammaSum));
					
					//if(phi.getEntry(i,n) > 1)
						//System.out.println(phi.getEntry(i,n));
				}
			}
			
			// C5
			for(int n=0; n<nWords; n++){
				for(int i=0; i<nTops; i++){
					for(int j=0; j<vocabSize; j++){
						if(words.get(n) == j)
						C5 += phi.getEntry(i, n) * Math.log10(beta.getEntry(i, j));
						
						// TODO : Check if above interpretation is correct
					}
				}
			}
			
			// C6
			C6 += utils.logGamma(gammaSum);
			
			// C7
			for(int i=0; i<nTops; i++)
				C7 += utils.logGamma(gamma.getEntry(i));
			
			// C8
			for(int i=0; i<nTops; i++)
				C8 += (gamma.getEntry(i) - 1) * 
							(utils.diGamma(gamma.getEntry(i)) - utils.diGamma(gammaSum));
			
			// C9
			for(int n=0; n<nWords; n++){
				for(int i=0; i<nTops; i++){
					C9 += phi.getEntry(i, n) * Math.log10(phi.getEntry(i, n));
				}
			}
			
			
			// Debugging messages
			/*System.out.println(C1);
			System.out.println(C2);
			System.out.println(C3);
			*/
			
			//System.out.println(C4);
			//System.out.println(C5);
			//System.out.println(C6);
			//System.out.println(C7);
			//System.out.println(C8);
			//System.out.println(C9 + "\n\n");
			
			
			// Calculate the likelihood
			likelihood = C1 - C2 + C3 + C4 + C5 - C6 + C7 - C8 - C9;
			
			// Find convergence
			convergence = Math.abs((likelihood - prevLikelihood) / prevLikelihood);
			iters++;
			prevLikelihood = likelihood;
		}
		
		return likelihood;
	}
}
