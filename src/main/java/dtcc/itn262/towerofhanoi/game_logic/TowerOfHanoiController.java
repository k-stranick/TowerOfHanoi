package dtcc.itn262.towerofhanoi.game_logic;

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
	private VBox selectedRod; // tracks the tower that the user has selected

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


	private Rectangle createDisk(int count) {
		Rectangle disk = new Rectangle((30 * count), 20);  // define the size of the disk as constant
		disk.setFill(Color.BLUE);
		disk.setArcWidth(10);
		disk.setArcHeight(10);
		return disk;
	}


	@FXML
	private void handleRodClick(VBox tower, Deque<Rectangle> towerDisks) { //TODO RENAME
		if (selectedRod == null && !towerDisks.isEmpty()) {
			// Select the top disk from this tower
			selectedRod = tower; // Set the selected tower
			moveLog.appendText("Picked up disk from Tower " + getTowerNumber(tower) + "\n");
		} else if (selectedRod != null) {
			if (canPlaceDisk(towerDisks, selectedRod)) {
				// Move the disk to this tower
				moveDisk(selectedRod, tower, towerDisks);
				moveLog.appendText("Moved disk to Tower " + getTowerNumber(tower) + "\n");
				selectedRod = null; // Reset selection
			 	/*if (isSolved()) {
					moveLog.appendText("Congratulations! You've solved the puzzle.\n");}

			}*/
			} else {
				moveLog.appendText("Invalid move. Cannot place larger disk on top of a smaller one.\n");
			}
		}
	}

	private void handleWin() {
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

		// Optionally, offer to reset or start a new game
		moveLog.appendText("Game Over! You've solved the puzzle.\nPress 'Restart' to play again.\n");
	}


	// Check if the game is solved (all disks on the third tower)
	private boolean isSolved() {
		return tower3.getChildren().size() == GameSettings.getInstance().getNumDisks();
	}


	private boolean canPlaceDisk(Deque<Rectangle> targetRodDisks, VBox fromRod) {
		if (targetRodDisks.isEmpty()) return true;
		Rectangle movingDisk = getTopDisk(fromRod);
		Rectangle targetTopDisk = targetRodDisks.peek();
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
			getRodDisks(fromRod).pop();
			toRodDisks.push(disk);
			if (isSolved()) {
				handleWin();
			}
		}
	}


	private Deque<Rectangle> getRodDisks(VBox tower) { //TODO RENAME
		if (tower == tower1) return tower1ArrayDeque;
		if (tower == tower2) return tower2ArrayDeque;
		return tower3ArrayDeque;
	}


	private int getTowerNumber(VBox tower) { // TODO RENAME
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
		handleRodClick(tower1, tower1ArrayDeque);
	}

	@FXML
	public void handleTower2Clicked() {
		handleRodClick(tower2, tower2ArrayDeque);
	}

	@FXML
	public void handleTower3Clicked() {
		handleRodClick(tower3, tower3ArrayDeque);
	}
}
