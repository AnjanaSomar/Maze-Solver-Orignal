/*
Anjana Somasundaram
January.08.2018
Mr. Radulovich - ICS3U
Array Assignment
Uses 2D arrays to load given text file and identifies an available path from the starting point (R) to the goal (G)
This class draws the grid and implements the algorithm
*/

package ArrayAssignment;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.image.*;

public class MazeSolver extends Application 
{
	// Window Dimensions (pixels)
	private final int width = 600;
	private final int height = 800;

	// Map Text File
	private final String filename = "src/ArrayAssignment/Maze2.txt";

	// Loads Map Class (responsible for loading text file in 2D array)
	private Map maze;
	
	// Loads PathFinder Class (responsible for finding the path the robot must travel)
	private PathFinder pathfinder;

	// Loads Robot Class (responsible for loading robot image)
	private Robot robot;
	private ImageView robotImageView;

	// Variables used for animation purposes
	private long oldTime;
	private int updateCounter = 0;

	// Starting coordinates (R)
	private int startX;
	private int startY;

	// Ending coordinates (G)
	private int endX;
	private int endY;

	// Marks current coordinates as the robot navigates around the map
	private int currentX;
	private int currentY;

	// Finds dimension of maze
	private int numRows;
	private int numColumns;

	// Calculates the spacing to accurately position robot image
	private double xSpace;
	private double ySpace;

	// Initializes graphics
	private GraphicsContext gc;

	// Records score
	private int score;
	
	public static void main(String[] args) 
	{
		// Sets up the app and launches it
		launch(args);
	}

	@Override
	public void start(Stage primarystage) throws Exception 
	{
		// Initializes graphics and canvas
		Canvas canvas = new Canvas(width, height);
		gc = canvas.getGraphicsContext2D();

		// Loads map and sets up the maze by creating the 2D array
		maze = new Map();
		maze.readMap(filename);
		maze.Grid();
		
		// Initializes path finder
		pathfinder=new PathFinder();
		pathfinder.initialize();

		// Calculates the dimensions of the given maze
		numRows = maze.getHeight(filename);
		numColumns = maze.getWidth(filename);

		// Initializes robot and robot image
		robot = new Robot();
		robotImageView = robot.getRobotImageView();

		// Adds the images to the window
		Pane root = new Pane();
		root.getChildren().add(canvas);
		root.getChildren().add(robotImageView);

		// Sets the scene and draws it
		Scene scene = new Scene(root, width, height);
		drawScene(gc);

		// Sets up a timer to repeatedly redraw the scene
		oldTime = System.nanoTime();
		AnimationTimer timer = new AnimationTimer() 
		{
			public void handle(long now) 
			{
				double deltaT = (now - oldTime) / 1000000000.0; //in nanoseconds
				onUpdate(deltaT);
				oldTime = now;
			}
		};

		// Calls method onUpdate () method once every 1/60 of a second
		timer.start();

		// Sets title
		primarystage.setTitle("Maze Solver");

		// Set the scene for this stage
		primarystage.setScene(scene);

		// Shows the primary stage
		primarystage.show();
	}

	// Draws original grid
	private void drawScene(GraphicsContext gc) 
	{
		// Width of column
		xSpace = width / numColumns;

		// Height of row
		ySpace = height / numRows;

		// Sets robot image to fit the dimension of each grid cell
		robotImageView.setFitWidth(xSpace);
		robotImageView.setFitHeight(ySpace);

		// Sets line colour and width
		gc.setStroke(Color.BLACK);
		gc.setLineWidth(1);

		// Draws the rows of the grid
		for (int i = 0; i <= numRows; i+=1) 
		{
			gc.strokeLine(0, i * ySpace, width, i * ySpace);
		}

		// Draws the columns of the grid
		for (int i = 0; i <= numColumns; i+=1) 
		{
			gc.strokeLine(i * xSpace, 0, i * xSpace, height);
		}

		// Colours the map as per the provided legend
		for (int y = 0; y < numRows; y+=1) 				// The y coordinate corresponds to the row number
		{
			for (int x = 0; x < numColumns; x+=1) 		// The x coordinate corresponds to the column number
			{
				// Represents obstacle
				if (maze.getCell(x, y).equals("0")) 
				{
					gc.setFill(Color.BLUE);
				} 
				// Represents empty space
				else if (maze.getCell(x, y).equals("1")) 
				{
					gc.setFill(Color.WHITE);
				} 
				// Represents robot position
				else if (maze.getCell(x, y).equals("R"))
				{
					gc.setFill(Color.GREEN);
					
					// Sets beginning x and y coordinates
					startX = x;
					startY = y;
					
					// Sets current x and y coordinates to the initial start position
					currentX = startX;
					currentY = startY;

					// Sets robot image to the starting coordinates
					robotImageView.setX(currentX * xSpace);
					robotImageView.setY(currentY * ySpace);
				}
				// Represents goal position
				else
				{
					gc.setFill(Color.RED);
					
					// Sets ending x and y coordinates
					endX = x;
					endY = y;
				} 
				// Fills each grid cell with the corresponding colour
				gc.fillRect(x * xSpace, y * ySpace, xSpace - 0.5, ySpace - 0.5);
			}
		}
	}

	// Moves the robot every 0.5 seconds
	private void onUpdate(double deltaT) 
	{
		// Counter updates
		updateCounter+= 1;
		
		// Moves robot every 0.5 seconds
		if (updateCounter == 25)
		{
			// Stores the current x and y coordinates as visitedX and visitedY
			int visitedX=currentX;
			int visitedY=currentY;
			
			// Attempts to solve the maze from the current x and y position
			pathfinder.solveMaze(currentX, currentY,endX,endY);
				
			// Colours the visited cell yellow if it is not the goal and if the robot is able to move to a new position
			if (maze.getCell(currentX, currentY).equals("G") ==false && pathfinder.getX()!=visitedX || pathfinder.getY()!=visitedY)
			{
				gc.setFill(Color.YELLOW);
				gc.fillRect(currentX * xSpace, currentY * ySpace, xSpace - 0.5, ySpace - 0.5);
			}
			
			// Resets counter
			updateCounter = 0;
			
			// Prints final score if destination is reached
			if (maze.getCell(currentX, currentY).equals("G") == true) 
			{
				System.out.print("The robot has reached the destination. The final score is: "+ score);
				
				// Sets counter such that it will never enter this loop again
				updateCounter = 1000;
			}
			
			// Updates the currentX and currentY coordinates to its new position
			currentX=pathfinder.getX();
			currentY=pathfinder.getY();
			
			//Moves robot to the new position
			if (pathfinder.getX()!=visitedX || pathfinder.getY()!=visitedY)
			{
				robotImageView.setX(currentX * xSpace);
				robotImageView.setY(currentY * ySpace);
				
				// Increases score with each additional movement
				score+=1;
			}					
		}
	}
}
