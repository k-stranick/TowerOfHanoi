package dtcc.itn262.towerofhanoi.game_logic;

import dtcc.itn262.towerofhanoi.entry_point.Main;
import dtcc.itn262.towerofhanoi.settings.GameSettings;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.*;

public class TowerOfHanoiController {

	private final Deque<Rectangle> tower1ArrayDeque = new ArrayDeque<>(); // Stacks to keep track of disks on each tower
	private final Deque<Rectangle> tower2ArrayDeque = new ArrayDeque<>();
	private final Deque<Rectangle> tower3ArrayDeque = new ArrayDeque<>();
	@FXML
	private VBox tower1;
	@FXML
	private VBox tower2;
	@FXML
	private VBox tower3;
	@FXML
	private TextArea moveLog;
	private VBox selectedTower; // tracks the tower that the user has selected
	private Main mainApp;

	@FXML
	public void initializeGame(int numDisksEnteredByPlayer) {
		setupDisks(numDisksEnteredByPlayer); // Initialize the game with the given number of disks
	}


	private void setupDisks(int numDisksEnteredByPlayer) {
		List<Rectangle> disks = new ArrayList<>();
		for (int i = numDisksEnteredByPlayer; i >= 1; i--) {
			Rectangle disk = createDisk(i);
			tower1ArrayDeque.push(disk);
			disks.add(disk); // store the disks in a list to add them to the VBox later reducing the number of calls to the VBox's add method
		}
		Collections.reverse(disks); //https://www.geeksforgeeks.org/java-program-to-reverse-a-list/

		tower1.getChildren().addAll(disks);
	}


	private Rectangle createDisk(int order) {
		// This line creates a rectangle with a width based on its order and a fixed height,
		// visually representing the size of each disk in the Tower of Hanoi game.
		Rectangle disk = new Rectangle((30 * order), 20);  // define the size of the disk as constant
		disk.setFill(Color.BLUE);
		disk.setArcWidth(10);
		disk.setArcHeight(10);
		return disk;
	}


	@FXML
	private void handleTowerClick(VBox tower, Deque<Rectangle> towerDisks) {
		if (selectedTower == null && !towerDisks.isEmpty()) {
			// Select the top disk from this tower
			selectedTower = tower; // Set the selected tower
			moveLog.appendText("Picked up disk from Tower " + getTowerNumber(tower) + "\n");
		} else if (selectedTower != null) {
			// Reset selection
			if (canPlaceDisk(towerDisks, selectedTower)) {
				// Move the disk to this tower
				moveLog.appendText("Moved disk to Tower " + getTowerNumber(tower) + "\n");
				moveDisk(selectedTower, tower, towerDisks);

			} else {
				moveLog.appendText("Invalid move. Cannot place larger disk on top of a smaller one.\n");
			}
			selectedTower = null; // Reset selection
		}
	}

	// Check if the game is solved (all disks on the third tower)
	private boolean isSolved() {
		return tower3.getChildren().size() == GameSettings.getInstance().getNumDisks();
	}


	private boolean canPlaceDisk(Deque<Rectangle> targetTowerDisks, VBox fromRod) {
		if (targetTowerDisks.isEmpty()) {
			return true;
		}
		Rectangle movingDisk = getTopDisk(fromRod);
		Rectangle targetTopDisk = targetTowerDisks.peek();
		if (movingDisk == null || targetTopDisk == null) {
			return false;
		}
		return movingDisk.getWidth() < targetTopDisk.getWidth();
	}



	private Rectangle getTopDisk(VBox tower) {
		return (Rectangle) tower.getChildren().getFirst();
	}


	private void moveDisk(VBox fromRod, VBox toRod, Deque<Rectangle> toRodDisks) {
		Rectangle disk = getTopDisk(fromRod);
		if (disk != null) {
			fromRod.getChildren().remove(disk);
			toRod.getChildren().addFirst(disk);

			// Update stacks
			getSelectedTowerArrayDeque(fromRod).pop();
			toRodDisks.push(disk);
			if (isSolved()) {
				wonGamePopUp();
			}
		}
	}


	private Deque<Rectangle> getSelectedTowerArrayDeque(VBox tower) {
		if (tower == tower1) return tower1ArrayDeque;
		if (tower == tower2) return tower2ArrayDeque;
		return tower3ArrayDeque;
	}


	private int getTowerNumber(VBox tower) {
		if (tower == tower1) {
			return 1;
		} else if (tower == tower2) {
			return 2;
		} else {
			return 3;
		}
	}


	@FXML
	public void handleTower1Clicked() {
		handleTowerClick(tower1, tower1ArrayDeque);
	}

	@FXML
	public void handleTower2Clicked() {
		handleTowerClick(tower2, tower2ArrayDeque);
	}

	@FXML
	public void handleTower3Clicked() {
		handleTowerClick(tower3, tower3ArrayDeque);
	}

	public void resetHandler() {
		// clears the towers
		resetTowers();
		// Clear move log and reset selected tower
		clearMoveLog();
		resetSelection();
		// Reinitialize game with the current number of disks
		initializeGame(GameSettings.getInstance().getNumDisks());
	}

	private void clearMoveLog() {
		moveLog.clear();
	}

	private void resetSelection() {
		selectedTower = null;
	}

	private void resetTowers() {
		// List of towers and their corresponding dequeue
		List<VBox> towers = List.of(tower1, tower2, tower3);
		List<Deque<Rectangle>> towerDequeue = List.of(tower1ArrayDeque, tower2ArrayDeque, tower3ArrayDeque);

		// Reset each tower and its deque
		for (int i = 0; i < towers.size(); i++) {
			towers.get(i).getChildren().clear();
			towerDequeue.get(i).clear();
			towers.get(i).setDisable(false); // Ensure towers are enabled after reset
		}
	}


	private void wonGamePopUp() {
		// Display a winning message
		Alert winAlert = new Alert(Alert.AlertType.INFORMATION);
		winAlert.setTitle("Congratulations!");
		winAlert.setHeaderText("You've solved the puzzle!");
		winAlert.setContentText("All disks are now on the final tower. Great job!");
		winAlert.showAndWait();

		// Disable further interactions with the towers
		tower1.setDisable(true);
		tower2.setDisable(true);
		tower3.setDisable(true);

		moveLog.appendText("\nPress 'Restart' to play again.\n");
	}

	public void setMainApp(Main mainApp) {
		this.mainApp = mainApp;
	}

	@FXML
	private void goBackToMainScreen() {
		if (mainApp != null) {
			mainApp.showSplashScreen();
		}
	}
}
