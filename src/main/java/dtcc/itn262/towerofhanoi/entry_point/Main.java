package dtcc.itn262.towerofhanoi.entry_point;

import dtcc.itn262.towerofhanoi.game_logic.TowerOfHanoiController;
import dtcc.itn262.towerofhanoi.main_menu.SplashScreenController;
import dtcc.itn262.towerofhanoi.settings.GameSettings;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
	private Stage primaryStage;

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/dtcc/itn262/towerofhanoi/SplashScreen.fxml"));
		Parent splashRoot = loader.load();

		SplashScreenController splashController = loader.getController();
		splashController.setMainApp(this); // Allow splash screen to switch scenes
		primaryStage.setTitle("Tower of Hanoi");
		primaryStage.setScene(new Scene(splashRoot, 400, 300));
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

	public void showGameScreen() {
		try {
			// Load the FXML file for the game screen
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/dtcc/itn262/towerofhanoi/TowerOfHanoi.fxml"));
			Parent gameRoot = loader.load();

			// Retrieve the controller for the game screen
			TowerOfHanoiController controller = loader.getController();

			// Initialize the game with the number of disks from GameSettings
			int numDisks = GameSettings.getInstance().getNumDisks();
			controller.initializeGame(numDisks);  // Method in TowerOfHanoiController to set up the game

			// Set up the scene and switch to it
			Scene gameScene = new Scene(gameRoot, 600, 400);
			primaryStage.setTitle("Tower of Hanoi - Game");
			primaryStage.setScene(gameScene);
			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
