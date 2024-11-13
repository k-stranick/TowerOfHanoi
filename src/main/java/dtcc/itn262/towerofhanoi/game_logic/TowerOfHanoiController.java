package dtcc.itn262.towerofhanoi.game_logic;

import dtcc.itn262.towerofhanoi.entry_point.Main;
import dtcc.itn262.towerofhanoi.settings.GameSettings;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.*;

public class TowerOfHanoiController {
	// Stacks to keep track of disks on each tower
	private final Deque<Rectangle> tower1ArrayDeque = new ArrayDeque<>();
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

	/**
	 * Initializes the Tower of Hanoi game with the given number of disks.
	 * This method is called when the game is started or restarted with a new number of disks.
	 *
	 * @param numDisksEnteredByPlayer the number of disks entered by the player
	 */
	@FXML
	public void initializeGame(int numDisksEnteredByPlayer) {
		setupDisks(numDisksEnteredByPlayer); // Initialize the game with the given number of disks
	}

	/**
	 * `setupDisks` method initializes the game with the given number of disks.
	 * This method creates the disks and adds them to the first tower.
	 * The disks are represented as rectangles with varying widths, where each disk is smaller than the one below it.
	 * The disks are added to the first tower in descending order, with the smallest disk at the top.
	 * @param numDisksEnteredByPlayer the number of disks entered by the player
	 * */
	private void setupDisks(int numDisksEnteredByPlayer) {
		List<Rectangle> disks = new ArrayList<>(); // temp list to store all the disks
		for (int i = numDisksEnteredByPlayer; i >= 1; i--) {
			Rectangle disk = createDisk(i);
			tower1ArrayDeque.addFirst(disk);
			disks.addFirst(disk); // store the disks in a list to add them to the VBox later reducing the number of calls to the VBox's add method
		}
		//Collections.reverse(disks); // reverse the list of disks to add them in the correct order
		//https://www.geeksforgeeks.org/java-program-to-reverse-a-list/

		tower1.getChildren().addAll(disks); // add all the disks to the first tower
	}

	/**
	 * Creates a disk with a width based on its order and a fixed height.
	 * The disk is visually represented as a rectangle with a blue fill color.
	 * The width of the disk is calculated based on its order, where the smallest disk has the largest width.
	 * The height of the disk is fixed to a constant value.
	 *
	 * @param i number of the disk selected by the player
	 * @return a rectangle representing the disk
	 */
	private Rectangle createDisk(int i) {
		int diskHeight = 20;
		int diskWidth = 30;
		// This line creates a rectangle with a width based on its order and a fixed height,
		// visually representing the size of each disk in the Tower of Hanoi game.
		Rectangle disk = new Rectangle((diskWidth * i), diskHeight);  // define the size of the disk as constant
		disk.setFill(Color.BLUE);
		disk.setArcWidth(10); // rounded corners
		disk.setArcHeight(10); // rounded corners
		return disk;
	}


	@FXML
	/*private void handleTowerClick(VBox tower, Deque<Rectangle> towerArrayDeque) {
		if (selectedTower == null && !towerArrayDeque.isEmpty()) {
			// Select the top disk from this tower
			selectedTower = tower; // Set the selected tower
			moveLog.appendText("Picked up disk from Tower " + getTowerNumber(tower) + "\n");
		} else if (selectedTower != null) {
			// Reset selection
			if (isValidMove(towerArrayDeque, selectedTower)) {
				// Move the disk to this tower
				moveLog.appendText("Moved disk to Tower " + getTowerNumber(tower) + "\n");
				moveDisk(selectedTower, tower, towerArrayDeque);

			} else {
				moveLog.appendText("Invalid move. Cannot place larger disk on top of a smaller one.\n");
			}
			resetSelection();
		}
	}*/


