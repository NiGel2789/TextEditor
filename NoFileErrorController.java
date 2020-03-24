package application;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * This is a controller class for the No File Error Popup window of the Text Formatter program. The class includes
 * all functions and GUI element definitions for the the No File Erorr Popup Window.
 * @author Peter Antonietti, Dustin Dekker-Parrent, Douglas George, Nigel Wong
 */
public class NoFileErrorController extends MainMenuController {
	
	/**
	 * Closes the popup.
	 */
	@FXML
	public void backToMainMenu(ActionEvent event) throws IOException 
	{
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.close();
	}
}
