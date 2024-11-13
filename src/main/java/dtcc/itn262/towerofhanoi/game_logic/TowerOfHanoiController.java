/*
* Kyle Stranick
* ITN 262-401
* Project: Tower of Hanoi
* Due: 11/14/2024
* */

package dtcc.itn262.towerofhanoi.game_logic;

import dtcc.itn262.towerofhanoi.entry_point.Main;
import dtcc.itn262.towerofhanoi.settings.GameSettings;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.logging.Logger;

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
	private Main mainApp; // Reference to the main application

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
	 * This method creates the disks and adds them to the first tower in.
	 * The disks are represented as rectangles with varying widths, where each disk is smaller than the one below it.
	 * The disks are added to the first tower in ASCENDING ORDER, with the smallest disk at the top represented by index 0.
	 *
	 * @param numDisksEnteredByPlayer the number of disks entered by the player
	 */
	private void setupDisks(int numDisksEnteredByPlayer) {
		List<Rectangle> disks = new ArrayList<>(); // temp list to store all the disks
		// each time (i) is decremented,(i) will become the new "top" disk in the tower
		for (int i = numDisksEnteredByPlayer; i >= 1; i--) {
			Rectangle disk = createDisk(i);
			tower1ArrayDeque.addFirst(disk); // add disk to the "front" of the deque
			disks.addFirst(disk); // store the disks in a list to add them to the VBox later reducing the number of calls to the VBox's add method
		} // this is where I was going wrong, I was not synchronizing the deque with the VBox
		tower1.getChildren().addAll(disks); // add all the disks to the first tower
	}

	/**
	 * Creates a disk with a calculated width based on the given count and a fixed height.
	 * The disk is represented as a rectangle with a blue fill color.
	 * The width of the disk is determined by the given count, where higher numbers produce wider disks.
	 * The height of the disk is fixed to a constant value.
	 *
	 * @param diskSizeFactor the factor used to determine the disk width; higher values yield wider disks
	 * @return a rectangle representing the disk
	 */
	private Rectangle createDisk(int diskSizeFactor) {
		// the final keyword is used to make the variable immutable
		final double DISK_HEIGHT = 20;
		final double BASE_WIDTH = 30;

		// Calculate the disk width based on the diskSizeFactor
		Rectangle disk = new Rectangle(BASE_WIDTH * diskSizeFactor, DISK_HEIGHT);
		disk.setFill(Color.BLUE);
		disk.setArcWidth(10); // Rounded corners for aesthetic
		disk.setArcHeight(10); // Rounded corners for aesthetic

		return disk;
	}

	/**
	 * Handles a tower click event, if no tower is currently selected, set the selected tower.
	 * If a tower is already selected, attempts to move the disk to the clicked tower.
	 *
	 * @param clickedTower the tower clicked by the user
	 * @param clickedTowerDisks the deque of disks on the clicked tower
	 */
	@FXML
	private void handleTowerClick(VBox clickedTower, Deque<Rectangle> clickedTowerDisks) {
		// If no tower is currently selected, set the clicked tower as the selected tower
		if (selectedTower == null && !clickedTowerDisks.isEmpty()) {
			selectedTower = setSelectedTower(clickedTower, clickedTowerDisks);
		}
		// If a tower is already selected, attempt to move the disk to the clicked tower
		else if (selectedTower != null) {
			attemptMove(clickedTower, clickedTowerDisks);
		}
	}

	/**
	 * Selects the specified tower and logs the selection of the top disk in the move log.
	 *
	 * @param tower the tower being selected by the user
	 * @param towerDisks the deque of disks on the selected tower
	 * @return the selected tower (VBox)
	 */
	private VBox setSelectedTower(VBox tower, Deque<Rectangle> towerDisks) {
		// Get the top disk on the selected tower
		Rectangle topDisk = towerDisks.peek();

		// Log the disk selection if a disk exists on the tower
		if (topDisk != null) {
			moveLog.appendText("Picked up disk of width " + topDisk.getWidth() +
					" from Tower " + getTowerNumber(tower) + "\n");
		}

		// Return the selected tower
		return tower;
	}

	/**
	 * Attempts to move a disk from the selected tower to the target tower.
	 * Logs the move if successful or an error message if the move is invalid.
	 *
	 * @param targetTower      the tower to which the disk is being moved
	 * @param targetTowerDisks the deque of disks on the target tower
	 */
	private void attemptMove(VBox targetTower, Deque<Rectangle> targetTowerDisks) {
		// Check if the move is valid
		if (isValidMove(targetTowerDisks, selectedTower)) {
			// Retrieve the top disk from the selected tower
			Rectangle movingDisk = getTopDisk(selectedTower);

			// Log the move action
			if (movingDisk != null) {
				moveLog.appendText("Moved disk of width " + movingDisk.getWidth() +
						" to Tower " + getTowerNumber(targetTower) + "\n");
			}

			// Execute the move
			moveDisk(selectedTower, targetTower, targetTowerDisks);
		} else {
			// Log an invalid move attempt
			moveLog.appendText("Invalid move. Cannot place larger disk on top of a smaller one.\n");
		}

		// Reset the selected tower
		resetSelection();
	}

	/**
	 * Handles the 'Back' button click event.
	 * This method is called when the user clicks the 'Back' button to return to the main menu.
	 */
	@FXML
	private void goBackToMainScreen() {
		if (mainApp != null) {
			mainApp.showSplashScreen();
		}
	}

	/**
	 * Checks if the game is solved.
	 * The game is considered solved if all disks are on the third tower
	 * and arranged in ascending order from top (smallest) to bottom (largest).
	 *
	 * @return true if the game is solved; false otherwise
	 */
	private boolean isSolved() {
		// First, check if the number of disks in tower3 matches the expected total
		if (tower3ArrayDeque.size() != GameSettings.getInstance().getNumDisks()) {
			return false; // If the number of disks is incorrect, the game is not solved
		}
		// Next, initialize the previous width to 0 ensuring that the first disk's width will always be greater than previousWidth initially
		double previousWidth = 0;
		// The for loop iterates over each disk in tower3ArrayDeque,
		// starting from the last added disk (top disk) and moving down
		for (Rectangle disk : tower3ArrayDeque) {
			double currentDiskWidth = disk.getWidth();
			/*System.out.println("Checking disk with width: " + currentDiskWidth); // Debugging*/

			if (currentDiskWidth < previousWidth) {
				Logger.getLogger(TowerOfHanoiController.class.getName()).warning("Disks are out of order. Disk width " + currentDiskWidth + " is smaller than previous width " + previousWidth);
				return false; // Disks are out of order
			} else {
				// Update the previous width for the next iteration by setting it to the current disk's width
				previousWidth = currentDiskWidth;
			}
		}

		// If the loop completes without returning false, all disks are in correct ascending order
		Logger.getLogger(TowerOfHanoiController.class.getName()).info("All disks are in tower3 and in the correct order.");
		return true;

	}

	/**
	 * Determines if a move is valid.
	 * A move is valid if the target tower is empty or if the top disk of the source tower
	 * is smaller than the top disk of the target tower.
	 *
	 * @param targetTowerDisks the deque representing the disks on the target tower
	 * @param sourceTower      the VBox representing the source tower
	 * @return true if the move is valid; false otherwise
	 */
	private boolean isValidMove(Deque<Rectangle> targetTowerDisks, VBox sourceTower) {
		// If the target tower is empty, the move is automatically valid
		if (targetTowerDisks.isEmpty()) {
			return true;
		}

		// Retrieve the top disks of both source and target towers
		Rectangle movingDisk = getTopDisk(sourceTower);
		Rectangle targetTopDisk = targetTowerDisks.peek();

		// Check for null to avoid NullPointerException, return false if either disk is null
		if (movingDisk == null || targetTopDisk == null) {
			return false;
		}

		// The move is valid if the moving disk is smaller than the target tower's top disk
		return movingDisk.getWidth() < targetTopDisk.getWidth();
	}

	/**
	 * Retrieves the top disk from the specified tower.
	 * If the tower is empty, a warning message is displayed and null is returned.
	 *
	 * @param tower the tower from which to retrieve the top disk
	 * @return the top disk from the tower; null if the tower is empty
	 */
	private Rectangle getTopDisk(VBox tower) {
		if (tower.getChildren().isEmpty()) {
			System.out.println("Warning: Attempted to get top disk from an empty tower.");
			return null;
		}
		return (Rectangle) tower.getChildren().getFirst(); // assuming the top disk is at index 0
	}

	private void moveDisk(VBox sourceTower, VBox destinationTower, Deque<Rectangle> diskDequeDestination) {
		Rectangle disk = getTopDisk(sourceTower);
		if (disk != null) {
			/*System.out.println("Moving disk with width: " + disk.getWidth() + " from Tower " + getTowerNumber(sourceTower) +
					" to Tower " + getTowerNumber(destinationTower)); // Debugging*/

			/*// Debug before move
			System.out.println("Source Deque before move: " + getSelectedTowerArrayDeque(sourceTower));
			System.out.println("Destination Deque before move": + diskDequeDestination);*/

			// Visually move the disk from the source tower to the destination tower
			sourceTower.getChildren().remove(disk);
			destinationTower.getChildren().addFirst(disk); // Add at the top of the VBox (index 0)

			// Update stacks
			Deque<Rectangle> sourceDeque = getSelectedTowerArrayDeque(sourceTower);
			sourceDeque.pop(); // Remove from the source tower deque

			diskDequeDestination.push(disk); // Add to the destination tower deque

			/*// Debug after move
			System.out.println("Source Deque after move: " + getSelectedTowerArrayDeque(sourceTower));
			System.out.println("Destination Deque after move: " + diskDequeDestination);*/

			// Check if the game is solved
			if (isSolved()) {
				/*System.out.println("Game solved! Final state of destination deque: " + diskDequeDestination); //Debugging*/
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
	} // TODO add undo/redo reset

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
	}

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


	/** Try to implement for undo/redo?
	 * The following class is used to keep track of the moves made by the user.
	 * This is used to implement the undo/redo functionality.
	 */
	/*private static class Move {
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
	}*/

}
