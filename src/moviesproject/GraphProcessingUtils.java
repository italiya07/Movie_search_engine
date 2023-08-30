package moviesproject;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import graphs.Graph;
import graphs.SymbolGraph;
import graphs.TST;
import common.BoyerMoore;

public class GraphProcessingUtils {
	
	// Object declaration and initialization

	Set<String> uniqueCast = new HashSet<String>();
	Set<String> setOfGenres = new HashSet<String>();
	
	List<String> movieNames = new ArrayList<String>();
	List<String> collectionMovieAndCast = new ArrayList<String>();
	List<List<String>> collectionCastRespectToMovie = new ArrayList<List<String>>();
	List<List<String>> collectionMovieRespectToGenre = new ArrayList<List<String>>();
	
	static JSONArray arrayOfMoviesWithDetails = new JSONArray();
	
	static Hashtable<String, Integer> movieIndex = new Hashtable<String, Integer>();
	static SymbolGraph sgForMovieCast, sgForMovieGenre, sgForMovieActor;
	static Graph movieCastGraph, movieGenreGraph;

	TST<Set<Integer>> ternarySearchTrie = new TST<Set<Integer>>();
	
	Movie[] arrayForSorting;

	
	// A method to insert data in Ternary Search Trie
	public void putMovieInTrie(String nameOfMovie, int currentIndex) {
		String[] wordsInMovies = nameOfMovie.toLowerCase().split(" ");

		for (String word : wordsInMovies) {
			// Trie Leaf nodes will store the Set of movies
			Set<Integer> indexesOfMovies = new HashSet<Integer>();

			if (!ternarySearchTrie.contains(word)) {
				indexesOfMovies.add(currentIndex);
				ternarySearchTrie.put(word, indexesOfMovies);
			} else {
				for (int index : ternarySearchTrie.get(word)) {
					indexesOfMovies.add(index);
				}
				indexesOfMovies.add(currentIndex);
				ternarySearchTrie.put(word, indexesOfMovies);
			}
		}
	}

	
	// Creating SymbolGraph and Graph from JSON
	public void createGraphFromJson() throws FileNotFoundException, IOException, ParseException {

		JSONParser parserForJson = new JSONParser();	// Creating JSONParser object
		
		// Getting array of movies from JSON
		arrayOfMoviesWithDetails = (JSONArray) parserForJson.parse(new FileReader("moviesNew.json"));
		
		// Create object of movies
		createArrayForMovies(arrayOfMoviesWithDetails);

		int currentIndexOfMovie = 0;

		for (Object movieObject : arrayOfMoviesWithDetails) {

			JSONObject movie = (JSONObject) movieObject;

			List<String> temp = new ArrayList<String>();
			List<String> tempListToHandleMovieGenre = new ArrayList<String>();

			String name = (String) movie.get("movieName");

			putMovieInTrie(name, currentIndexOfMovie);

			movieNames.add(name);
			collectionMovieAndCast.add(name);
			temp.add(name);
			tempListToHandleMovieGenre.add(name);

			movieIndex.put(name, currentIndexOfMovie);
			currentIndexOfMovie += 1;

			JSONArray casts = (JSONArray) movie.get("cast");

			for (Object cast : casts) {
				uniqueCast.add((String) cast);
				collectionMovieAndCast.add((String) cast);
				temp.add(((String) cast).toLowerCase());
			}

			JSONArray genres = (JSONArray) movie.get("genres");
			for (Object genre : genres) {
				tempListToHandleMovieGenre.add((String) genre);
				setOfGenres.add((String) genre);
			}

			collectionCastRespectToMovie.add(temp);
			collectionMovieRespectToGenre.add(tempListToHandleMovieGenre);

		}
		
		
		// Creating SymbolGraphs
		
		// SymbolGraph for Movie name with respect to cast
		sgForMovieCast = new SymbolGraph(collectionCastRespectToMovie);
		
		// SymbolGraph for Movie name with respect to genres
		sgForMovieGenre = new SymbolGraph(collectionMovieRespectToGenre);

		movieCastGraph = sgForMovieCast.G();
		movieGenreGraph = sgForMovieGenre.G();

	}
	
	public List<String> getAllMovieNames() {
		return movieNames;
	}

	// Returns the name of movie from index
	public String getMovieFromIndex(int indexOfMovie) {
		JSONObject movieObject = (JSONObject) arrayOfMoviesWithDetails.get(indexOfMovie);
		return (String) movieObject.get("movieName");
	}
	

	public void printMovieNameFromIndex(int indexOfMovie) {
		System.out.println();
	}

	public Set<String> getUniqueCast() {
		return uniqueCast;
	}

	public Set<String> getAllGenres() {
		return setOfGenres;
	}

	public Graph getGraphForMoviesWithCast() {
		return movieCastGraph;
	}

	public SymbolGraph getSymbolGraphForMoviesWithCast() {
		return sgForMovieCast;
	}

	public Graph getGraphForMoviesWithGenre() {
		return movieGenreGraph;
	}

	public SymbolGraph getSymbolGraphForMoviesWithGenre() {
		return sgForMovieGenre;
	}

	public List<List<String>> getMoviesWithGenre() {
		return collectionMovieRespectToGenre;
	}

