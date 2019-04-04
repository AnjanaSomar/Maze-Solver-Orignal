package ArrayAssignment;
import javafx.scene.image.*;
/*
Anjana Somasundaram
January.08.2018
Mr. Radulovich - ICS3U
Array Assignment
Uses 2D arrays to load given text file and identifies an available path from the starting point (R) to the goal (G)
This class loads the robot image
*/

public class Robot 
{
	private final Image robotImage;
	private final ImageView robotImageView;
	private final String filename="file:resources//images//robot2.png";
	
	public Robot()
	{
		//Loads robot image and sets image settings
		robotImage=new Image(filename);
		robotImageView=new ImageView();
		robotImageView.setImage(robotImage);
		
		//Preserves original image ratio
		robotImageView.setPreserveRatio(true);
	}

	// This method allows the robotImageView to be accessed by other classes
	public ImageView getRobotImageView() 
	{
		return robotImageView;
	}
}
