import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;


public class Solution {
	

	public class vertex implements Comparator<vertex>	{
		public ArrayList<Character> colors;
		public int start;
		public ArrayList<Integer> ends;		
		
	



		@Override
		public int compare(vertex o1, vertex o2) {
			return o1.start - o2.start;
		}
	}
	
	public class point	{
		public int start;
		public int end;
		public char color;
	}
	
	public int gridLength;	//n
	public int numPoints;	//m
	public char[] colors;	//B R R Y Y G G B G Y R G B Y B G R R R R R G R B G R G
	public int rocketStart;
	public int luckyStart;
	
	
	public vertex rocketVert, luckyVert;
	
	public ArrayList<vertex> vertTree;
	public ArrayList<point> allPoints;
	
	public int goal;
	public int[] table;
	public int[] traceback;
	public int[] loopTable;
	
	
	public Solution(String fileName) throws FileNotFoundException	{
		
		vertTree = new ArrayList<vertex>();
		allPoints = new ArrayList<point>();
		File file = new File(fileName);
		Scanner sc = new Scanner(file);			
		String line;
		int i = 0;
		while(sc.hasNextLine())	{
			line = sc.nextLine();	
			String[] lineValues = line.split(" ");
			switch(i) {
			case 0:
				//line 1: n, m
				gridLength = Integer.parseInt(lineValues[0]);
				numPoints = Integer.parseInt(lineValues[1]);
				break;
			case 1:
				colors = new char[gridLength-1];
				table = new int[gridLength];
				traceback = new int[gridLength];
				loopTable = new int[numPoints];
				for(int j=0; j<colors.length; j++)	{
					colors[j] = lineValues[j].charAt(0);
				}
				break;
			case 2:
				rocketStart = Integer.parseInt(lineValues[0]);
				luckyStart = Integer.parseInt(lineValues[1]);			
				break;
			default:
				
				
				
				vertex start;
				int end = Integer.parseInt(lineValues[1]);
				
				start = new vertex();
				start.start = Integer.parseInt(lineValues[0]);				
				
				start.ends = new ArrayList<Integer>();
				start.colors = new ArrayList<Character>();
				//start.ends.add(end);
				start.colors.add(new Character( lineValues[2].charAt(0)));
				


				
				boolean foundStartValue = false;
				boolean foundEndValue = false;
				
				start.ends = new ArrayList<Integer>();
				start.ends.add(end);
				
				
				
				for(vertex v : vertTree)	{
					if(v.start == start.start)	{
						foundStartValue = true;
						if(v.ends == null) {v.ends = new ArrayList<Integer>(); v.colors = new ArrayList<Character>();}
						v.ends.add(end);
						v.colors.add( new Character( lineValues[2].charAt(0)));
					}	
				}
								
				if(!foundStartValue)	{vertTree.add(start); }
				
				
			}
			
				
			
				
			i++;
		}
		

		
		
		for(int j=0; j<table.length; j++) {
			table[j] = -1;
			traceback[j] = -1;
			loopTable[j] = -1;
		}
		sc.close();			
		ArrayList<String> strs = new ArrayList<String>();
		strs.add("f");
		//Collections.sort(vertTree, new vertex());

		goal = gridLength;
	}
	
	

	
	private int dynamic(vertex start)	{
		
		//we've done a loop! (been here before)
		if(table[start.start - 1]!=-1) {
			
			return -1;
		}
		
		if(loopTable[start.start]!=-1) {
			return -1;
		}	
		loopTable[start.start] = 0;
		//base case 1: (we've found the goal)
		if(start.start == goal) {
			table[start.start - 1] = 0;
			return 0;
		}
		
		int min = Integer.MAX_VALUE;		
		int index = -1;
		ArrayList<vertex> options = new ArrayList<vertex>();
		if(start.ends == null || start.ends.size()==0) {
			return -1;
		}
		for(int end : start.ends)	{
			for(vertex v : vertTree) {
				if(end == v.start)	{
					options.add(v);
				}
			}
		}
		
		for(int i=0; i<options.size(); i++) {	
			int optLength = dynamic(options.get(i));
			if(optLength==-1) {continue;}	//this path turned out to be a dead end
			int length = Math.abs(start.start - start.ends.get(i)) + optLength;
			if(length<min )	{
				min=length;			
				index=i;
			}
		}
		if(index==-1)	{
			//this was a dead end:
			return -1;
		}
		traceback[start.start - 1] = start.ends.get(index);
		table[start.start - 1] = min;
		
		return min;
	}
	
	public void solveDynamic()	{
		vertex luckyV = vertTree.get(luckyStart-1);
		vertex rocketV = vertTree.get(rocketStart-1);
		System.out.println("Lucky's length: " + dynamic(luckyV));
		System.out.println("Rocket's length: " + dynamic(rocketV));
	}
	
	public void print()	{
		System.out.println("lengths:");
		for(int i : table) {
			System.out.print(i + " ");
		}
		System.out.println();
		System.out.println("traceback:");
		for(int i : traceback) {
			System.out.print(i + " ");
		}
		System.out.println();
		System.out.println();
		System.out.println("Steps:");
		
		int luckyIndex = this.luckyStart;
		int rocketIndex = this.rocketStart;

		while(true)	{
			if(luckyIndex!=gridLength && luckyIndex!=-1) {luckyIndex = traceback[luckyIndex - 1]; System.out.println("L " + luckyIndex);}
			if(rocketIndex!=gridLength&& rocketIndex!=-1) {rocketIndex = traceback[rocketIndex - 1]; System.out.println("R " + rocketIndex);}
			
			
			
			if(luckyIndex == gridLength && rocketIndex == gridLength) {
				break;
			}
			if(luckyIndex == -1) {
				System.out.println("Lucky cannot reach the goal!");
				
			}
			if(rocketIndex == -1) {
				System.out.println("Rocket cannot reach the goal!");
				
			}
			if(luckyIndex == -1 && rocketIndex == -1) {
				break;
			}

		}
	}
}
