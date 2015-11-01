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
			System.out.println(line);	
			String array[] =line.split("[:,]+");
			for(int i=0;i<array.length;i++){
				System.out.println(array[i]);
			}
		}
		
		fileScanner.close();
		
	}
	
	public int vertexForName(String name){
		
		for(int i=0;i<vertices.size();i++){
			if(vertices.get(i).name.equalsIgnoreCase(name)){
				return i;
			}
		}
		return -1;
	}
	
	public void printGraph(){
	
	}
	
	public static void main(String[] args) throws IOException{
		
		//Scanner sc =  new Scanner(System.in);
		 //System.out.print("Enter graph input file name: ");
		 String fileName = "hyponymy.txt";
		 Graph  graph = new Graph(fileName);
		 graph.printGraph();
	}
}