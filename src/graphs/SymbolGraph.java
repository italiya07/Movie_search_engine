package graphs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//////////////////
//this code might resembles to that provided in the class
///////////////////

public class SymbolGraph {
    private ST<String, Integer> st;  // string -> index
    private String[] keys;           // index  -> string
    private Graph G;

    //creating the symbol graph
    public SymbolGraph(List<List<String>> vertices) {
        st = new ST<String, Integer>();

        for(int j=0;j<vertices.size();j++) {
	        List<String> a=vertices.get(j);
	        
	        for (int i = 0; i < a.size(); i++) {
	            if (!st.contains(a.get(i)))
	                st.put(a.get(i), st.size());
	        }
        }
        
//        System.out.println("Done reading ");

        // inverted index to get string keys in an array
        keys = new String[st.size()];
        for (String name : st.keys()) {
            keys[st.get(name)] = name;
        }

        // second pass builds the graph by connecting first vertex on each line to all others
        G = new Graph(st.size());

  
        for(int j=0;j<vertices.size();j++) {
        	List<String> a=vertices.get(j);
	        
	        int v = st.get(a.get(0));
            for (int i = 1; i < a.size(); i++) {
                int w = st.get(a.get(i));
                G.addEdge(v, w);
            }
        }
        	
           

    }

    //Does the graph contain the vertex named s?
 
    public boolean contains(String s) {
        return st.contains(s);
    }

   
    // Returns the integer associated with the vertex named s.
    public int index(String s) {
        return st.get(s);
    }

    //Returns the name of the vertex associated with the integer v
    public String name(int v) {
        return keys[v];
    }

    //Returns the graph associated with the symbol graph.
    public Graph G() {
        return G;
    }

    
//    public static void main(String[] args) {
// 
//        SymbolGraph sg = new SymbolGraph("movies.txt", "/");
//        Graph G = sg.G();
//        
//        // Task 4
//        StdOut.println("Movies by Leonardo DiCaprio:");
//        if(sg.contains("DiCaprio, Leonardo")) {
//        	Iterable<Integer> t=G.adj(sg.index("DiCaprio, Leonardo"));
//        	for(Integer i : t) {
//        		StdOut.println(sg.name(i));        		
//        	}
//        }
//        
//        ArrayList<Integer> ja = new ArrayList<Integer>();
//        StdOut.println("\nMovies by Julia Roberts:");
//        if(sg.contains("Roberts, Julia (I)")) {
//        	Iterable<Integer> t=G.adj(sg.index("Roberts, Julia (I)"));
//        	for(Integer i : t) {
//        		ja.add(i);
//        		StdOut.println(sg.name(i));        		
//        	}
//        }
//        
//        ArrayList<Integer> ha = new ArrayList<Integer>();
//        StdOut.println("\nMovies by Hugh Grant:");
//        if(sg.contains("Grant, Hugh (I)")) {
//        	Iterable<Integer> t=G.adj(sg.index("Grant, Hugh (I)"));
//        	for(Integer i : t) {
//        		ha.add(i);
//        		StdOut.println(sg.name(i));        		
//        	}
//        }
//        
//     
//        ja.retainAll(ha); // getting movies that are present in both the arrays
//        StdOut.println("\nMovies by both Hugh Grant and Julia Roberts:");
//        for(Integer i : ja) {
//    		
//    		StdOut.println(sg.name(i));        		
//    	}
//        
//
//    }
}
