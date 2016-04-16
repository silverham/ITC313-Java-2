/*	Joshua Graham
	 *  ID: 11490893
	 *  ITC-313
	 * 	Date:26/08/2013
	 *  Assignment 1
	 *  Task 2
	 *  This program read in POIs, display them with zoom functionality and saves it to an image
	 *  
	 *  Version 2 - Applet
	 *  16/4/2016
	 */
import javax.swing.*;		//enables use of Jlabels and other GUI elements
import javax.swing.filechooser.FileNameExtensionFilter;
import java.security.AccessControlException;
import java.util.*;			//allows arraylist Iterator (for loop)
import java.util.List;			//allows arraylist
import java.awt.*;			//GUI layouts
import java.awt.event.*;	//GUI Buttons
import java.io.BufferedReader; //allows use of a buffered reader to write data
import java.io.File;
import java.io.InputStreamReader; //accessing data
import javax.imageio.ImageIO;	//allow writing data specifically with images
import java.awt.image.BufferedImage;	//allows java to interface with image data(eg create the image pixels etc)
import java.io.IOException;		//handle file errors
import java.net.URL;

public class Java2Assign1Task1Applet extends JApplet implements ActionListener {	
	private static final long serialVersionUID = 1L;//stop Eclipse complaining
	JButton btnSave;
	JButton btnZoomIn;
	JButton btnZoomOut;
	JLabel savedLabel;
	DrawPanel mainp;
	int zoomLevel;
	JLabel zoomLabel;
	int correction;
	static List<JLabel> pointsLabels;
	//constructor to instantiate the applet
	public Java2Assign1Task1Applet(){}
	public void actionPerformed(ActionEvent e)
    {
		if(e.getSource() == btnSave) {
	        //Execute when button is pressed
	    	String imagefilename = "screenshot.png";  		//set the filename
	    	try {
		    	BufferedImage bi = ScreenImage.createImage(this); 	//create the image by passing the frame
		    	FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG Files", "png", "PNG");
		    	overwriteCheckerJFileChooser fileChooser = new overwriteCheckerJFileChooser();	//this file chooser will check before override files
		    	fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);	//must set to at least files in order for the default filename to show
		    	fileChooser.setSelectedFile(new File(imagefilename));
		    	fileChooser.setFileFilter(filter);
		    	if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
					ImageIO.write(bi, "PNG", fileChooser.getSelectedFile());
					savedLabel.setText("<html>Image saved as: <br>"  + fileChooser.getSelectedFile().getName() + ".</html>"); //confirm file is written with a newline in html
		    	} else {
		    		savedLabel.setText("<html>Saving image<br />cancelled.</html>"); //confirm file is written with a newline in html
		    	}
	    	} catch (AccessControlException e1) {
	    		savedLabel.setText("<html>Error saving image.<br />Permission denied<br /><br />Check java.policy file</html>");
	    	} catch (IOException | AWTException e1) {
				savedLabel.setText("<html>Error saving image.</html>");
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
	public void start(){
		setLayout(new BorderLayout(1, 2));
		btnSave = new JButton("Save Picture"); //make a button to save on command
		btnZoomIn = new JButton("Zoom In"); //make a button to save on command
		btnZoomOut = new JButton("Zoom Out"); //make a button to save on command
		savedLabel = new JLabel("Not Saved."); //set text and set final so we can refer to it from an inner class.
		mainp = new DrawPanel();
		zoomLevel = 100;
		zoomLabel = new JLabel("100%");
		correction = 70;   //The labels get painted at different places for some reason?, we correct it here
		pointsLabels = new ArrayList<JLabel>(); //an arrray of labels so we can refer to them individually
		
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
	    this.add(mainp, BorderLayout.CENTER);	//add the panels
	    this.add(sidep, BorderLayout.EAST);
	    this.setSize(800, 600); 	//a low size chosen so program works on slow screens (no affect on JApplets unless it's a JPanel or something)
	    
	    //commented as you can't run these on JApplets
	    //frame.setTitle("Task 1 - POIs");
	    //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //make the close button quit.
	    //frame.setLocationRelativeTo(null); //center the program on screen
	    this.setVisible(true);	//done setting up, now to display
	    ArrayList<String> POI = new ArrayList<String>();
	    String textFile = "POI.txt";
	    try {
	    	URL source = new URL(getDocumentBase(), textFile);
	    	BufferedReader theText = new BufferedReader(new InputStreamReader(source.openStream()));
	    	String aLine;
			while ((aLine = theText.readLine()) != null) { //if data is in the line (not null)
				POI.add(aLine);		//add the data 3 lines at a time to collection
				}
			theText.close(); //close the reader, reduces memory usage
		} catch (IOException errorDescirption) {
			JLabel errorLabel = new JLabel("<html>An error occurred reading data file '"+ textFile + "'" +
					"<br /><br />" + 
					"Begining Stacktrace: " +
					"<br />" + errorDescirption + "</html>");
			mainp.setLayout(new GridBagLayout());
			mainp.add(errorLabel);
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
}

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
//check file exists before overrite 
//http://stackoverflow.com/questions/3651494/jfilechooser-with-confirmation-dialog
class overwriteCheckerJFileChooser extends JFileChooser{
	private static final long serialVersionUID = 1L;
	@Override
    public void approveSelection(){
        File f = getSelectedFile();
        if(f.exists() && getDialogType() == SAVE_DIALOG){
            int result = JOptionPane.showConfirmDialog(this,"The file exists, overwrite?","Existing file",JOptionPane.YES_NO_CANCEL_OPTION);
            switch(result){
                case JOptionPane.YES_OPTION:
                    super.approveSelection();
                    return;
                case JOptionPane.NO_OPTION:
                    return;
                case JOptionPane.CLOSED_OPTION:
                    return;
                case JOptionPane.CANCEL_OPTION:
                    cancelSelection();
                    return;
            }
        }
        super.approveSelection();
    }
};

//create a component that you can actually draw on. (extends JPanel instead of JFrame)
@SuppressWarnings("serial")
class DrawPanel extends JPanel{
	static List<String> pointsList = new ArrayList<String>(); //a field variable to be used in paint component
	static double factor = 0.5;
	double squareSize[] = {100.0, 50.0};
	
	public static void setMyValue(List<String> locations){
	  pointsList = locations ; //pass variable to a field we can use in paint component
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