	public static int getCodeOfMovie(String nameOfMovie) {
		return movieIndex.get(nameOfMovie);
	}
	
	
	// Searches for the movie based on the user's input
	public Set<Integer> getMovieByKeyWord(String key, GraphProcessingUtils customGraph) {

		Set<Integer> setOfMovies = new HashSet<Integer>();
		List<String> listOfFaultyMovies = new ArrayList<String>();

		String[] arr = key.toLowerCase().split(" ");
		
		// If user enters only one word
		if (arr.length == 1) {
			
			// Use try to retrieve the movie
			if (ternarySearchTrie.get(key) != null) {
				return ternarySearchTrie.get(key);
			} else {
				
				// Trie could not find the movie, check if it is a substring or not
				for (String movieInDB : this.getAllMovieNames()) {
					try {
						BoyerMoore boyerObj = new BoyerMoore(key.strip().toLowerCase());
						int offset1 = boyerObj.search(movieInDB.strip().toLowerCase());
						if (offset1 < movieInDB.length()) {
							listOfFaultyMovies.add(movieInDB);
						}
					} catch (Exception e) {}
				}
			}
		} 
		
		// User entered more than one word
		else {
			if (!this.getAllMovieNames().contains(key)) {
				for (String movieInDB : this.getAllMovieNames()) {
					try {
						BoyerMoore boyerObj = new BoyerMoore(key.strip().toLowerCase());
						int offset1 = boyerObj.search(movieInDB.strip().toLowerCase());
						if (offset1 < movieInDB.length()) {
							listOfFaultyMovies.add(movieInDB);		
						}
					}
					catch(Exception e) {}
				}
			}
		}

		// Boyer moore could not find any pattern, check for spelling mistakes
		if (listOfFaultyMovies.size() == 0) {
			SpellChecker.bymoviename(customGraph, key);
		}
		else if (listOfFaultyMovies.size() > 0) {

			System.out.println("Some similar results... ");
			int i = 0;
			for (String a : listOfFaultyMovies) {
				setOfMovies.add(movieIndex.get(a));
				if (i == 9) {
					break;
				}
				i++;
			}
		}
		return setOfMovies;
	}

	// Print details in a structured format
	public JSONObject printMovieDetails(int movieCode) {
		JSONObject movieObject = (JSONObject) arrayOfMoviesWithDetails.get(movieCode);

		System.out.println();

		System.out.println("Name 	   : " + movieObject.get("movieName"));
		System.out.println("Description: " + movieObject.get("movieDesc"));
		System.out.println("Duration   : " + movieObject.get("movieLength"));
		System.out.println("Release    : " + movieObject.get("movieYear"));
		System.out.println("Rating	   : " + movieObject.get("movieRating"));
		System.out.println("Genres	   : " + movieObject.get("genres"));
		System.out.println("Cast	   : " + movieObject.get("cast"));

		return movieObject;
	}

	// Create objects of movies
	public static Movie[] createArrayForMovies(JSONArray jsonArray) {
		Movie[] movieArray = new Movie[jsonArray.size()];
		int j = 0;
		for (Object i : jsonArray) {
			try {
				JSONObject currentMovie = (JSONObject) i;
				Movie movie = new Movie((String) currentMovie.get("movieName"), (String) currentMovie.get("movieRating"),
						(String) currentMovie.get("movieDesc"), (String) currentMovie.get("movieLength"),
						(String) currentMovie.get("movieYear"), (JSONArray) currentMovie.get("genres"),
						(JSONArray) currentMovie.get("cast"));
				// Add other movie attributes to movie object here
				movieArray[j] = movie;
				j += 1;
				
			} catch (Exception e) {}
		}
		return movieArray;
	}
	
	
	//create Movie array from set
	public static Movie[] createArrayForMoviesFromSet(Set<Integer> setArray)  {
		
		Movie[] movieArray = new Movie[setArray.size()];
		int j = 0;
		for (Integer i : setArray) {
			try {				
				JSONObject currentMovie = (JSONObject) arrayOfMoviesWithDetails.get(i);
				Movie movie = new Movie((String) currentMovie.get("movieName"), (String) currentMovie.get("movieRating"),
						(String) currentMovie.get("movieDesc"), (String) currentMovie.get("movieLength"),
						(String) currentMovie.get("movieYear"), (JSONArray) currentMovie.get("genres"),
						(JSONArray) currentMovie.get("cast"));
				// Add other movie attributes to movie object here
				movieArray[j] = movie;
				j += 1;
			} catch (Exception e) {	}
		}
		return movieArray;
	}
	
	//create Movie array from iterable
	public static Movie[] createArrayForMoviesFromItr(Iterable<Integer> itArray)  {
		
		 List<Integer> temp = new ArrayList<Integer>();
		 itArray.forEach(temp::add);

		Movie[] movieArray = new Movie[temp.size()];
		int j = 0;
		for (Integer i : temp) {
			try {				
				JSONObject currentMovie = (JSONObject) arrayOfMoviesWithDetails.get(i);
				Movie movie = new Movie((String) currentMovie.get("movieName"), (String) currentMovie.get("movieRating"),
						(String) currentMovie.get("movieDesc"), (String) currentMovie.get("movieLength"),
						(String) currentMovie.get("movieYear"), (JSONArray) currentMovie.get("genres"),
						(JSONArray) currentMovie.get("cast"));
				movieArray[j] = movie;
				j += 1;
			} catch (Exception e) {
			}
			// Add other movie attributes to movie object here
		}
		return movieArray;
	}
		
		

	// List of stop words to filter out unnecessary inputs
	public static boolean isStopWord(String word) {
		String[] stopWords = { "a", "an", "the", "in", "on", "at", "for", "and", "or", "but", "with", "is", "are",
				"was", "were", "be", "been", "has", "have", "had", "this", "that", "these", "those", "there", "here",
				 "from", "to", "of" };
		return Arrays.asList(stopWords).contains(word.toLowerCase());
	}

}
