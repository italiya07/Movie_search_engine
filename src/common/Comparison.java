package common;

import moviesproject.Movie;

public class Comparison {
	
	// important sorting done in main using comparator and Arrays.sort
			// Here, the rating is considered first, if rating is same, then number of genres and then number of cast is considered
	public static int customCompare(Movie movie1, Movie movie2) {
		try {
			
			// Both movies have have same number of rating sort based on number of genres
			if (movie1.getRating() == movie2.getRating()) {
				if (movie1.getMovieGenres().size() < movie2.getMovieGenres().size()) {
					return 1;
				} 
				
				// Number of genres of both the movies is also same, sort based on number of cast.
				else if ((movie1.getMovieGenres().size() == movie2.getMovieGenres().size())) {
					if (movie1.getMovieCast().size() < movie2.getMovieCast().size()) {
						return 1;
					} else {
						return -1;
					}
				} 
				else {
					return -1;
				}
			} 
			
			//	Rating is different, sort accordingly
			else if (movie1.getRating() < movie2.getRating()) {
				return 1;
			} 
			else {
				return -1;
			}
		} catch (Exception e) {
			return 0;
		}
	}
}
