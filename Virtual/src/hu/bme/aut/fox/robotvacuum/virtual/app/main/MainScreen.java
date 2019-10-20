package hu.bme.aut.fox.robotvacuum.virtual.app.main;

import hu.bme.aut.fox.robotvacuum.virtual.app.clock.ClockScreen;
import hu.bme.aut.fox.robotvacuumsimulator.app.Screen;
import hu.bme.aut.fox.robotvacuum.virtual.app.world.WorldScreen;

import javax.swing.*;
import java.awt.*;

public class MainScreen extends Screen {

	public MainScreen() {
		BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
		setLayout(layout);

		JButton clockButton = new JButton("Clock");
		clockButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		clockButton.addActionListener(
				(event) -> navigate(new ClockScreen())
		);

		JButton virtualWorldButton = new JButton("VirtualWorld");
		virtualWorldButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		virtualWorldButton.addActionListener(
				(event) -> navigate(new WorldScreen())
		);

		JButton exitButton = new JButton("Exit");
		exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		exitButton.addActionListener(
				(event) -> System.exit(0)
		);

		add(Box.createVerticalGlue());
		add(clockButton);
		add(virtualWorldButton);
		add(exitButton);
		add(Box.createVerticalGlue());
	}
}