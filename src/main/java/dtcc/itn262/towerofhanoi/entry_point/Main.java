/*
 * Kyle Stranick
 * ITN 262-401
 * Project: Tower of Hanoi
 * Due: 11/14/2024
 * */
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

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		showSplashScreen();
	}

	public void showSplashScreen() {
		loadScene("/dtcc/itn262/towerofhanoi/SplashScreen.fxml", "Tower of Hanoi", 400, 300, SplashScreenController.class);
	}

	public void showGameScreen() {
		loadScene("/dtcc/itn262/towerofhanoi/TowerOfHanoi.fxml", "Tower of Hanoi - Game", 600, 400, TowerOfHanoiController.class);
	}

	private <T> void loadScene(String fxmlPath, String title, int width, int height, Class<T> controllerClass) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
			Parent root = loader.load();
			primaryStage.setTitle(title);
			primaryStage.setScene(new Scene(root, width, height));

			T controller = loader.getController();
			if (controllerClass == SplashScreenController.class) {
				((SplashScreenController) controller).setMainApp(this);
			} else if (controllerClass == TowerOfHanoiController.class) {
				((TowerOfHanoiController) controller).setMainApp(this);
				int numDisks = GameSettings.getInstance().getNumDisks();
				((TowerOfHanoiController) controller).startNewGame(numDisks);
			}
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
