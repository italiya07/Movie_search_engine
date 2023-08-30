package moviesproject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;

import common.Comparison;

public class TopMovies {

	// A function to display menu to the user
	public static int showMenu() {
		boolean stayInLoop = true;
		int numberOfMovies = 0;
		while(stayInLoop) {			
			Scanner sc = new Scanner(System.in);
			System.out.println("\nEnter number of movies that you want to get between 1 to 50:");
			numberOfMovies = sc.nextInt();
			if (numberOfMovies > 0 && numberOfMovies < 51) {
				stayInLoop = false;
			}
			else {
				System.out.print("\nEnter correct number.");
			}
		}
		return numberOfMovies;
	}
	
	// A function to fetch movies
	public static void fetchMovies(Movie[] arr) {

			Scanner scannerObj = new Scanner(System.in);
	
			System.out.println("\nDo you want to fetch details of particular movie");
			System.out.println("1: Yes");
			System.out.println("2: NO");
			System.out.println("Enter your choice:");
			int fetchMoiveDetails = scannerObj.nextInt();

			switch (fetchMoiveDetails) {
			case 1:
				System.out.println();
				System.out.print("Enter the movie code: ");
				int movieCode = (scannerObj.nextInt()) - 1;
				System.out.println();
				System.out.println("********** Details of Movie **********");

				Movie movie = arr[movieCode];

				System.out.println();

				System.out.println("Name 	   : " + movie.getMovieName());
				System.out.println("Description: " + movie.getMovieDesc());
				System.out.println("Duration   : " + movie.getMovieDuration());
				System.out.println("Release    : " + movie.getMovieRelease());
				System.out.println("Rating	   : " + movie.getRating());
				System.out.println("Genres	   : " + movie.getMovieGenres());
				System.out.println("Cast	   : " + movie.getMovieCast());
				break;
				
			case 2:
				break;

			default:
				System.out.println("Please enter a valid choice");
				break;
			}
	}

	// A function to print the top movies which are sorted
	public static void printSortedMovies(int x, Movie[] arr) {
		for (int i = 0; i < x; i++) {
			System.out.println((i + 1) + " : " + arr[i].getMovieName() + " : Rating: " + arr[i].getRating());
		}
		fetchMovies(arr);
	}

	
	// Entry point of this file
	public void main(GraphProcessingUtils customGraph) {

		// Display menu and get number of movies from user.
		int numberOfMovies = showMenu();

		Movie[] arr = GraphProcessingUtils.createArrayForMovies(customGraph.arrayOfMoviesWithDetails);

		// important sorting done in main using comparator and Arrays.sort
		// Here, the rating is considered first, if rating is same, then number of genres and then number of cast is considered
		Arrays.sort(arr, new Comparator<Movie>() {
			@Override
			public int compare(Movie movie1, Movie movie2) {
				return Comparison.customCompare(movie1,movie2)	;
			}
		});
		
		// Call the function to print the sorted movies
		printSortedMovies(numberOfMovies, arr);

	}

}