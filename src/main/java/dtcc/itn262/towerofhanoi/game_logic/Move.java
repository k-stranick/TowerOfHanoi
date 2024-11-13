package dtcc.itn262.towerofhanoi.game_logic;

import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

public class Move {
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
