package com.udel.algorithms;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

class Neighbour {
	public int vertext;
	public Neighbour nextNeighbour;
	public Neighbour(int vertext, Neighbour nextNeighbour) {
		this.vertext = vertext;
		this.nextNeighbour = nextNeighbour;
	}
}

class Vertex {
	public String name;
	public Neighbour adjList;
	public Vertex parent;
	public String color;
	public boolean ignore;
	public boolean isIgnore() {
		return ignore;
	}
	public void setIgnore(boolean ignore) {
		this.ignore = ignore;
	}
	public Vertex(String name, Neighbour adjList) {
		this.name = name;
		this.adjList = adjList;
		this.ignore=false;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Neighbour getAdjList() {
		return adjList;
	}
	public void setAdjList(Neighbour adjList) {
		this.adjList = adjList;
	}
	public Vertex getParent() {
		return parent;
	}
	public void setParent(Vertex parent) {
		this.parent = parent;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
}

class Graph {
	public Boolean directed;
	public ArrayList<Vertex> vertices =  new ArrayList<Vertex>();
	
	/**
	 * Default constructor. Reads from stdin.
	 * @throws IOException
	 */
	public Graph() throws IOException {
		this.buildGraphByStdin();
	}
	
	/**
	 * Alternative constructor. Reads from a file.
	 * @param fileName
	 * @throws FileNotFoundException
	 */
	public Graph(String fileName) throws FileNotFoundException {
		this.buildGraphByFile(fileName);
	}
	
	/**
	 * Builds the graph using input from stdin.
	 * @throws IOException
	 */
	private void buildGraphByStdin() throws IOException {
		InputStreamReader in= new InputStreamReader(System.in);
        BufferedReader input = new BufferedReader(in);
        String line;
		while ((line = input.readLine()) != null) {
			this.parseLine(line);
		}
	}
	
	/**
	 * Builds the graph using the input from a file.
	 * @param fileName
	 * @throws FileNotFoundException
	 */
	private void buildGraphByFile(String fileName) throws FileNotFoundException {
		Scanner fileScanner  = new Scanner(new File(fileName));
		while (fileScanner.hasNextLine()) {
			String line = fileScanner.nextLine();
			this.parseLine(line);
		}
		fileScanner.close();
	}
	
	/**
	 * Parses a line and add the elements to the graph.
	 * @param line
	 */
	private void parseLine(String line) {
		String tempArray[] = line.split("[:,]+");
		
		// check if the parent already exists in the list if not create one
		int parentIndex = findVertexIndexByName(tempArray[0]);
		Vertex parentVertex;
		if (parentIndex!=-1) {
			// this vertex already exists
			parentVertex = vertices.get(parentIndex);
		} else {
			parentVertex = new Vertex(tempArray[0].trim(), null);
			vertices.add(parentVertex);
			parentIndex = vertices.indexOf(parentVertex);
		}
	
		for (int i = 1; i < tempArray.length; i++) {
			int indexOfVertex = findVertexIndexByName(tempArray[i]);
			
			if (indexOfVertex != -1) {
				// vertex exists in the array => link the reference
			    // iterate through the adjList of the vertex and add the neighbor at the end
				Neighbour temp = parentVertex.adjList;
				if (temp == null) {
					parentVertex.adjList = new Neighbour(indexOfVertex, null);
				} else {
					while (temp.nextNeighbour != null) {
						temp = temp.nextNeighbour;
					}
					temp.nextNeighbour = new Neighbour(indexOfVertex, null);
				}
			} else {
				// create a new vertex, add it to the array list and link the reference
				Vertex childVertex = new Vertex(tempArray[i].trim(), null);
				vertices.add(childVertex);
				
				int indexOfChild = vertices.indexOf(childVertex);
				Neighbour temp = parentVertex.adjList;
				if (temp == null) {
					parentVertex.adjList = new Neighbour(indexOfChild, null);
				} else {
					while (temp.nextNeighbour != null) {
						temp = temp.nextNeighbour;
					}
					temp.nextNeighbour = new Neighbour(indexOfChild, null);
				}
			}
		}
	}
	
	/**
	 * Search for a vertex by name.
	 * @param name
	 * @return int The vertex's index or -1 if not found
	 */
	public int findVertexIndexByName(String name) {
		for (int i = 0; i < vertices.size(); i++) {
			if (vertices.get(i).name.equalsIgnoreCase(name.trim())) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Prints subspecies of a species of certain order.
	 * @param number
	 * @param species
	 * @param order
	 */
	public void cite(int number, String species, int order) {
		int index = this.findVertexIndexByName(species);
		if (index == -1) {
			System.out.print("Species not found.");
			return;
		}
		Vertex v = vertices.get(index);
		this.cite(v, number, order-1);
	}
	
	/**
	 * Prints all subspecies of a species of certain order.
	 * @param species
	 * @param order
	 */
	public void citeAll(String species, int order) {
		int index = this.findVertexIndexByName(species);
		if (index == -1) {
			System.out.print("Species not found.");
			return;
		}
		Vertex v = vertices.get(index);
		this.cite(v, -1, order-1);
	}
	
	/**
	 * Recursive method for citing.
	 * @param v
	 * @param number
	 * @param order
	 * @return int Remaining number of subspecies to print.
	 */
	private int cite(Vertex v, int number, int order) {
		if (number == 0) {
			return number;
		}
		if (order <= 0) {
			// Reached provided order => print
			Neighbour nbr = v.adjList;
			while (number != 0 && nbr != null) {
				System.out.print(vertices.get(nbr.vertext).name + ", ");
				nbr = nbr.nextNeighbour;
				number--;
			}
		} else {
			// Did not reach provided order => keep going
			for (Neighbour nbr = v.adjList; nbr != null; nbr = nbr.nextNeighbour) {
				number = cite(vertices.get(nbr.vertext), number, order-1);
			}
		}
		return number;
	}
	
	/**
	 * Prints the most diverse species (the one that has more subspecies).
	 */
	public void mostDiverse() {
		int max = -1;
		Vertex v = null;
		for (int i = 0; i < vertices.size(); i++) {
			int count = 0;
			for (Neighbour nbr = vertices.get(i).adjList; nbr != null; nbr = nbr.nextNeighbour) {
				count++;
			}
			if (count > max) {
				max = count;
				v = vertices.get(i);
			}
		}
		System.out.println("Most diverse species: " + v.name + " with " + max + " subspecies.");
	}
	
	/**
	 * Finds the lowest common ancestor of 2 species starting from a certain species.
	 * @param species1 Starting point
	 * @param species2
	 * @param species3
	 * 
	 * IDEA:
	 * find the shortest path to one of s2/s3
	 * traverse back from the s2/s3 to source s1 and from every ancestor try finding the path to s2/s3 if exists
	 * if exists then that is the lowest common ancestor.
	 * 
	 * Algorithm:
	 * s1 is the source. 
	 * if s1=s2
	 * 		return -1 //no such ancestor 
	 * if s1=s3
	 * 		return -1 //no such ancestor
	 * 
	 * 		
	 * for each vertex x in vertices
	 * 		if x=s1 //finding s1 source
	 * BFS(G,x,w)
	 * 	  
	 * for each vertex k of vertices  //initialize all the vertices
	 * 		k.color = WHITE
	 * 		k.parent = NIL
	 * 
	 * x.color = GRAY
	 * x.parent = NIL
	 * Q=None;
	 * Enqueue(Q,x)
	 * while Q !=None
	 *        for each u in Adj[x] //visit all the children of u
	 *          if u.color != GRAY
	 *             	u.parent = x
	 *          	u.color = GRAY;
	 *              if u =w
	 *              	return true;
	 *              else
	 * 					Enqueue(Q,u)
	 *          x.color = black //fully explored.
	 * return false;
	 * 
	 *     
	 *for each vertex x in vertices
	 *		if x=s2
	 *			recursiveFinder(x)    
	 *				k = x.parent
	 *     			while(k != s1)
	 *         			//remove edges k-x & k-k.parent do Bfs with remaining edges.
	 *      			if BFS(G,k,s3) = true 
	 *      				print x.parent is the lowest common ancestor
	 *      				return
	 *          		else // reached route
	 *          			add the edges k-x & k-k.parent back continues search.
	 *          			recursiveFinder(k.parent) 	
	 *              
	 *              print there is no common ancestor.
	 *              return	
	 * 			   
	 */
	
	
	
	public boolean BFS(Vertex s, Vertex w){
		
		Queue<Vertex> queue = new LinkedList<Vertex>();
		initializeVertices();
		s.setColor("GRAY");
		queue.add(s);
		
		while(!queue.isEmpty()){
			Vertex u = queue.poll();
			for (Neighbour nbr = u.adjList; nbr != null; nbr = nbr.nextNeighbour) {
				Vertex child = vertices.get(nbr.vertext);
				if(!child.getColor().equalsIgnoreCase("GRAY") && !child.isIgnore()){
					child.setColor("GRAY");
					child.setParent(u);
		
					if(w!=null && child.getName().equalsIgnoreCase(w.getName())){ //when w is not provided it will explore the entire graph.
						return true;
					}else{
						queue.add(u);
					}
				}
			}
			u.setColor("BLACK");
		}
		
		return false;
	}
	
	private void initializeVertices() {
		
		for (int i = 0; i < vertices.size(); i++) {
			vertices.get(i).setColor("WHITE");
			vertices.get(i).setParent(null);
		}
	}
	

	public int lowestCommonAncestor(String species1, String species2, String species3) {
		int v1i = this.findVertexIndexByName(species1);
		int v2i = this.findVertexIndexByName(species2);
		int v3i = this.findVertexIndexByName(species3);
		Vertex s1 = vertices.get(v1i);
		Vertex s2 = vertices.get(v2i);
		Vertex s3 = vertices.get(v2i);
		if (v1i < 0 || v2i < 0 || v3i < 0) {
			System.out.println("Vertices not found.");
			return -1;
		}else if(species1.equalsIgnoreCase(species2) || species1.equalsIgnoreCase(species3)){
			System.out.println("No common ancestor starting from s1");
			return -1;
		}else{
			BFS(s1,null); //explores the entire graph
			Vertex k = s2.getParent();
			getCommonAncestor(s1,s2,s3);
			
		}
		return v3i;
		
		
		// TODO
	}
	
	private void getCommonAncestor(Vertex s1, Vertex s2, Vertex s3) {
	
		Vertex k = s2.getParent();
		while(k!=null){
			
			//remove the edge connecting k-s2 and k-k.parent
			
			for (Neighbour nbr = k.adjList; nbr != null; nbr = nbr.nextNeighbour) {
				int index = nbr.vertext;
				if(index==vertices.indexOf(s2) || index==vertices.indexOf(k.getParent())){
					vertices.get(index).setIgnore(true);
				}
			}
			if(BFS(k,s3)){
				System.out.printf("Common Ancestor: %s",k.getName());
				break;
			}else{
				for (Neighbour nbr = k.adjList; nbr != null; nbr = nbr.nextNeighbour) {
					int index = nbr.vertext;
					vertices.get(index).setIgnore(false);
				}
				getCommonAncestor(s1, k.parent, s3);
			}
		
		}
		
		System.out.println("No Common Ancestor Found");
	}

	/**
	 * Prints the graph.
	 */
	public void printGraph() {
		for (int i = 0; i < vertices.size(); i++) {
			System.out.print(vertices.get(i).name);
			for (Neighbour nbr = vertices.get(i).adjList; nbr != null; nbr = nbr.nextNeighbour) {
				System.out.print(" --> "+ vertices.get(nbr.vertext).name);
			}
			System.out.println("\n");
		}
	}
	
	public static void main(String[] args) throws IOException {
		Graph graph;
		try {
			graph = new Graph("hyponymy.txt");
			//graph.printGraph();
			graph.lowestCommonAncestor("Entity","water","pot");
		} catch (FileNotFoundException e) {
			graph = new Graph();
		}
		 
		//graph.printGraph();
		 
		//graph.mostDiverse();
		 
		/*graph.cite(3, "vertebrates", 1);
		System.out.print("\n");
		graph.cite(4, "vertebrates", 2);
		System.out.print("\n");
		graph.cite(11, "carnivore", 2);
		System.out.print("\n");
		graph.cite(15, "pet", 3);*/
		
		//graph.citeAll("vertebrates", 2);
	}
}