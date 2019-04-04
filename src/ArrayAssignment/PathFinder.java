/*
Anjana Somasundaram
January.08.2018
Mr. Radulovich - ICS3U
Array Assignment
Uses 2D arrays to load given text file and identifies an available path from the starting point (R) to the goal (G)
This class stores the algorithm which finds the correct path
*/

package ArrayAssignment;

public class PathFinder 
{
	// Name of text file
	private final String filename = "src/ArrayAssignment/Maze2.txt";
	
	// Marks current coordinates as the robot navigates around the map
	private int currentX;
	private int currentY;
	
	// Finds dimension of maze
	private int numColumns;
	private int numRows;
	
	// Initial cell value of starting position (R)
	private String value = "1";
	
	// Choice of direction (allows robot to determine its position relative to the goal position)
	private String choiceDirection;
	
	// Loads Map Class (responsible for loading text file in 2D array)
	private Map maze;

	// A map of the positions that the robot has visited and is on the solution path
	private int[][] solution;

	// Stores cells that lead to dead ends
	private int[][] deadEnd;

	// Initializes the map class
	public void initialize() 
	{
		// Uses map class to create 2D array grid to represent the maze
		maze = new Map();
		maze.readMap(filename);
		maze.Grid();
		
		// Calculates the dimensions of the given maze
		numColumns = maze.getWidth(filename);
		numRows = maze.getHeight(filename);

		solution = new int[numColumns][numRows];
		deadEnd = new int[numColumns][numRows];
		
		for (int y = 0; y < numRows; y += 1) 
		{
			for (int x = 0; x < numColumns; x +=1) 
			{
				solution[x][y] = 0; 		// Initializes solution array (contains 0s which change to 1s when the position is visited)
				deadEnd[x][y] = 0; 			// Initializes the dead end array (contains 0s which change to 1s when the position leads to a dead end)
				
			}
		}
	}
	
	// Solves maze given the starting and ending x and y coordinates
	public void solveMaze(int x, int y, int endX, int endY) 
	{
		// Orientates the robot to move towards the goal depending on its position
		choiceDirection = setDirection(x, y, endX, endY);
		// Option 1: Robot must move northeast to reach goal
		if (choiceDirection == "NE") 
		{
			// Ensures that the robot is not at the goal position (else no further moves would be required)
			if (value.equals("G") == false) 
			{
				// Marks position as visited
				solution[x][y] = 1;
				
				// North
				if (isSafe(x, y - 1) == true) 
				{
					y = y - 1;
				}
				// East
				else if (isSafe(x + 1, y) == true) 
				{
					x = x + 1;
				}
				// South
				else if (isSafe(x, y + 1) == true) 
				{
					y = y + 1;
				}
				// West
				else if (isSafe(x - 1, y) == true) 
				{
					x = x - 1;
				}
				// Backtracks
				else 
				{
					backtrack(x, y);
				}
			}
		}
		// Option 2: Robot must move northwest to reach goal
		else if (choiceDirection == "NW") 
		{
			// Ensures that the robot is not at the goal position (else no further moves would be required)
			if (value.equals("G") == false) 
			{
				// Marks position as visited
				solution[x][y] = 1;
				// North
				if (isSafe(x, y - 1) == true) 
				{
					y = y - 1;
				}
				// West
				else if (isSafe(x - 1, y) == true) 
				{
					x = x - 1;
				}
				// South
				else if (isSafe(x, y + 1) == true) 
				{
					y = y + 1;
				}
				// East
				else if (isSafe(x + 1, y) == true) 
				{
					x = x + 1;
				}
				// Backtracks
				else 
				{
					backtrack(x, y);
				}
			}
		}
		// Option 3: Robot must move southeast to reach goal
		else if (choiceDirection == "SE") 
		{
			// Ensures that the robot is not at the goal position (else no further moves would be required)
			if (value.equals("G") == false) 
			{
				// Marks position as visited
				solution[x][y] = 1;
				// South
				if (isSafe(x, y + 1) == true) 
				{
					y = y + 1;
				}
				// East
				else if (isSafe(x + 1, y) == true) 
				{
					x = x + 1;
				}
				// North
				else if (isSafe(x, y - 1) == true) 
				{
					y = y - 1;
				}
				// West
				else if (isSafe(x - 1, y) == true) 
				{
					x = x - 1;
				}
				// Backtracks
				else 
				{
					backtrack(x, y);
				}
			}
		}
		// Option 4: Robot must move southwest to reach goal
		else 
		{
			// Ensures that the robot is not at the goal position (else no further moves would be required)
			if (value.equals("G") == false) 
			{
				// Marks position as visited
				solution[x][y] = 1;
				// South
				if (isSafe(x, y + 1) == true) 
				{
					y = y + 1;
				}
				// West
				else if (isSafe(x - 1, y) == true) 
				{
					x = x - 1;
				}
				// North
				else if (isSafe(x, y - 1) == true) 
				{
					y = y - 1;
				}
				// East
				else if (isSafe(x + 1, y) == true) 
				{
					System.out.print("East" + isSafe(x + 1, y));
					x = x + 1;
				}
				// Backtracks
				else 
				{
					backtrack(x, y);
				}
			}
		}
		// Updates current position
		currentX = x;
		currentY = y;

		// Reassess the value of the cell
		value = maze.getCell(x, y);
	}

	// Checks if position is safe to move to and returns true or false
	public boolean isSafe(int x, int y) 
	{
		boolean result;
		// Checks if cell is out of bounds
		if (x >= numColumns || y >= numRows) 
		{
			result = false;
		}
		// Checks if cell is out of bounds (negative coordinates)
		else if (x < 0 || y < 0) 
		{
			result = false;
		}
		// Checks if cell is blocked
		else if (maze.getCell(x, y).equals("0")) 
		{
			result = false;
		}
		// Checks if cell has already been visited
		else if (solution[x][y] == 1) 
		{
			result = false;
		}
		// Checks if cell leads to a dead end
		else if (deadEnd[x][y] == 1) 
		{
			result = false;
		}
		// Identifies safe cell
		else 
		{
			result = true;
		}
		return result;
	}

	// Backtracks if the cell does not lead to a viable path
	public void backtrack(int x, int y) 
	{
		// Marks cell on the dead end array
		deadEnd[x][y] = 1;
		// Resets solution path so the robot can re-position itself and revisit old cells as it backtracks
		for (int a = 0; a < numColumns; a++) 
		{
			for (int b = 0; b < numRows; b++) 
			{
				solution[a][b] = 0;
			}
		}
	}

	// Identifies the robot's position relative to the goal position so it can move accordingly
	public String setDirection(int startX, int startY, int endX, int endY) 
	{
		String direction = null;
		// Robot needs to move southeast
		if (startX <= endX && startY <= endY) 
		{
			direction = "SE";
		}
		// Robot needs to move southwest
		else if (startX >= endX && startY <= endY) 
		{
			direction = "SW";
		}
		// Robot needs to move northeast
		else if (startX <= endX && startY >= endY) 
		{
			direction = "NE";
		}
		// Robot needs to move northwest
		else 
		{
			direction = "NW";
		}
		
		//Returns the choice of direction
		return direction;
	}

	// Gets the current x coordinate
	public int getX() {
		return currentX;
	}

	// Gets the current y coordinate
	public int getY() {
		return currentY;
	}

}
