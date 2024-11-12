package dtcc.itn262.towerofhanoi.main_menu;

import dtcc.itn262.towerofhanoi.settings.GameSettings;
import dtcc.itn262.towerofhanoi.entry_point.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class SplashScreenController {
	@FXML
	private Button startButton;
	@FXML
	private TextField numDisksInput;
	@FXML
	private Label errorLabel;

	private Main mainApp;

	public void setMainApp(Main mainApp) {
		this.mainApp = mainApp;
	}

	@FXML
	private void initialize() {
		// Set a listener on the text field to enable the button when input is valid
		numDisksInput.textProperty().addListener((observable, oldValue, newValue) -> {
			boolean isValid = validateInput(newValue);
			startButton.setDisable(!isValid); // Enable button only if input is valid
		});
	}

	// Centralized validation method
	private boolean validateInput(String input) {
		try {
			int numDisks = Integer.parseInt(input.trim());
			int initialNumberOfDisks = 3;
			if (numDisks >= initialNumberOfDisks) {
				errorLabel.setText(""); // Clear any previous error message if valid
				return true;
			} else {
				errorLabel.setText("Please enter an integer 3 or greater.");
				return false;
			}
		} catch (NumberFormatException e) {
			errorLabel.setText("Please enter a valid integer.");
			return false;
		}
	}

	@FXML
	private void startGame() {
		if (validateInput(numDisksInput.getText().trim())) {
			GameSettings.getInstance().setNumDisks(Integer.parseInt(numDisksInput.getText().trim())); // Store in GameSettings
			mainApp.showGameScreen(); // Switch to game screen
		} else {
			// Prevent multiple clicks and show error if input is invalid
			startButton.setDisable(true);
		}
	}
}
