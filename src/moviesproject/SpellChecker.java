package moviesproject;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import common.Sequences;

public class SpellChecker {
	

	public static int byActor(GraphProcessingUtils customGraph, String nameOfActor) {
		int i = 0;	
		
		// Create set based on unique cast name from custom graph 
		Set<String> uniquecasts = customGraph.getUniqueCast();
		
		// To iterate through set of cast
		Iterator<String> castIterator = uniquecasts.iterator();
		
		// Create HashSet to store result of spell checking
		HashSet<String> result = new HashSet<String>();
		
		// Iterate through castIterator set till it reach to last value
		while(castIterator.hasNext()) {
			String actor = castIterator.next();
			
			// Create string array of actor name splited by space 
			String[] splitedactor = actor.trim().split(" ");
			
			//Create string array of user input splited by space 
			String[] splitedInputActor = nameOfActor.trim().split(" ");
			
			// If splited array of user input length is grater than 1
			if(splitedInputActor.length>1) {
				// call the edit distance method, and if the returned edit distance is less than 4
				 if(Sequences.editDistance(actor.toLowerCase(), nameOfActor) < 4) {
					 // add actor name into result hashset
		    		result.add(actor);
		    		 i++;
		    		 // To show first 9 results of edit distance
		    		 if(i == 9) {
		    			 castIterator = Collections.emptyIterator();
		    		 }
		    	 }
			}
			
		}
		// if HashSet of result has values
		if (!result.isEmpty()) {
			System.out.println("Do you mean by....");
		}
		
		// Iterate through results and print all suggestion based on spell checking
		for(String s : result) {
			System.out.println(s);
		}
		return i;
	}
	
	public static int bymoviename(GraphProcessingUtils customGraph, String nameOfMovie) {
		int i = 0;
		
		// Create list based on all movie names from custom graph 
		List<String> allmovies = customGraph.getAllMovieNames();

		// Create HashSet to store result of spell checking
		HashSet<String> result = new HashSet<String>();
		Iterator<String> movies = allmovies.iterator();
		
		// Iterate through movies list till it reach to last value
		while(movies.hasNext()) {
			String moviename = movies.next();
			
			// Create string array of movie name split by space 
			String[] splitedmovienames = moviename.trim().split(" ");
			//Create string array of user input split by space 
			String[] splitedInputMovieName = nameOfMovie.trim().split(" ");
			
			// If split array of user input length is grater than 1
			if(splitedInputMovieName.length > 1) {
				// call the edit distance method, and if the returned edit distance is less than 4
				 if(Sequences.editDistance(moviename.toLowerCase(), nameOfMovie) < 4) {
		    		 result.add(moviename);
		    		 i++;
		    		 if(i == 9) {
		    			 movies = Collections.emptyIterator();
		    		 }
		    	 }
			}else {
				// Iterate through split movie names array
				for(String word : splitedmovienames) {
					// If length of user input is grater than 5 and edit distance is less than 3 then
						 if(nameOfMovie.length()>5 && Sequences.editDistance(word.toLowerCase(), nameOfMovie) < 3) {
							 // add actor name into result HashSet
							 result.add(moviename);
				    		 i++;
				    		 if(i == 9) {
				    			 movies = Collections.emptyIterator();
				    		 }
				    	 }
						// If length of user input is grater than 3 and edit distance is less than 2 then
						 else if(nameOfMovie.length()>3 && Sequences.editDistance(word.toLowerCase(), nameOfMovie) < 2) {
							 result.add(moviename);
				    		 i++;
				    		 if(i == 9) {
				    			 movies = Collections.emptyIterator();
				    		 }
				    	 }
				}
			}
		}
		
		if (!result.isEmpty()) {
			System.out.println("Do you mean by....");
		}
		for(String s : result) {
			System.out.println(s);
		}
		return i;
	}

	
	
	public static int byGenre(GraphProcessingUtils customGraph, String nameOfGenre) {
		int i = 0;
		
		// Create set based on unique genres name from custom graph
		Set<String> allGenres = customGraph.getAllGenres();

		
		HashSet<String> result = new HashSet<String>();
		Iterator<String> genres = allGenres.iterator();
		
		// Iterate through genre set till it reach to last value
		while(genres.hasNext()) {
			String genre = genres.next();
			
			// Create string array of genre name splited by space 
			String[] splitedGenre = genre.trim().split(" ");
			//Create string array of user input splited by space 
			String[] splitedInputGenre = nameOfGenre.trim().split(" ");
			
			// If splited array of user input length is grater than 1
			if(splitedInputGenre.length>1) {
				 if(Sequences.editDistance(genre.toLowerCase(), nameOfGenre) < 3) {
		    		 result.add(genre);
		    		 i++;
		    		 if(i == 9) {
		    			 genres = Collections.emptyIterator();
		    		 }
		    	 }
			}
		}
		if (!result.isEmpty()) {
			System.out.println("Do you mean by....");
		}
		for(String s : result) {
			System.out.println(s);
		}
		return i;
	}

}
