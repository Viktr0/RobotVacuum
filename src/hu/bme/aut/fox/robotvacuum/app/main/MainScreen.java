package hu.bme.aut.fox.robotvacuum.app.main;

import hu.bme.aut.fox.robotvacuum.app.App;
import hu.bme.aut.fox.robotvacuum.app.clock.ClockScreen;

import javax.swing.*;
import java.awt.*;

public class MainScreen extends App.Screen {

	public MainScreen() {
		BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
		setLayout(layout);

		JButton clockButton = new JButton("Clock");
		clockButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		clockButton.addActionListener(
				(event) -> navigate(new ClockScreen())
		);

		JButton exitButton = new JButton("Exit");
		exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		exitButton.addActionListener(
				(event) -> System.exit(0)
		);

		add(Box.createVerticalGlue());
		add(clockButton);
		add(exitButton);
		add(Box.createVerticalGlue());
	}
}
