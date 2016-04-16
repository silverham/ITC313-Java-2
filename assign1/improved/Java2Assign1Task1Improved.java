/*	Joshua Graham
	 *  ID: 11490893
	 *  ITC-313
	 * 	Date:26/08/2013
	 *  Assignment 1
	 *  Task 2
	 *  This program read in POIs, display them with zoom functionality and saves it to an image
	 */
import javax.swing.*;		//enables use of Jlabels and other GUI elements

import java.util.*;			//allows arraylist Iterator (for loop)
import java.util.List;			//allows arraylist
import java.awt.*;			//GUI layouts
import java.awt.event.*;	//GUI Buttons
import java.io.BufferedReader; //allows use of a buffered reader to write data
import java.io.DataInputStream;	//allows use of various methods of 
import java.io.FileInputStream;	//processing and 
import java.io.InputStreamReader; //accessing data
import javax.imageio.ImageIO;	//allow writing data specifically with images
import java.awt.image.BufferedImage;	//allows java to interface with image data(eg create the image pixels etc)
import java.io.IOException;		//handle file errors
class ScreenImage
{
	public static BufferedImage createImage(Component programFrame) //Receive the frame
		throws AWTException
	{
		Point somepoint = new Point(0, 0); //make a point
		SwingUtilities.convertPointToScreen(somepoint, programFrame); //convert from program to screen coodinates
		Rectangle coordinates = programFrame.getBounds(); //get the boundry of the screen
		coordinates.x = somepoint.x;	//use the specific area of the program space
		coordinates.y = somepoint.y;
		//capture the an image of the screen  using the coordinates we got earlier
		BufferedImage programImage = new Robot().createScreenCapture(coordinates); 
		return programImage; //made the image, now pass it back to program to be written
	}
}

//create a component that you can actually draw on. (extends JPanel instead of JFrame)
@SuppressWarnings("serial")
class DrawPanel extends JPanel{
	static List<String> pointsList = new ArrayList<String>(); //a field variable to be used in paint component
	static double factor = 0.5;
	double squareSize[] = {100.0, 50.0};
	
	public static void setMyValue(List<String> locations){
	  pointsList = locations; //pass variable to a field we can use in paint component
	  }
	@Override
	public void paintComponent(Graphics g){
		  //draw on g here e.g.
		  super.paintComponent(g); //draw the system buttons etc first
		  g.setColor(Color.BLUE);	//draw the points in blue

		  if (pointsList != null){ //due to threads, paint starts before we get coordinates, so we if we have them first
			  int i = 0;
			  int x, y;
				while (pointsList.size() > i) {
					x = Integer.parseInt(pointsList.get(i));
					i++;
					y = Integer.parseInt(pointsList.get(i));
					i++;
					i++;
					g.drawOval(x, y, 20, 20);
					}
		  		}
		  	}
	}
@SuppressWarnings("serial") //stops IDE eclipse from complaining about serial 
public class Java2Assign1Task1Improved extends JFrame implements ActionListener {
	JButton btnSave = new JButton("Save Picture"); //make a button to save on command
	JButton btnZoomIn = new JButton("Zoom In"); //make a button to save on command
	JButton btnZoomOut = new JButton("Zoom Out"); //make a button to save on command
	JLabel savedLabel = new JLabel("Not Saved."); //set text and set final so we can refer to it from an inner class.
	JFrame frame = new JFrame("Task 1 - POIs"); 
	DrawPanel mainp = new DrawPanel();
	int zoomLevel = 100;
	JLabel zoomLabel = new JLabel("100%");
	int correction = 70;   //The points get painted at different places to some reason?, we correct it here
	static List<JLabel> pointsLabels = new ArrayList<JLabel>(); //an arrray of labels so we can refer to them individually
	
