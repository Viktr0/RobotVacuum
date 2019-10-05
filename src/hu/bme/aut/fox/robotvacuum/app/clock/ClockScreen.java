package hu.bme.aut.fox.robotvacuum.app.clock;

import hu.bme.aut.fox.robotvacuum.app.App;
import hu.bme.aut.fox.robotvacuum.viewmodel.ClockViewModel;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class ClockScreen extends App.Screen {

	private ClockViewModel viewModel = new ClockViewModel();

	private Label hours;
	private Label minutes;
	private Label seconds;

	public ClockScreen() {
		HBox pane = new HBox();
		pane.setAlignment(Pos.CENTER);

		hours = new Label();
		minutes = new Label();
		seconds = new Label();

		pane.getChildren().addAll(
				hours,
				new Label(":"),
				minutes,
				new Label(":"),
				seconds
		);

		setRoot(pane);
	}

	@Override
	public void onAttach() {
		subscribe(viewModel.hours, (value) -> hours.setText(value.toString()));
		subscribe(viewModel.minutes, (value) -> minutes.setText(value.toString()));
		subscribe(viewModel.seconds, (value) -> seconds.setText(value.toString()));

		viewModel.start();
	}

	@Override
	public void onDetach() {
		viewModel.stop();
	}
}
