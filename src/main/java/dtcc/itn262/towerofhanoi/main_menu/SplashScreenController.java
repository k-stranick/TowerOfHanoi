package dtcc.itn262.towerofhanoi.main_menu;

import dtcc.itn262.towerofhanoi.entry_point.Main;
import dtcc.itn262.towerofhanoi.settings.GameSettings;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

public class SplashScreenController {
	@FXML
	private ComboBox<Integer> numDisksComboBox;
	@FXML
	private Button startButton;

	private Main mainApp;

	public void setMainApp(Main mainApp) {
		this.mainApp = mainApp;
	}

	@FXML
	private void initialize() {
		// Populate the ComboBox with disk count options, e.g., from 3 to 10 disks
		for (int i = 3; i <= 10; i++) {
			numDisksComboBox.getItems().add(i);
		}
		numDisksComboBox.setValue(3);
	}

	@FXML
	private void startGame() {
		Integer selectedDisks = numDisksComboBox.getValue();
		if (selectedDisks != null) {
			GameSettings.getInstance().setNumDisks(selectedDisks); // Store in GameSettings
			mainApp.showGameScreen(); // Switch to game screen
		} else {
			// Prevent multiple clicks and show error if input is invalid
			startButton.setDisable(true);
		}
	}
}
