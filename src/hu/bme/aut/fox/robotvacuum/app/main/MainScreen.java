package hu.bme.aut.fox.robotvacuum.app.main;

import hu.bme.aut.fox.robotvacuum.app.App;
import hu.bme.aut.fox.robotvacuum.app.clock.ClockScreen;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class MainScreen extends App.Screen {

	public MainScreen() {
		VBox pane = new VBox();
		pane.setAlignment(Pos.CENTER);
		pane.setSpacing(16);

		Button clockButton = new Button("Clock");
		clockButton.setOnMouseClicked(
				(event) -> navigate(new ClockScreen())
		);

		Button exitButton = new Button("Exit");
		exitButton.setOnMouseClicked(
				(event) -> getStage().close()
		);

		pane.getChildren().addAll(
				clockButton,
				exitButton
		);

		setRoot(pane);
	}
}