	private void handleTowerClick(VBox tower, Deque<Rectangle> towerArrayDeque) {
		if (selectedTower == null && !towerArrayDeque.isEmpty()) {
			// Select the top disk from this tower
			Rectangle topDisk = towerArrayDeque.peek();
			if (topDisk != null) {
				System.out.println("Selected disk width: " + topDisk.getWidth() + " from Tower " + getTowerNumber(tower));
				moveLog.appendText("Picked up disk of width " + topDisk.getWidth() + " from Tower " + getTowerNumber(tower) + "\n");
			}
			selectedTower = tower; // Set the selected tower
		} else if (selectedTower != null) {
			// Move the disk to this tower if the move is valid
			if (isValidMove(towerArrayDeque, selectedTower)) {
				Rectangle movingDisk = getTopDisk(selectedTower);
				System.out.println("Moving disk width: " + (movingDisk != null ? movingDisk.getWidth() : "null") +
						" to Tower " + getTowerNumber(tower));
				moveLog.appendText("Moved disk of width " + (movingDisk != null ? movingDisk.getWidth() : "null") +
						" to Tower " + getTowerNumber(tower) + "\n");
				moveDisk(selectedTower, tower, towerArrayDeque);
			} else {
				moveLog.appendText("Invalid move. Cannot place larger disk on top of a smaller one.\n");
			}
			resetSelection();
		}
	}



	// Allows the user to go back to the main screen
	@FXML
	private void goBackToMainScreen() {
		if (mainApp != null) {
			mainApp.showSplashScreen();
		}
	}


/*	// Check if the game is solved (all disks on the third tower)
	private boolean isSolved() {
		return tower3.getChildren().size() == GameSettings.getInstance().getNumDisks();
	}*/

	private boolean isSolved() {
		// First, check if the number of disks in tower3 matches the expected total
		if (tower3ArrayDeque.size() != GameSettings.getInstance().getNumDisks()) {
			System.out.println("Not all disks are in tower3. Current size: " + tower3ArrayDeque.size());
			return false;
		}

		// Check that disks are in ascending order (smallest at the bottom, largest on top)
		double previousWidth = 0; // Start with the smallest possible width for comparison
		for (Rectangle disk : tower3ArrayDeque) {
			double diskWidth = disk.getWidth();
			System.out.println("Checking disk with width: " + diskWidth);

			if (diskWidth < previousWidth) {
				System.out.println("Disks are out of order. Disk width " + diskWidth + " is smaller than previous width " + previousWidth);
				return false; // Disks are out of order
			}

			previousWidth = diskWidth;
		}

		System.out.println("All disks are in tower3 and in the correct order.");
		return true;
	}




	private boolean isValidMove(Deque<Rectangle> targetTowerDisks, VBox sourceTower) {
		if (targetTowerDisks.isEmpty()) {
			return true;
		}

		Rectangle movingDisk = getTopDisk(sourceTower);
		Rectangle targetTopDisk = targetTowerDisks.peek();

		if (movingDisk == null || targetTopDisk == null) {
			return false;
		}
		return movingDisk.getWidth() < targetTopDisk.getWidth(); // Check if the moving disk is smaller than the target disk
	}


	private Rectangle getTopDisk(VBox tower) {
		if (tower.getChildren().isEmpty()) {
			System.out.println("Warning: Attempted to get top disk from an empty tower.");
			return null;
		}
		return (Rectangle) tower.getChildren().getFirst(); // assuming the top disk is at index 0
	}



