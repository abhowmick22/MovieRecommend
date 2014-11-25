package main.lda;

import java.util.ArrayList;
import java.util.List;

/*
 * This is the main module that models the corpus, using variational
 * EM. This class provides the API that the CTR model interacts with.
 */

/*
 * The CTR client will first ask for the topic model, this contains the \alpha_k
 * \beta_kXV matrix, which is list of topics, each topic being a distribution
 * over words in the vocabulary (also returned).
 * Next, it calls getFeatures for a new document - this returns the top
 * k topics (features) of the document as a vector that
 * will be used to characterise the document(movie). This works as follows -
 * given \alpha and \beta as the parameters of the model, we infer the posterior
 * Dirichlet parameters \gamma_k associated with the document. 
 */

public class TopicModeler {
	

	// Model a test document as list of topics
	public ArrayList<Double> getFeatures(Document doc, Model model){
		
		return null;
		
	}
	
	// Model the provided corpus
	public Model modelCorpus(Corpus corpus, Configs conf, Vocabulary vocab){
		
		// Initiate a model
		Model model = new Model();
		model.initModel(corpus, conf, vocab);
		
		// Do the E-M algorithm
		double prevLikelihood = 0;
		double likelihood = 0;
		double convergence = 1;
		double emConv = conf.getEmConvergence();
		int iters = 0;
		int maxIters = conf.getEmIters();
		int nDocs = corpus.getNbrDocs();
		InferenceBlock infBlock = new InferenceBlock();
		EstimatorBlock estBlock = new EstimatorBlock();
		List<Document> docs = corpus.getDocs();
		
		while((iters < maxIters) && (convergence > emConv)){

			// E-step for each document
			// update the variational parameters in the model
			likelihood = 0;
			System.out.println("Running inference on documents...");
	
			for(int i=0; i<nDocs; i++){
				if(i%200 == 0){
					System.out.format("Running inference on document : %d\n", i);
				}
				
				likelihood += infBlock.infer(docs.get(i), model, conf);	
			}

			System.out.println(likelihood);
			//System.out.println(likelihood);
			//System.out.println("\n\n\n");
			// M-step
			estBlock.estimate(corpus, model, conf);

			// calculate and check for convergence
			convergence = Math.abs((likelihood - prevLikelihood) / prevLikelihood); 
			prevLikelihood = likelihood;
			iters++;
		}
		
		return model;
	}
	
	// Return the performance metrics
	public Metrics getMetrics(){
		
		return null;
	}
	
}
