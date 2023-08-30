package moviesproject;

import org.json.simple.JSONArray;

public class Movie {

	private String movieName;
	private String movieRating;
	private String movieDesc, movieLength, movieyear;
	private JSONArray genres;
	private JSONArray cast;

	
	//Constructor of Movie
	
	public Movie(String title, String rating, String desc, String length, String year, JSONArray genres, JSONArray cast) {
		
		
		if (title.isBlank()) {
			this.movieName = "No name found";
		} else {
			this.movieName = title;
		}

		if (desc.isBlank()) {
			this.movieDesc = "No description found";
		} else {
			this.movieDesc = desc;
		}

		if (length.isBlank()) {
			this.movieLength = "Could not find duration";
		} else {
			this.movieLength = length;
		}

		if (year.isBlank()) {
			this.movieyear = "Could not find year";
		} else {
			this.movieyear = year;
		}

		this.movieRating = rating;
		this.genres = genres;
		this.cast = cast;
		
	}

	
	//getters
	
	//get movie name
	public String getMovieName() {
		return movieName;
	}

	//get movie description
	public String getMovieDesc() {
		return movieDesc;
	}

	//get movie duration
	public String getMovieDuration() {
		return movieLength;
	}

	//get Movie release year
	public String getMovieRelease() {
		return movieyear;
	}

	//get movie genre
	public JSONArray getMovieGenres() {
		return genres;
	}

	//get movie cast
	public JSONArray getMovieCast() {
		return cast;
	}

	//get rating converted to double from string
	public double getRating() {
		try {
			return Double.parseDouble(movieRating.strip());
		} catch (Exception e) {
			return (double) 0;
		}
	}
}