	/*private void moveDisk(VBox sourceTower, VBox destinationTower, Deque<Rectangle> diskDequeDestination) {
		Rectangle disk = getTopDisk(sourceTower);
		if (disk != null) {
			sourceTower.getChildren().removeFirst();
			destinationTower.getChildren().addFirst(disk);

			// Update stacks
			getSelectedTowerArrayDeque(sourceTower).pop();
			diskDequeDestination.push(disk);
			if (isSolved()) {
				wonGamePopUp();
			}
		}
	}*/
/*	private void moveDisk(VBox sourceTower, VBox destinationTower, Deque<Rectangle> diskDequeDestination) {
		Rectangle disk = getTopDisk(sourceTower);
		if (disk != null) {
			System.out.println("Moving disk with width: " + disk.getWidth() + " from Tower " + getTowerNumber(sourceTower) +
					" to Tower " + getTowerNumber(destinationTower));
			System.out.println("this! is the deque: " + diskDequeDestination);


			// visually move the disk from the source tower to the destination tower
			sourceTower.getChildren().remove(disk);
			destinationTower.getChildren().addFirst(disk);

			// Update stacks
			getSelectedTowerArrayDeque(sourceTower).pop();
			diskDequeDestination.push(disk);

			if (isSolved()) {
				System.out.println("this is the deque: " + diskDequeDestination);
				wonGamePopUp();
			}
		}
	}*/
	private void moveDisk(VBox sourceTower, VBox destinationTower, Deque<Rectangle> diskDequeDestination) {
		Rectangle disk = getTopDisk(sourceTower);
		if (disk != null) {
			System.out.println("Moving disk with width: " + disk.getWidth() + " from Tower " + getTowerNumber(sourceTower) +
					" to Tower " + getTowerNumber(destinationTower));

			// Debug before move
			System.out.println("Source Deque before move: " + getSelectedTowerArrayDeque(sourceTower));
			System.out.println("Destination Deque before move: " + diskDequeDestination);

			// Visually move the disk from the source tower to the destination tower
			sourceTower.getChildren().remove(disk);
			destinationTower.getChildren().add(0, disk); // Add at the top of the VBox (index 0)

			// Update stacks
			Deque<Rectangle> sourceDeque = getSelectedTowerArrayDeque(sourceTower);
			if (sourceDeque != null) {
				sourceDeque.pop(); // Remove from the source tower deque
			}
			diskDequeDestination.push(disk); // Add to the destination tower deque

			// Debug after move
			System.out.println("Source Deque after move: " + getSelectedTowerArrayDeque(sourceTower));
			System.out.println("Destination Deque after move: " + diskDequeDestination);

			// Check if the game is solved
			if (isSolved()) {
				System.out.println("Game solved! Final state of destination deque: " + diskDequeDestination);
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

	@FXML
	public void resetHandler() {
		// clears the towers
		resetTowers();
		clearMoveLog();
		resetSelection();
		// Reinitialize game with the current number of disks
		initializeGame(GameSettings.getInstance().getNumDisks());
	}

	/**
	 * Clears the move log text area.
	 */
	private void clearMoveLog() {
		moveLog.clear();
	}

	/**
	 * Resets the selected tower to null.
	 */
	private void resetSelection() {
		selectedTower = null;
	}

	/**
	 * Resets the towers and their corresponding dequeue.
	 * This method clears all the disks from each tower and deque,
	 * and ensures that the towers are enabled after the reset.
	 */
	private void resetTowers() {
		// List of towers and their corresponding dequeue
		List<VBox> towers = List.of(tower1, tower2, tower3);
		List<Deque<Rectangle>> towerDequeue = List.of(tower1ArrayDeque, tower2ArrayDeque, tower3ArrayDeque);

		// Reset each tower and its deque to their initial state using a for loop
		for (int i = 0; i < towers.size(); i++) {
			towers.get(i).getChildren().clear();
			towerDequeue.get(i).clear();
			towers.get(i).setDisable(false); // Ensure towers are enabled after reset
		}
	}

	/**
	 * Displays a pop-up alert to inform the user that they have successfully solved the Tower of Hanoi puzzle.
	 * The alert contains a congratulatory message and disables further interactions with the towers.
	 * Additionally, it appends a message to the move log instructing the user to press 'Restart' to play again.
	 */
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
	}//TODO add clear undo/redo

	/**
	 * Sets the main application instance.
	 * This method is used to set the reference to the main application,
	 * allowing the controller to interact with the main application.
	 *
	 * @param mainApp the main application instance
	 */
	public void setMainApp(Main mainApp) {
		this.mainApp = mainApp;
	} // setting stage???


	/** The following class is used to keep track of the moves made by the user.
	 * This is used to implement the undo/redo functionality.
	 */
	private static class Move {
		private final VBox fromTower;
		private final VBox toTower;
		private final Rectangle disk;

		public Move(VBox fromTower, VBox toTower, Rectangle disk) {
			this.fromTower = fromTower;
			this.toTower = toTower;
			this.disk = disk;
		}

		public VBox getFromTower() {
			return fromTower;
		}

		public VBox getToTower() {
			return toTower;
		}

		public Rectangle getDisk() {
			return disk;
		}
	}

}
