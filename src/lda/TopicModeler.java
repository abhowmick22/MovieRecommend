package lda;

import java.util.ArrayList;

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
	public Model modelCorpus(Corpus corpus, Configs conf){
		
		return null;
	}
	
	// Return the performance metrics
	public Metrics getMetrics(){
		
		return null;
	}
	
}
