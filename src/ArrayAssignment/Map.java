/*
Anjana Somasundaram
January.08.2018
Mr. Radulovich - ICS3U
Array Assignment
Uses 2D arrays to load given text file and identifies an available path from the starting point (R) to the goal (G)
This class loads the text file onto a 2D array
*/

package ArrayAssignment;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Map 
{
	
	//Initializes scanner to scan file
	private Scanner MapReader;
	private Map map;
	private String Grid1 [][];
	private final String filename="src/ArrayAssignment/Maze2.txt";

	//Reads the text file
	public Scanner readMap(String filename)
	{
		map=new Map();
		MapReader = null;
		try 
		{
			MapReader = new Scanner(new File(filename));
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}
		return MapReader;
	}
	
	//Identifies the number of columns (the width)
	public int getWidth(String filename) 
	{
		MapReader=readMap(filename);
		int column_Count=MapReader.nextLine().split(" ").length;
		return column_Count;
	}
	
	//Identifies the number of rows (the height)
	public int getHeight(String filename) 
	{
		MapReader=readMap(filename);
		int row_Count = 0;
		while (MapReader.hasNextLine()) 
		{
			row_Count += 1;
			MapReader.nextLine();
		}
		return row_Count;
	}

	//Creates a 2D array
	public String[][] Grid()
	{
			int r=map.getHeight(filename);
			int c=map.getWidth(filename);
			Grid1 = new String [c][r];
				for (int y = 0; y < r; y++)								// The y coordinate corresponds to the row number
				{
					String[] line = (MapReader.nextLine().split(" "));
					for (int x = 0; x < c; x++)							// The x coordinate corresponds to the column number
						{
							Grid1[x][y] = line[x]; //This is my 2D array
						}
				}
			return Grid1;
	}
	
	//Retrieves cell value of the given x and y coordinates
	public String getCell(int x, int y)
	{			
		return Grid1 [x][y];
	}
}