	Java2Assign1Task1(){
		setLayout(new BorderLayout(1, 2));
		//create the main panel to use.
				
				mainp.setLayout(null); //set layout to null so we can put Jlabels at random places

				//Making side panel
				JPanel sidep  = new JPanel();
				sidep.setLayout(new GridLayout(8,1,5,5)); //8 rows, so that button an label are close together at top
				
				sidep.add(btnSave);	//add button to the side
				sidep.add(savedLabel);	//add label to the side
				sidep.add(btnZoomIn);
				sidep.add(btnZoomOut);
		        savedLabel.setSize(100, 100); //set the label big enough to see
		        sidep.add(zoomLabel);
		        zoomLabel.setSize(100, 100);
		        //set up the frame
			    frame.add(mainp, BorderLayout.CENTER);	//add the panels
			    frame.add(sidep, BorderLayout.EAST);
			    frame.setTitle("Task 1 - POIs");
			    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //make the close button quit.
			    frame.setSize(800, 600); 	//a low size chosen so program works on slow screens
			    frame.setLocationRelativeTo(null); //center the program on screen
			    frame.setVisible(true);	//done setting up, now to display
		        ArrayList<String> POI = new ArrayList<String>();
		        try {
		        	String filename = "POI.txt";
					//read the data
					FileInputStream filestream = new FileInputStream(filename);		//make some instances of the file objects to make reading possible
					DataInputStream input = new DataInputStream(filestream);
					BufferedReader theText = new BufferedReader(new InputStreamReader(input));
					String aLine;
					while ((aLine = theText.readLine()) != null) { //if data is in the line (not null)
						POI.add(aLine);		//add the data 3 lines at a time to collection
						}
					theText.close(); //close the reader, reduces memory usage
					
					} 
				catch (Exception errorDescirption) {
					System.err.println("An error occurred reading data file 'POI.txt', begining stacktrace: \n" + errorDescirption);	//produce an error 
					}
		        DrawPanel.setMyValue(POI);	//pass arraylist to method so that we can paint the points
		      int x, y;			//initialise variables 
		  	  String pointName;
		  	  int i = 0;
		  	while (DrawPanel.pointsList.size() > i) {
		  			x = Integer.parseInt(DrawPanel.pointsList.get(i));	//pass elements to variables
		  			y = Integer.parseInt(DrawPanel.pointsList.get(i+1));
		  			pointName = DrawPanel.pointsList.get(i+2);
		  			JLabel aPointLabel = new JLabel(pointName);	//create label for each point
		  			pointsLabels.add(aPointLabel);
		  			mainp.add(pointsLabels.get((i+2)/3));						//setup and add each point to panel 
		  			aPointLabel.setText(pointName);
		  			aPointLabel.setSize(100, 100);
		  			aPointLabel.setLocation(x, y - correction);
		  			i+=3;
		  			}
		  	mainp.revalidate();
		    mainp.repaint();
		    btnSave.addActionListener(this); //attach a listener to the btn that saves
		  	btnZoomIn.addActionListener(this); //attach a listener to the btn that saves
		  	btnZoomOut.addActionListener(this); //attach a listener to the btn that saves
	}
	public void actionPerformed(ActionEvent e)
    {
		
		if(e.getSource() == btnSave) {
	        //Execute when button is pressed
	    	String imagefilename = "screenshot.png";  		//set the filename
	        java.io.File file=new java.io.File(imagefilename);	//create file instance
	        try {
	        BufferedImage bi = ScreenImage.createImage(frame); 	//create the image by passing the frame
	        ImageIO.write(bi, "png", file);	//write image in png format with the file instance
	        savedLabel.setText("<html>Image saved as: <br>"  + imagefilename + ".</html>"); //confirm file is written with a newline in html
	        }catch (AWTException e1) { //catch image creation error
	        	e1.printStackTrace();
	        }catch (IOException e1) {  //catch a writing error
	            e1.printStackTrace();
	        	}
		}else{
			int i;
			double x, y, realX, realY, posX, posY, positiveX, positiveY;
			i = 0;
			String pointName;
			double zoomFactor = 0.02;	//zoom at this speed
			Dimension dim = mainp.getSize();
			double width = dim.width - 10; //dont include the edges
			double height = dim.height - 10;
			double centerX = width / 2;		//find center
			double centerY = height / 2;
			Boolean keepGoing = true;
			
			if (e.getSource() == btnZoomOut) {
				if (zoomLevel >= 5){
					zoomLevel -= 5;
					zoomLabel.setText(zoomLevel + "%");
				} else {
					keepGoing = false;
					zoomLabel.setText("<html>" + zoomLevel + "%<br> You cannot zoom<br> out anymore!</html>");
				}
			}else {
				zoomLevel += 5;	//increase zoom counter
				zoomLabel.setText(zoomLevel + "%");	//set zoom text
			}
			if (keepGoing == true) {
				while (DrawPanel.pointsList.size() > i) {
					realX = Integer.parseInt(DrawPanel.pointsList.get((int) i)); //get our POI data
					realY = Integer.parseInt(DrawPanel.pointsList.get(i+1));
					//set postion incase we don't change them
					posX = realX;
					posY = realY;
					pointName = DrawPanel.pointsList.get(i+2);
					//get relative data
					y = realY - centerY;
					x = realX - centerX;
					
					//get positive number only, other thing will go diagonally
					positiveX = Math.abs(x);
					positiveY = Math.abs(y);
					//get relative angle as well - maths.abs - positive only

					if(e.getSource() == btnZoomIn){
						if (realX > centerX){
							posX = (x + (positiveX  * zoomFactor)) + centerX;
						}else{
							posX = (x - (positiveX  * zoomFactor)) + centerX;
						}
						if (realY > centerY){
							posY = (y + (positiveY  * zoomFactor)) + centerY;
						}else{
							posY = (y - (positiveY  * zoomFactor)) + centerY;
						}
						pointsLabels.get((i+2)/3).setLocation((int)posX, (int)(posY - correction));
					}else{					
						if (realX > centerX){
							posX = (x - (positiveX  * zoomFactor)) + centerX;
						}else{
							posX = (x + (positiveX  * zoomFactor)) + centerX;
						}
						if (realY > centerY){
							posY = (y - (positiveY  * zoomFactor)) + centerY;
						}else{
							posY = (y + (positiveY  * zoomFactor)) + centerY;
						}
						pointsLabels.get((i+2)/3).setLocation((int)posX, (int)(posY - correction));
					}
					DrawPanel.pointsList.set(i, Integer.toString((int)posX));	//set the coordinates data variables
					DrawPanel.pointsList.set(i+1, Integer.toString((int)posY));
					DrawPanel.pointsList.set(i+2, pointName);
					i+=3;
					}
				mainp.revalidate();	//get rid of graphic artifacts
				mainp.repaint();	//this will be called anyway internally but this will make it more apparent
				}
		}
    }
	public static void main(String[] args) throws Exception {
		new Java2Assign1Task1(); //start program
		}
	}

