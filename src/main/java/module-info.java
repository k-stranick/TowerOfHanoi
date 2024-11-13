module dtcc.itn262.towerofhanoi {
	requires javafx.controls;
	requires javafx.fxml;

	requires org.controlsfx.controls;
	requires com.dlsc.formsfx;
	requires org.kordamp.bootstrapfx.core;
	requires java.logging;

	opens dtcc.itn262.towerofhanoi to javafx.fxml;
	exports dtcc.itn262.towerofhanoi.main_menu;
	opens dtcc.itn262.towerofhanoi.main_menu to javafx.fxml;
	exports dtcc.itn262.towerofhanoi.game_logic;
	opens dtcc.itn262.towerofhanoi.game_logic to javafx.fxml;
	exports dtcc.itn262.towerofhanoi.settings;
	opens dtcc.itn262.towerofhanoi.settings to javafx.fxml;
	exports dtcc.itn262.towerofhanoi.entry_point;
	opens dtcc.itn262.towerofhanoi.entry_point to javafx.fxml;
}