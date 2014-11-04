package tests.ldaTests;
import java.io.File;
import java.util.List;
import main.lda.*;

public class CorpusTest {
	
	/************************************************************/
	public static void main(String[] args){
		// Path to the file
		String corpusPath = "data/summariesTrain.txt";
		String vocabPath = "data/sortedVocab.txt";
		
		Corpus movieSummaries;
		Vocabulary movieVocab;
		
		//Creating the file to read the documents from
		File documentFile = new File(corpusPath);
		File vocabFile = new File(vocabPath);
		
		movieSummaries = new Corpus(documentFile);
		movieVocab = new Vocabulary(vocabFile);
		
		System.out.println("Number of movie summaries read : " + movieSummaries.getNbrDocs());
		System.out.println("Successfully read " + movieVocab.getVocabSize() + " words from vocabulary!\n");
		
		List<Document> summaries = movieSummaries.getDocs();
		
		for(int i=0; i<2; i++){
				Document doc = summaries.get(i);
				System.out.println("Movie id: " + doc.getDocId());
				System.out.println(doc.getWordIds());
				System.out.println(doc.readDoc(movieVocab));
				System.out.println("Review size: " + doc.getDocSize() + "\n");
		}
	}
}
