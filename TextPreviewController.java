package application;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

/**
 * This is a controller class for the Text Preview window of the Text Formatter program. The class includes
 * all functions and GUI element definitions for the Text Preview Window.
 * @author Peter Antonietti, Dustin Dekker-Parrent, Douglas George, Nigel Wong
 */
public class TextPreviewController {

	public static String toString = "";
	
	@FXML
	public Label userHome;

	@FXML
	public TextArea result;
	
	/**
	 * A method that changes scenes from the Text Preview window to the Main Menu.
	 * @param event
	 * @throws IOException
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
	 * A method that shows the pop up window for when a file is successfully exported to the User's home folder.
	 * @param event
	 * @throws IOException
	 */
	@FXML
	public void exportText(ActionEvent event) throws IOException
	{		
		Parent successParent = FXMLLoader.load(getClass().getResource("Exported.fxml"));
		Scene successScene = new Scene(successParent);
		
		Stage window = new Stage();
		window.setTitle("Success!");
		window.setScene(successScene);
		window.show();
	}
	
	/**
	 * A method that updates the text preview window's "Formatted Text Preview" area with the text from the exported .txt file
	 * @param event
	 * @throws IOException
	 */
	@FXML
	public void showFormatting(ActionEvent event) throws IOException 
	{
		//gets the user's home folder
		String userHomeFolder = System.getProperty("user.home");
		//creates a file
		File exportedFile = new File(userHomeFolder, "FormattedText.txt");
		//updates the Label at the bottom of the Text Preview window with the
		//Location of the File
		userHome.setText(userHomeFolder);
		Scanner sc = new Scanner(exportedFile); 
		
		while (sc.hasNextLine())
		{
			//prints the text from the exported file
			toString += sc.nextLine() + "\n";
		}
		
		result.setText(toString);
	}
	
	// terminates the program
	public void close(ActionEvent event)
	{
		System.exit(0);
	}
}
