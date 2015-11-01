package com.udel.algorithms;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;



class Neighbour{
	public int vertext;
	public Neighbour nextNeighbour;
	public Neighbour(int vertext,Neighbour nextNeighbour){
		this.vertext = vertext;
		this.nextNeighbour = nextNeighbour;
	}
}
class Vertex{
	public String name;
	public Neighbour adjList;
	public Vertex(String name, Neighbour adjList){
		this.name = name;
		this.adjList = adjList;
	}
}

public class Graph {
	public Boolean directed;
	public ArrayList<Vertex> vertices =  new ArrayList<Vertex>();
	private int count=0;
	
	public Graph(String fileName) throws FileNotFoundException{
		Scanner fileScanner  = new Scanner(new File(fileName));
		while(fileScanner.hasNextLine()){
			String line = fileScanner.nextLine();
			String tempArray[] =line.split("[:,]+");
			//check if the parent already exists in the list if not create one.
			int parentIndex = vertexForName(tempArray[0]);
			Vertex parentVertex;
			if(parentIndex!=-1){
				//this vertex already exists.
				parentVertex = vertices.get(parentIndex);
			}else{
				parentVertex = new Vertex(tempArray[0].trim(), null);
				vertices.add(parentVertex);
				parentIndex = vertices.indexOf(parentVertex);
			}
		
			for(int i=1;i<tempArray.length;i++){
				int indexOfVertex = vertexForName(tempArray[i]);
				
				if(indexOfVertex!=-1){
					//vertex exists in the array, link the reference
					
				    //iterate through the adjList of the vertex and add the neighbor at the end.
					Neighbour temp = parentVertex.adjList;
					if(temp==null){
						parentVertex.adjList = new Neighbour(indexOfVertex, null);
					}else{
						while(temp.nextNeighbour!=null){
							temp = temp.nextNeighbour;
						}
						temp.nextNeighbour = new Neighbour(indexOfVertex, null);
					}
				}else{
					//create a new vertex add it to the array list and link the reference.
					Vertex childVertex = new Vertex(tempArray[i].trim(), null);
					vertices.add(childVertex);
					
					int indexOfChild = vertices.indexOf(childVertex);
					Neighbour temp = parentVertex.adjList;
					if(temp==null){
						parentVertex.adjList = new Neighbour(indexOfChild, null);
					}else{
						while(temp.nextNeighbour!=null){
							temp = temp.nextNeighbour;
						}
						temp.nextNeighbour= new Neighbour(indexOfChild, null);
					}
				}
			}
		}
		
		fileScanner.close();
		
	}
	
	public int vertexForName(String name){
		
		for(int i=0;i<vertices.size();i++){
			if(vertices.get(i).name.equalsIgnoreCase(name.trim())){
				return i;
			}
		}
		return -1;
	}
	
	public void printGraph(){
		for(int i=0;i<vertices.size();i++){
			System.out.print(vertices.get(i).name);
			for(Neighbour nbr = vertices.get(i).adjList; nbr!=null ; nbr=nbr.nextNeighbour){
				System.out.print("--> "+ vertices.get(nbr.vertext).name);
			}
			System.out.println("\n");
			
		}
	}
	
	public static void main(String[] args) throws IOException{
		
		//Scanner sc =  new Scanner(System.in);
		 //System.out.print("Enter graph input file name: ");
		 String fileName = "hyponymy.txt";
		 Graph  graph = new Graph(fileName);
		 graph.printGraph();
	}
}