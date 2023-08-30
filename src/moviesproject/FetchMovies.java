package moviesproject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

class FetchMovies {
	@SuppressWarnings("unchecked")
	public void sync() {

		
		
		//// change the stop to 1 to run this file
		int stop=0;
		
		
		///Please Read:
		/// this will take nearly 15-30 minutes to fetch all the movies because it is visiting nearly 2400 web pages.
		/// to run this for limited no. of pages which will result in less no. of movies change the NumberOfPages to 5.
		/// less number of movies can result in less number of results for auto suggestion and spell checker
		// for complete processing put NumberOfPages to 10000
		int NumberOfPages=10000;
		
		
		
		if(stop==0) {
			System.out.println("You need to change the stop value to 1 to run this file. (This is for safety purpose.)");
			return;
		}
		
		int pageNumber=1;
		JSONArray jsonArray = new JSONArray();
		
		System.out.println("Fetching the movies......");
		System.out.println("Please have patience this will take some time, Don't stop in between  ......");
		
		while(stop==1) {
			
			
			System.out.println(pageNumber);
			
			//country code 129 for USA, 147 is for Canada
			// URL which is being scrapped for movies data
			String url = "https://goku.to/filter?type=1&country=147&sort=latest-update&page="+pageNumber;
			
			pageNumber++;
			
			
			try {
				
				//get document using Jsoup
				Document doc = Jsoup.connect(url).get();
				
				// select the area where movies are
				Elements movieItemsSection = doc.select(".section-items");
				
				//this gives array of movie items
				Elements movieItems = movieItemsSection.select(".item");
				
				
				// it number of movieItems is 0 then we stop the loop
				if(movieItems.size()==0 || pageNumber>NumberOfPages ) {
					stop=0;
					continue;
				}
				
				
				
				
				// for each movie item in movie items
				for(Element movieItem:movieItems) {
					
					//create a JSON object
					JSONObject obj=new JSONObject();
					
					// selecting the DIV containing movie info
					Elements movieInfo = movieItem.select(".movie-info");
					
					//these select classes depends on website getting different movie attributes.
					String movieName = movieInfo.select(".movie-name").text();
					String movieLink = movieInfo.select(".movie-link").attr("abs:href");
					String movieRating = movieInfo.select(".is-rated").text();
					String movieYear = movieInfo.select(".info-split > div").get(0).text();
					String movieLength = movieInfo.select(".info-split >div").get(2).text();
			
					
					//putting the items in json object
					obj.put("movieName",movieName);    
					obj.put("movieLink",movieLink); 
					obj.put("movieRating",movieRating); 
					obj.put("movieYear",movieYear); 
					obj.put("movieLength",movieLength); 
			
					
					
					//getting into particular movie link, kind of dfs
					Document doc1= Jsoup.connect(movieLink).get();
					
					//getting movie details
					Elements movieDetails=doc1.select(".movie-detail");
					
					//get summary of the movie
					String movieDesc=movieDetails.select(".dropdown-text > .dropdown-text").text();
			 
					//put summary in json object
					obj.put("movieDesc",movieDesc); 
					
					
					//get other details, which gives a list we use get function to access that
					Elements impDetails=movieDetails.select(".is-sub > div");
					
					
					//genre tags of movie
					Elements tags=impDetails.get(0).select(".value > a");
					
					List<String> genres=new ArrayList<String>();
					// put these tags in arraylist
					for(Element tag:tags) {
						genres.add(tag.text());
					}
					
					// convert array to json array and insert in json object
					obj.put("genres",genres);
					
					
					//similar to cast memebers
					Elements casts=impDetails.get(1).select(".value > a");
					List<String> cast=new ArrayList<String>();
					for(Element m:casts) {
						cast.add(m.text());
					}
			
 
					obj.put("cast",cast);
					
					
					
					// similar to production industries
					Elements productions=impDetails.get(2).select(".value > a");
					List<String> production=new ArrayList<String>();
					for(Element m:productions) {
						production.add(m.text());
					}
				
					
					obj.put("production",production);
					
			
			
					// similar for countries of movie
					Elements countries=impDetails.get(3).select(".value > a");
					List<String> country=new ArrayList<String>();
					for(Element m:countries) {
						country.add(m.text());
					}
					
					
					obj.put("country",country);
					
				
					//add each json object to JSON array
					jsonArray.add(obj);
						
					
				}
				
			} 
			catch (IOException e) {
				// catch exception if any issue
				e.printStackTrace();
			}
			
			
		}
		
//		 writing json array to file
		try (FileWriter file = new FileWriter("moviesNew.json")) {
			
            // write json array to file by converting it to string
            file.write(jsonArray.toJSONString()); 
            
            //clearing any buffered data
            file.flush();
 
        } catch (IOException e) {
        	//handle file exception
            e.printStackTrace();
        }
	
		System.out.println("Hurayyyy !!!! Done.");
	}
}