package lda;

import java.util.List;

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
		
		// initialize \phi for this document
		int docIndex = doc.getDocID();
		int nTops = model.getNbrTopics();
		
		List<Double> phiRow;
		for(int i=0; i<nTops; i++)	phiRow.add((double) (1/nTops));
		List<List<Double> > phi;
		for(int j=0; j<)	phi.add(phiRow);
		
		for(){
			for(){
				
			}
		}
		
	}

}
