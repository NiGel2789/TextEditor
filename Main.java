package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.fxml.FXMLLoader;

/**
 * This is the driver class for the Text Formatter program. Once the program is run, the
 * Main Menu window will open.
 * @author Peter Antonietti, Dustin Dekker-Parrent, Douglas George, Nigel Wong
 */
public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			VBox root = (VBox)FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
