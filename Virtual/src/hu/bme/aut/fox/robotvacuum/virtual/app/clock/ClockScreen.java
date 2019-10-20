package hu.bme.aut.fox.robotvacuum.virtual.app.clock;

import hu.bme.aut.fox.robotvacuum.virtual.app.App;
import hu.bme.aut.fox.robotvacuum.virtual.viewmodel.ClockViewModel;

import javax.swing.*;

public class ClockScreen extends App.Screen {

	private ClockViewModel viewModel = new ClockViewModel();

	private JLabel hours;
	private JLabel minutes;
	private JLabel seconds;

	public ClockScreen() {
		BoxLayout layout = new BoxLayout(this, BoxLayout.X_AXIS);
		setLayout(layout);

		hours = new JLabel();
		minutes = new JLabel();
		seconds = new JLabel();

		add(Box.createHorizontalGlue());
		add(hours);
		add(new JLabel(":"));
		add(minutes);
		add(new JLabel(":"));
		add(seconds);
		add(Box.createHorizontalGlue());
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
