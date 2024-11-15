/*
 * Kyle Stranick
 * ITN 262-401
 * Project: Tower of Hanoi
 * Due: 11/14/2024
 * */
package dtcc.itn262.towerofhanoi.settings;

// class designed to hold the number of disks (or difficulty) of the game
public class GameSettings {
	private static GameSettings instance;
	private int numDisks;

	private GameSettings() {
	}

	public static GameSettings getInstance() { // singleton pattern, to ensure only one instance of GameSettings, is created
		if (instance == null) {
			instance = new GameSettings();
		}
		return instance;
	}

	public int getNumDisks() {
		return numDisks;
	}

	public void setNumDisks(int numDisks) {
		this.numDisks = numDisks;
	}
}
