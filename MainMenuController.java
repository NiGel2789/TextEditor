package application;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * This is a controller class for the Main Menu window of the Text Formatter program. The class includes
 * all functions and GUI element definitions for the Main Menu Window.
 * @author Peter Antonietti, Dustin Dekker-Parrent, Douglas George, Nigel Wong
 */
public class MainMenuController extends TextPreviewController {

	static boolean SingleSpace = true; 			// true is single space, false is double space
	static int numOfChars = 80;		   			// maximum number of characters per line
	static int charsThisLine = 0;				// counter for characters in that line (for wrapping)
	static boolean PreviousLineBlank = true;	// boolean to check if the previous line is blank
	static int indentSize = 0;					// counter for the indent size
	static int numColumns = 1;					// number of columns
	static String TextBeforeSplitting = "";		
	static boolean leftJust = true;				// boolean for left justification
	static boolean rightJust = false;			// boolean for right justification
	static boolean centerJust = false;			// boolean for center justification
	static boolean equalJust = false;			// boolean for equal justification
	static String commandNumber = "";			// int for command number after -n
	static boolean wrapping = false;			// boolean for wrapping


@FXML
private Label FileNameBox;
public File file = null;

/**
 * A method that opens up a FileChooser window that enables the user to select the file they wish to format.
 * It also displays the path of the file chosen in the window.
 * @param event
 */
@FXML
void chooseFile(ActionEvent event)
{
	FileChooser fileChooser = new FileChooser();
	FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
	fileChooser.getExtensionFilters().add(extFilter);
	file = fileChooser.showOpenDialog(null);

	if (file != null) 
	{
		 FileNameBox.setText(file.getAbsolutePath());
	}
}

/**
 * A method that executes once the "Import and Format" button is pressed. The text get's put into the formatter() method
 * and the Text Preview window is opened.
 * If no file is chosen and this method is called, a No File Error popup window will open to warn the user.
 * @param event
 * @throws IOException
 */
@FXML
public void importAndFormat(ActionEvent event) throws IOException 
{
	if (file != null) 
	{
		formatter();
		Parent previewParent = FXMLLoader.load(getClass().getResource("TextPreview.fxml"));
		Scene previewScene = new Scene(previewParent);
	
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
	
		window.setScene(previewScene);
		window.show();
	}
	
	else
	{
		Parent errorParent = FXMLLoader.load(getClass().getResource("NoFileError.fxml"));
		Scene errorScene = new Scene(errorParent);
		
		Stage window = new Stage();
		window.setTitle("Error!");
		window.setScene(errorScene);
		window.show();
	}
}

/**
 * A method takes the user back to the main menu.
 */
@FXML
public void backToMainMenu(ActionEvent event) throws IOException 
{
	Parent previewParent = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
	Scene previewScene = new Scene(previewParent);
	
	Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
	
	window.setScene(previewScene);
	window.show();
}

/**
 * A method that formats the text provided by the user in the text file selected.
 * @throws IOException
 */
public void formatter() throws IOException
{
	// Gets the user's home folder
	String userHomeFolder = System.getProperty("user.home");
	// Creates the file to be exported
	File exportFile = new File(userHomeFolder, "FormattedText.txt");
	
	if (!exportFile.exists())
	{
		// Creates the file if it doesn't already exist
		exportFile.createNewFile();
	}
	
	// Instantiates an instance of the PrintStream, which will be used to write to the FormattedText.txt file
	PrintStream o = new PrintStream(exportFile);
	// Sets an instance of the PrintStream to System.out
	PrintStream console = System.out;
	// Sets the output to the FormattedText.txt file
	System.setOut(o);
	// Scans the unformatted text file
	Scanner sc = new Scanner(file);
	
	// While there is still text in the unformatted text file
	while(sc.hasNextLine())
	{
		// Gets the line
		String line = sc.nextLine();
		
		// If the line is not empty
		if (line.length() != 0) 
		{
			// Get the command
			String command = line.substring(0, 2);
			
			// Number of Charactrs Command
			// Changes the number of characters
			if(command.equals("-n")) {
				commandNumber = line.substring(2);  // get number for command
				numOfChars = Integer.parseInt(commandNumber);
				numOfChars = numOfChars - indentSize;
			}
			
			if(command.equals("-l")) {  // turn left justification on
				leftJust = true;
				rightJust = false;
				centerJust = false;
				equalJust = false;
			}
			else if(command.equals("-r")) {  // turn right justification on
				leftJust = false;
				rightJust = true;
				centerJust = false;
				equalJust = false;
			}
			else if(command.equals("-c")) {  // turn center justification on
				leftJust = false;
				rightJust = false;
		 		centerJust = true;
				equalJust = false;
			}
			else if(command.equals("-e")) {  // turn equal justification on
				leftJust = false;
				rightJust = false;
				centerJust = false;
				equalJust = true;
			}
			
			// Turns Single Spacing on
			else if (command.equals("-s"))
	    	{
	    		SingleSpace = true;
	    	}
			
			// Turns double spacing on
			else if (command.equals("-d"))
	    	{
	    		SingleSpace = false;
	    	}
			
			// Turns text wrapping on or off
			else if (command.equals("-w"))
	    	{
				// Turns text wrapping on
	    		String commandSymbol = line.substring(2);
	    		if(commandSymbol.equals("+"))
	    		{
	    			//String str = sc.nextLine();
		    		wrapping = true;
		    		//printline(str + sc.nextLine());
	    		}
	    		
	    		// Turns text wrapping off
	    		if(commandSymbol.equals("-"))
	    		{
	    			wrapping = false;
	    		}
	    	}
			
			// The Title Command
	    	else if (command.contentEquals("-t"))
	    	{
	    		// get the line
	    		line = sc.nextLine();
	    		// centre aligns the title text
	    		int spaces = numOfChars - line.length();
	    		spaces = spaces / 2;
	    		for (int j = 0; j < spaces; j++)
	    		{
	    			System.out.print(" ");	// prints out the spaces
	    		}
	    		
	    		System.out.println(line);
	    		
	    		// same group of for loops to centre alignt the underline
	    		for (int j = 0; j < spaces; j++)
	    		{
	    			System.out.print(" ");
	    		}
	    		
	    		// prints the underline
	    		for (int i = 0; i < line.length(); i++)
	    		{
	    			System.out.print("-");
	    		}
	    		
	    		System.out.println();
	    	}
	    		
	    		else if (command.equals("-n"))
		    	{
		    		// takes the number to change the line to
		    		String number = line.substring(2);
		    		// converts it to an integer
		    		numOfChars = Integer.parseInt(number);
		    	}
			
				// The paragraph command
	    		else if (line.contains("-p"))
				{
					paragraph(line);
				}
		    	
				// The blank lines command
		    	else if(line.contains("-b"))
				{
		    		blank(line);
				}
		    	
				// The columns command
		    	else if(line.contains("-a"))
				{
		    		columns(line);
				}
	    		
				// If the command doesn't match, then just print the line.
		    	else
		    	{
		    		printline(line);
		    	}
	   		}
    	else
    	{
    		printline(line);
    	}
		
	}
	
	// If in two column mode
	if (numColumns == 2)
    {
    	printTwoColumns(TextBeforeSplitting);
    }
	
	// After all is done, set the output back to the console
	System.setOut(console);
}

/**
 * A method for the paragraph function of the Text Formatter
 * @param input
 */
public void paragraph(String input)// p# function
{
	input = input.replaceAll("[^0-9]", "");
	indentSize = Integer.parseInt(input);// reads the number after "p"
}

/**
 * A method for the blank lines function of the Text Formatter
 * @param input
 */
public static void blank(String input)// b# function
{
	input = input.replaceAll("[^0-9]", "");
	int blankLines = Integer.parseInt(input);// reads number after "b"
	
	if (numColumns == 2)
	{
		for (int i=1; i<=blankLines;i++)
		{
			TextBeforeSplitting += "\n";// add a blank line to TBS "blankLines" times
		}
	}
	else
	{
		
		for (int i=1; i<=blankLines;i++)
		{
			System.out.println();// print a blank line "blankLines" times
		}
		
	}
	
	if (blankLines > 0)
	{
		PreviousLineBlank = true;
	}
	
}

/**
 * A method for the columns function of the Text Formatter
 * @param input
 */
public static void columns(String input)
{
	input = input.replaceAll("[^0-9]", "");
	int colIn = Integer.parseInt(input);//reads number after "a"
	
	
	if((colIn == 1)&&(numColumns == 2))//if switching from 2 column mode to 1 column mode
	{
		printTwoColumns(TextBeforeSplitting);//print collated text
		TextBeforeSplitting = "";
	}
	
	
	if ((colIn == 2)||(colIn==1))//# columns must be 1 or 2
	{
		numColumns = colIn;//value used in printline function
	}
	//else display error;
	
}

/**
 * A method to print the two columns in the Column function of the Text Formatter
 * @param TextBeforeSplitting
 */
public static void printTwoColumns(String TextBeforeSplitting)
{
	int mid = 0;
	
	List<String> list = new ArrayList<String>();
	
	while (TextBeforeSplitting.length()>0)
	{
		if (TextBeforeSplitting.length()>35)
			{
				list.add(TextBeforeSplitting.substring(0, 35));//add full 35 char line to arrayList
				TextBeforeSplitting = TextBeforeSplitting.substring(35);//prune TBS
			}
		else
			{
				list.add(TextBeforeSplitting);//add remainder of line to arrayList
				TextBeforeSplitting = "";//reset TBS
			}
	}
	
	// convert list to array
    String [] array = list.toArray( new String[ list.size() ] );
    mid = ((array.length /2) + 1);
    
    if ((array.length %2 == 0)&&(array.length > 0)) 
    	{
    	mid = (array.length /2);
        	//for (int i = 0; i <= mid; i++)
    		for (int i = 0; i < mid; i++)
		        {
    			if (mid + i >= array.length)
        		{
        			System.out.printf("%-35s %-10s %-35s%n", array[mid], "", "");// print in two columns
        		}
    			else{
	        		System.out.printf("%-35s %-10s %-35s%n", array[i], "", array[mid + i]);// print in two columns
    			}
	        		
    			/*if (i == (mid - 1))
	        		{
	        			System.out.printf("%35s %10s %35s%n", array[mid], "", "");// print in two columns
	        		}*/
		        }
    	}
    else if (array.length > 0)
    {
    	mid = ((array.length /2)+1);
    	for (int i = 0; i < mid; i++)
	        {
    		
    		if (mid + i >= array.length)
        		{
        			System.out.printf("%-35s %-10s %-35s%n", array[mid], "", "");// print in two columns
        		}
    		else
    		{
        		System.out.printf("%-35s %-10s %-35s%n", array[i], "", array[mid + i]);// print in two columns
    		}
        			        		
        		/*if (i == (mid - 1))
        		{
        			System.out.printf("%35s %10s %35s%n", array[mid], "", "");// print in two columns
        		}*/
	        }
    	
    }
    
}

/**
 * The main printline function of the Text Formatter, takes care of all the wrapping, justifications,
 * and others.
 * @param line
 */
public static void printline (String line) {
	//String str2 = "";
	//String str3 = "";
	//String str4 = "";
	//if (wrapping) 
	//{
		/*str2 = line; 
		if(str2.length() < numOfChars)
		{
			str3 = str2 + str3;
		}
		if(str3.length() > numOfChars) 
		{
			//str4 is the substring of the previous line
			str4 = str3.substring(str2.length());
			//sets str3 into its substring rather than an entire line
			str3 = str2 + str3.substring(str2.length(), numOfChars);
			//makes str4 into a substring containing the correct characters after str3.
			str4 = str4.substring(str3.length() - str2.length());
		}*/
	//}
	
	 if (numColumns == 2)// if two column mode
	 {
		 formatTwoColumns(line);   
	 }
	 
	 else{
		 
  	 if(PreviousLineBlank)
	   		{
	   		
	   			for (int i=1; i<=indentSize;i++)//repeat "indentSize" times
					{
	   					System.out.print(" ");//add space to front of line
					}
	   		}
  		
  		// if line is empty or is a command
  		if((line.replaceAll("\\s+", "").length() == 0)||(line.substring(0,1).contains("-")))
	   		{
	   			PreviousLineBlank = true;// next line will be indented
	   		}
  		else PreviousLineBlank = false;// next line will not be indented
  		
  		
  		
		if (SingleSpace)
		{
			// if the line is longer than the number of characters
			if (!wrapping) 
			{
				if (line.trim().length() > numOfChars)
				{
				// only prints til the number of characters
					if(leftJust) {
						String padded = String.format("%-" + (numOfChars - indentSize) + "s", line.trim().substring(0, (numOfChars - indentSize)));
						System.out.println(padded);
						// recursively call the method for the characters after that
						printline(line.substring((numOfChars - indentSize)));
						indentSize = 0;
					}
					else if(rightJust) {
						String padded = String.format("%" + (numOfChars - indentSize) + "s", line.trim().substring(0,(numOfChars - indentSize)));
						System.out.println(padded);
						// recursively call the method for the characters after that
						printline(line.substring((numOfChars - indentSize)));
						indentSize = 0;
					}
					else if(centerJust) {
						String outputString = String.format("%"+ (numOfChars - indentSize) +"s%s%" + (numOfChars - indentSize) +"s", "",line.trim().substring(0,(numOfChars - indentSize)),"");
						float mid = (outputString.length()/2);
						float start = mid - ((numOfChars - indentSize)/2);
						float end = start + (numOfChars - indentSize);
						System.out.println(outputString.substring((int)start, (int)end));
						// recursively call the method for the characters after that
						printline(line.substring((numOfChars - indentSize)));
						indentSize = 0;
					}
					else if(equalJust) {
						String outputString = String.format("%"+ (numOfChars - indentSize) +"s%s%" + (numOfChars - indentSize) +"s", "",line.trim().substring(0,(numOfChars - indentSize)),"");
						float mid = (outputString.length()/2);
						float start = mid - ((numOfChars - indentSize)/2);
						float end = start + (numOfChars - indentSize);
						System.out.println(outputString.substring((int)start, (int)end));
						// recursively call the method for the characters after that
						printline(line.substring((numOfChars - indentSize)));
						indentSize = 0;
					}
				
				}
			
				else
				{
				if(leftJust) {
					String padded = String.format("%-" + (numOfChars - indentSize) + "s", line.trim());
					System.out.print(padded);
					System.out.println();
					indentSize = 0;
					}
				else if(rightJust) {
					String padded = String.format("%" + (numOfChars - indentSize) + "s", line.trim());
					System.out.print(padded);
					System.out.println();
					indentSize = 0;
					}
				else if(centerJust) {
					String outputString = String.format("%"+ (numOfChars - indentSize) +"s%s%" + (numOfChars - indentSize) +"s", "",line.trim(),"");
					float mid = (outputString.length()/2);
					float start = mid - ((numOfChars - indentSize)/2);
					float end = start + (numOfChars - indentSize);
					System.out.print(outputString.substring((int)start, (int)end));
					System.out.println();
					indentSize = 0;
					}
				else if(equalJust) {
					String outputString = String.format("%"+ (numOfChars - indentSize) +"s%s%" + (numOfChars - indentSize) +"s", "",line.trim(),"");
					float mid = (outputString.length()/2);
					float start = mid - ((numOfChars - indentSize)/2);
					float end = start + (numOfChars - indentSize);
					System.out.print(outputString.substring((int)start, (int)end));
					System.out.println();
					indentSize = 0;
					}	
				
				}
		}
		
		else
		{
			if ((charsThisLine + line.trim().length() + 1) > numOfChars)
			{
				leftJust = true;
				rightJust = false;
				centerJust = false;
				equalJust = false;
			
				int charsLeftover = numOfChars - charsThisLine;
				int charsToPrint = line.trim().length() - charsLeftover;
				
			// only prints til the number of characters
				if(leftJust) {
				System.out.println(line.trim().substring(0, charsLeftover));
				charsThisLine = 0;
				// recursively call the method for the characters after that
				printline(line.substring(charsToPrint));
				}
			}
		
			else
			{
			leftJust = true;
			rightJust = false;
			centerJust = false;
			equalJust = false;
				
			if(leftJust) {
				charsThisLine += line.trim().length() + 1;
				System.out.print(line.trim() + " ");
				}
			}
		}
	}
		
		// double-spacing
		else if (!SingleSpace)
		{
			if (line.trim().length() > numOfChars)
			{
				if(leftJust) {
					String padded = String.format("%-" + (numOfChars - indentSize) + "s", line.trim().substring(0,(numOfChars - indentSize)));
					System.out.println(padded);
					System.out.println();		// creates the double space
					// recursively call the method for the characters after that
					printline(line.substring((numOfChars - indentSize)));
					indentSize = 0;
				}
				else if(rightJust) {
					String padded = String.format("%" + (numOfChars - indentSize) + "s", line.trim().substring(0,(numOfChars - indentSize)));
					System.out.println(padded);
					System.out.println();		// creates the double space
					// recursively call the method for the characters after that
					printline(line.substring(numOfChars));
					indentSize = 0;
				}
				else if(centerJust) {
					String outputString = String.format("%"+ (numOfChars - indentSize) +"s%s%" + (numOfChars - indentSize) +"s", "",line.trim().substring(0,(numOfChars - indentSize)),"");
					float mid = (outputString.length()/2);
					float start = mid - ((numOfChars - indentSize)/2);
					float end = start + (numOfChars - indentSize);
					System.out.println(outputString.substring((int)start, (int)end));
					System.out.println();		// creates the double space
					// recursively call the method for the characters after that
					printline(line.substring((numOfChars - indentSize)));
					indentSize = 0;
				}
				else if(equalJust) {
					String outputString = String.format("%"+ (numOfChars - indentSize) +"s%s%" + (numOfChars - indentSize) +"s", "",line.trim().substring(0,(numOfChars - indentSize)),"");
					float mid = (outputString.length()/2);
					float start = mid - ((numOfChars - indentSize)/2);
					float end = start + (numOfChars - indentSize);
					System.out.println(outputString.substring((int)start, (int)end));
					System.out.println();		// creates the double space
					// recursively call the method for the characters after that
					printline(line.substring((numOfChars - indentSize)));
					indentSize = 0;
				}
			}
			
			else
			{
				if(leftJust) {
					String padded = String.format("%-" + (numOfChars - indentSize) + "s", line.trim());
					System.out.println(padded + " ");
					System.out.println();	// creates the double space
					indentSize = 0;
				}
				else if(rightJust) {
					String padded = String.format("%" + (numOfChars - indentSize) + "s", line.trim());
					System.out.println(padded + " ");
					System.out.println();	// creates the double space
					indentSize = 0;
				}
				else if(centerJust) {
					String outputString = String.format("%"+ (numOfChars - indentSize) +"s%s%" + (numOfChars - indentSize) +"s", "",line.trim(),"");
					float mid = (outputString.length()/2);
					float start = mid - ((numOfChars - indentSize)/2);
					float end = start + (numOfChars - indentSize);
					System.out.println(outputString.substring((int)start, (int)end));
					System.out.println();	// creates the double space
					indentSize = 0;
				}
				else if(equalJust) {
					String outputString = String.format("%"+ (numOfChars - indentSize) +"s%s%" + (numOfChars - indentSize) +"s", "",line.trim(),"");
					float mid = (outputString.length()/2);
					float start = mid - ((numOfChars - indentSize)/2);
					float end = start + (numOfChars - indentSize);
					System.out.println(outputString.substring((int)start, (int)end));
					System.out.println();	// creates the double space
					indentSize = 0;
				}
			}
		}
	 }
		
	}

/**
 * Method to format the text if in two column mode.
 * @param line
 */
public static void formatTwoColumns (String line) {//concatenates strings instead of printing them immediately
	
	if(PreviousLineBlank)
		{
		
			for (int i=1; i<=indentSize;i++)//repeat "indentSize" times
			{
				line = " " + line;//add space to front of line
			}
			
		}
	
	// if line is empty or is a command
	if((line.replaceAll("\\s+", "").length() == 0)||(line.substring(0,1).contains("-")))
		{
			PreviousLineBlank = true;// next line will be indented
		}
	else PreviousLineBlank = false;// next line will not be indented
	
	if(line.length() == 0)
	{
	//	TextBeforeSplitting += "\n";
	}
	
if (SingleSpace)
{
	if (line.length() > numOfChars)
	{
		TextBeforeSplitting += (line.substring(0, numOfChars));
		printline(line.substring(numOfChars));
	}
	
	else
	{
		TextBeforeSplitting += (line + " ");
		//TextBeforeSplitting += "\n";
	}
}

else if (!SingleSpace)
{
	if (line.length() > numOfChars)
	{
		TextBeforeSplitting += (line.substring(0, numOfChars));
		TextBeforeSplitting += "\n";		// creates the double space
		printline(line.substring(numOfChars));
	}
	
	else
	{
		TextBeforeSplitting +=(line + " ");
		TextBeforeSplitting += "\n";
	}
	
	
}

}


}
