package main.collabfilter;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import org.la4j.vector.Vector;
import org.la4j.vector.dense.BasicVector;

class Rating {
	int userId;
	int movieId;
	double rating;
	
	Rating(int userId, int movieId, double rating) {
		this.userId = userId;
		this.movieId = movieId;
		this.rating = rating;
	}
	
	int getUserId() {
		return userId;
	}
	
	int getMovieId() {
		return movieId;
	}
	
	double getRating() {
		return rating;
	}
}

public class CollaborativeFiltering {

	static int numUsers = 71567 + 1;
	static int numMovies = 65133 + 1; //10681 + 1;
	
	static int position = 0;
	
	static java.util.Vector<Rating> readRatings(String filename) {
		java.util.Vector<Rating> ratings = new java.util.Vector<Rating>(numMovies, numMovies);
		
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader("data/" + filename));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/* 
		 * Read the data from the files into the rating "matrix" 
		 */
		String line = null;
		try {
			while ((line = br.readLine()) != null) {
				String[] parsedLine = line.split("::");
				int userId = Integer.parseInt(parsedLine[0]);
				int movieId = Integer.parseInt(parsedLine[1]);
				double rating = Double.parseDouble(parsedLine[2]);
				
				ratings.addElement(new Rating(userId, movieId, rating));
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		ratings.trimToSize();
		
		try {
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ratings;
	}
	
	static double rmse(Vector[] users, Vector[] movies, java.util.Vector<Rating> ratings) {
		
		double rmse = 0;
		for (int i=0; i<ratings.size(); i++) {
			Rating r = ratings.get(i);
			int userId = r.getUserId();
			int movieId = r.getMovieId();
			double rating = r.getRating();
			
			rmse += Math.pow(users[userId].innerProduct(movies[movieId]) - rating, 2);
		}
		
		rmse = rmse/ratings.size();
		
		return Math.sqrt(rmse);
	}
	
	public static void main(String[] args) {
		
		/* MODEL PARAMETERS
		 * gamma - learning rate of the gradient descent
		 * 		kappa - forgetting rate
		 * 		tau - normalization
		 * lambda - regularization coefficient
		 * numFactors - dimension of the user and movie vectors
		 * numIters - number of iterations the gradient descent runs for
		 * batchSize - size of the batches chosen in the gradient descent
		 */
		double gamma;
			double kappa = 0.8;
			double tau = 150;
		double lambda = 0.02;
		int numFactors = 18;
		int numIters = 100;
		int batchSize = 100;
		
		Random rand = new Random();
		
		if (args.length < 2) {
			System.out.println("Please supply training and test file names");
			System.exit(1);
		}
		
		java.util.Vector<Rating> ratings = readRatings(args[0]);
		java.util.Vector<Rating> testRatings = readRatings(args[1]);
		
		/* 
		 * Create new users and movies "matrices" and
		 * initialize them randomly
		 */
		Vector[] users = new Vector[numUsers];
		Vector[] movies = new Vector[numMovies];
		
		for (int i=0; i<numUsers; i++) {
			users[i] = new BasicVector(numFactors);
			for (int j=0; j<numFactors; j++) {
				users[i].set(j, rand.nextDouble());
			}
		}
		
		for (int i=0; i<numMovies; i++) {
			movies[i] = new BasicVector(numFactors);
			for (int j=0; j<numFactors; j++) {
				movies[i].set(j, rand.nextDouble());
			}
		}
		
		System.out.println("Training RMSE before: " + rmse(users, movies, ratings));
		System.out.println("Test RMSE before: " + rmse(users, movies, testRatings));
		
		//System.out.println(users[7].innerProduct(movies[1284]));
		//System.out.println(rmse(users, movies, ratings));
		
		/* 
		 * Gradient descent algorithm
		 */
		for (int iter=0; iter<numIters; iter++) {
			gamma = Math.pow(tau + iter, -kappa);
			//gamma = 0.25;
			Collections.shuffle(ratings);
			System.out.println(iter);
			
			for (int i=0; i<ratings.size(); i+=batchSize) {
				
				HashMap<Integer, Vector> userTable = new HashMap<Integer, Vector>();
				HashMap<Integer, Vector> movieTable = new HashMap<Integer, Vector>();
				
				java.util.List<Rating> batch = null;
				if (i+batchSize >= ratings.size()) {
					batch = ratings.subList(i, ratings.size());
				} else {
					batch = ratings.subList(i, i+batchSize);
				}
				
				Iterator<Rating> rIter = batch.iterator();
				while (rIter.hasNext()) {
					Rating r = rIter.next();
					int userId = r.getUserId();
					int movieId = r.getMovieId();
					
					double error = r.getRating() - users[userId].innerProduct(movies[movieId]);
					
					//System.out.println(error);
					
					Vector userUpdate = movies[movieId].multiply(error).subtract(users[userId].multiply(lambda));
					Vector movieUpdate = users[userId].multiply(error).subtract(movies[movieId].multiply(lambda));
					
					if (userTable.containsKey(userId)) {
						userTable.put(userId, userTable.get(userId).add(userUpdate));
					} else {
						userTable.put(userId, userUpdate);
					}
					
					if (movieTable.containsKey(movieId)) {
						movieTable.put(movieId, movieTable.get(movieId).add(movieUpdate));
					} else {
						movieTable.put(movieId, movieUpdate);
					}
				}
				
				Iterator<Integer> userIds = userTable.keySet().iterator();
				Iterator<Integer> movieIds = movieTable.keySet().iterator();
				
				while (userIds.hasNext()) {
					int id = userIds.next();
					users[id] = users[id].add(userTable.get(id).multiply(gamma));
				}
				
				while (movieIds.hasNext()) {
					int id = movieIds.next();
					movies[id] = movies[id].add(movieTable.get(id).multiply(gamma));
				}
				
				userTable.clear();
				movieTable.clear();
			}
			
			//System.out.println(users[7].innerProduct(movies[1284]));
			//System.out.println(rmse(users, movies, ratings));
		}
		System.out.println("Training RMSE: " + rmse(users, movies, ratings));
		System.out.println("Test RMSE: " + rmse(users, movies, testRatings));
	}
}