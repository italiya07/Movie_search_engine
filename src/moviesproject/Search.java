package moviesproject;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.Set;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import common.Comparison;
import graphs.Graph;
import graphs.SymbolGraph;


public class Search {

	// method to display movies menu
	public static void showMoviesMenu() {
		System.out.println();
		System.out.println("=====+=====+===== Search Menu =====+=====+=====");
		System.out.println();

		System.out.println("1: Search Movies by Actor Name");
		System.out.println("2: Search Movies by Movie name or keyword");
		System.out.println("3: Search Movies by Genre");
		System.out.println("4: Go Back");
		System.out.println();
	}
	
	
	
	

	// method to search movie by actor name
	public static void searchMovieByActorName(GraphProcessingUtils customGraph) {
		System.out.println();
		Scanner sc = new Scanner(System.in);

		System.out.print("Enter the name of actor (full name): ");
		String nameOfActor = ((String) sc.nextLine()).strip().toLowerCase();

		System.out.println();

		// Check if the actor is present
		boolean actorIsPresent = customGraph.getSymbolGraphForMoviesWithCast().contains(nameOfActor);

		if (actorIsPresent) {
			Iterable<Integer> listOfMovies = customGraph.getGraphForMoviesWithCast()
					.adj(customGraph.getSymbolGraphForMoviesWithCast().index(nameOfActor));
			
			Movie[] arr = GraphProcessingUtils.createArrayForMoviesFromItr(listOfMovies);
			
			
			Arrays.sort(arr, new Comparator<Movie>() {
				@Override
				public int compare(Movie movie1, Movie movie2) {
					return Comparison.customCompare(movie1,movie2)	;
				}
			});
			
			
			
			for (int i = 0; i < arr.length; i++) {
				try {					
					System.out.println(GraphProcessingUtils.getCodeOfMovie(arr[i].getMovieName()) + " : " + arr[i].getMovieName() + " : Rating: " + arr[i].getRating());
				} catch (Exception e) {}
			}
			
			System.out.println();
			fetchMovie(customGraph);

		} 
		
		// Actor is not present, check for spelling mistakes
		else {
			int exist = SpellChecker.byActor(customGraph, nameOfActor);
			if (exist == 0) {
				System.out.println("Could not find actor.");
			}
		}
	}
	
	// Fetch Movie Details
	public static void fetchMovie(GraphProcessingUtils customGraph) {
		
		Scanner sc = new Scanner(System.in);
		System.out.println("Do you want to fetch the details of a particular movie?");
		System.out.println("1: Yes");
		System.out.println("2: No");
		System.out.print("Enter your choice: ");
		int fetchDetails = sc.nextInt();
		
		if (fetchDetails == 1) {
			
			System.out.println();
			System.out.print("Enter the movie code as listed along side movie name: ");
			int movieCode = sc.nextInt();
			System.out.println();
			
			System.out.println("=====+=====+===== Details of Movie =====+=====+=====");
			JSONObject movieForSuggestion = customGraph.printMovieDetails(movieCode);
			System.out.println("Do you want movie suggestions based on this movie?");
			System.out.println("1: Yes");
			System.out.println("2: No");
			System.out.print("Enter your choice: ");
			
			int wantSuggestion = sc.nextInt();
			if (wantSuggestion == 1) {
				MovieSuggestor.showSuggestionsBasedOnMovie(customGraph, movieForSuggestion);
			}
		}
	}
	
	// Method to search movie by movie name
	public static void searchMovieByMovieName(GraphProcessingUtils customGraph) {
		System.out.println();
		Scanner sc = new Scanner(System.in);

		System.out.print("Enter the name of Movie or a keyword: ");
		String word = sc.nextLine().strip().toLowerCase();
		System.out.println();

		Set<Integer> setOfMovies = customGraph.getMovieByKeyWord(word.toLowerCase(), customGraph);
		
		if (setOfMovies != null && setOfMovies.size() > 0) {
			
				
			Movie[] arr = GraphProcessingUtils.createArrayForMoviesFromSet(setOfMovies);
			
			
			Arrays.sort(arr, new Comparator<Movie>() {
				@Override
				public int compare(Movie movie1, Movie movie2) {
					return Comparison.customCompare(movie1,movie2)	;
				}
			});
			
			
			for (int i = 0; i < arr.length; i++) {
				try {					
					System.out.println(GraphProcessingUtils.getCodeOfMovie(arr[i].getMovieName()) + " : " + arr[i].getMovieName() + " : Rating: " + arr[i].getRating());
				} catch (Exception e) {}
			}
			
			System.out.println();
			fetchMovie(customGraph);
		}
		else {
			System.out.println("\n====================\nExact match not found...");
		}
	}

	// Method to search movie by genre
	public static void searchMovieByGenre(GraphProcessingUtils customGraph) {
		System.out.println();
		System.out.println("============== List of Genre ==============");
		
		// Get list of all genres and display it to the user
		Set<String> setOfGenres = customGraph.getAllGenres();
		Hashtable<Integer, String> genreIndex = new Hashtable<Integer, String>();
		int currentItr = 1;
		for (String i : setOfGenres) {
			System.out.println(currentItr + " : " + i);
			genreIndex.put(currentItr, i);
			currentItr += 1;
		}
		System.out.println();

		// Get selected genre from the user
		Scanner sc = new Scanner(System.in);
		System.out.print("Please choose a genre: ");
		String chosenGenre = genreIndex.get(sc.nextInt());
		System.out.println();

		// Get list of moives based on the provided genre and print it
		Iterable<Integer> listOfMovies = customGraph.getGraphForMoviesWithGenre()
				.adj(customGraph.getSymbolGraphForMoviesWithGenre().index(chosenGenre));
		
		
		Movie[] arr = GraphProcessingUtils.createArrayForMoviesFromItr(listOfMovies);
		
		
		Arrays.sort(arr, new Comparator<Movie>() {
			@Override
			public int compare(Movie movie1, Movie movie2) {
				return Comparison.customCompare(movie1,movie2)	;
			}
		});
		
		
		for (int i = 0; i < arr.length; i++) {
			try {
				System.out.println(GraphProcessingUtils.getCodeOfMovie(arr[i].getMovieName()) + " : " + arr[i].getMovieName() + " : Rating: " + arr[i].getRating());
			}
			catch(Exception e) {}
		}
		
		// A function which will ask if user wants to fetch movie detials or not
		fetchMovie(customGraph);

	}

	public void main(GraphProcessingUtils customGraph) {
		
		Scanner sc = new Scanner(System.in);

		boolean keepOnSameMenu = true;
		while(keepOnSameMenu) {			
			
			
			// show the menu for search based on different categories
			showMoviesMenu();
			System.out.print("Please enter your choice: ");
			int selectedOption = sc.nextInt();
			
			switch (selectedOption) {
			case 1:
				searchMovieByActorName(customGraph);
				break;
			case 2:
				searchMovieByMovieName(customGraph);
				break;
			case 3:
				searchMovieByGenre(customGraph);
				break;
			case 4:
				keepOnSameMenu = false;
				break;

			default:
				System.out.println();
				System.out.println("Please select from the provided options");
				main(customGraph);
				break;
			}
		}
	}

	
	
}
