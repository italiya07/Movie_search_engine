package moviesproject;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import common.Comparison;
import graphs.Graph;
import graphs.SymbolGraph;

public class MovieSuggestor {
	// Displays suggestions based on a movie
	@SuppressWarnings("unchecked")
	public static void showSuggestionsBasedOnMovie(GraphProcessingUtils customGraph, JSONObject movieForSuggestion) {

		// retrieves cast array from movie's json object
		JSONArray jsonArrayOfCast = (JSONArray) movieForSuggestion.get("cast");
		// converts json array to string array
		String[] stringArrayOfCast = new String[jsonArrayOfCast.size()];
		for (int i = 0; i < jsonArrayOfCast.size(); i++) {
			stringArrayOfCast[i] = jsonArrayOfCast.get(i).toString();
		}

		// retrieves genres array from movie's json object
		JSONArray jsonArrayOfGenres = (JSONArray) movieForSuggestion.get("genres");
		// converts json array to string array
		String[] stringArrayOfGenres = new String[jsonArrayOfGenres.size()];
		for (int i = 0; i < jsonArrayOfGenres.size(); i++) {
			stringArrayOfGenres[i] = jsonArrayOfGenres.get(i).toString();
		}

		// retrieves movies of all the actors listed in movie we use for suggestion,
		// from symbol graph of actorsAndMovies
		Set<String> listBasedOnActors = findMoviesInSymbolGraphOfActor(customGraph.getSymbolGraphForMoviesWithCast(),
				customGraph.getGraphForMoviesWithCast(), stringArrayOfCast);
		// retrieves movies of all the genres listed in movie we use for suggestion,
		// from symbol graph of actorsAndMovies
		Set<String> listBasedOnGenre = findMoviesInSymbolGraphOfGenre(customGraph.getSymbolGraphForMoviesWithGenre(),
				customGraph.getGraphForMoviesWithGenre(), stringArrayOfGenres);

		Set<String> suggestedMovies;
		// if both the list are empty then no movies can be suggested.
		if (listBasedOnGenre.isEmpty() && listBasedOnActors.isEmpty()) {
			System.out.println("Sorry. No suggestions found related to this movie.");
			return;
		} else if (listBasedOnActors.isEmpty()) {
			// if list based on actors is empty then list based on genre will be shown in
			// suggestion.
			suggestedMovies = listBasedOnGenre;
		} else if (listBasedOnGenre.isEmpty()) {
			// if list based on genres is empty then list based on actors will be shown in
			// suggestion.
			suggestedMovies = listBasedOnActors;
		} else {
			// if both list are present, common movies from both the list will be shown.
			suggestedMovies = listBasedOnGenre;
			suggestedMovies.retainAll(listBasedOnActors);
		}


		JSONArray jsonMovieArr = new JSONArray();
		// retrieve movie details of all the suggested movies
		for (String movieName : suggestedMovies) {
			jsonMovieArr.add(customGraph.arrayOfMoviesWithDetails.get(GraphProcessingUtils.getCodeOfMovie(movieName)));
		}
		// create a movie class array from that json array for further refinement of
		// suggestion
		Movie[] movieArr = GraphProcessingUtils.createArrayForMovies(jsonMovieArr);

		// movies will be sorted based on rating, number of genres, number of cast
		// present in the movie.
		Arrays.sort(movieArr, new Comparator<Movie>() {
			@Override
			public int compare(Movie movie1, Movie movie2) {
				return Comparison.customCompare(movie1,movie2)	;
			}
		});
		
		// display suggested movies
		System.out.println("=================================");
		System.out.println("Suggested Movies:");
		for (int i = 0; i < (movieArr.length >= 6 ? 6 : movieArr.length); i++) {
			if (GraphProcessingUtils.getCodeOfMovie(movieForSuggestion.get("movieName").toString()) == GraphProcessingUtils
					.getCodeOfMovie(movieArr[i].getMovieName())) {
				continue;
			}
			System.out.println(
						"=> " + GraphProcessingUtils.getCodeOfMovie(movieArr[i].getMovieName()) + ": " + movieArr[i].getMovieName());
		}
		System.out.println("=================================");

		// loop over for more suggestions from any of the suggested movies.
		System.out.println("Do you want more suggestions?");
		System.out.println("1: Yes");
		System.out.println("2: No");
		System.out.print("Enter your choice: ");
		Scanner sc = new Scanner(System.in);

		int wantSuggestionAgain = sc.nextInt();
		if (wantSuggestionAgain == 1) {
			System.out.print("Enter the movie code as listed along side movie name: ");
			int movieCode = sc.nextInt();
			showSuggestionsBasedOnMovie(customGraph, (JSONObject) customGraph.arrayOfMoviesWithDetails.get(movieCode));
		}

	}

	// method will find movies of all the actors listed in movie we use for
	// suggestion, from symbol graph of actorsAndMovies
	private static Set<String> findMoviesInSymbolGraphOfActor(SymbolGraph sg, Graph G, String[] actors) {
		Set<String> commonMovies = new HashSet<>();
		for (String actor : actors) {
			actor = actor.toLowerCase();
			if (sg.contains(actor)) {
				for (int movie : G.adj(sg.index(actor))) {
					commonMovies.add(sg.name(movie));
				}
			}
		}

		return commonMovies;
	}

	// find movies of all the genres listed in movie we use for suggestion,
	// from symbol graph of actorsAndMovies
	private static Set<String> findMoviesInSymbolGraphOfGenre(SymbolGraph sg, Graph G, String[] genres) {
		Set<String> commonMovies = new HashSet<>();
		for (String genre : genres) {
			if (sg.contains(genre)) {
				for (int movie : G.adj(sg.index(genre))) {
					commonMovies.add(sg.name(movie));
				}

			}
		}

		return commonMovies;
	}

}
