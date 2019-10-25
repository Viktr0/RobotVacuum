package hu.bme.aut.fox.robotvacuum.virtual.app.main;

import hu.bme.aut.fox.robotvacuum.virtual.app.App;
import hu.bme.aut.fox.robotvacuum.virtual.app.clock.ClockScreen;

import hu.bme.aut.fox.robotvacuum.virtual.app.simulationapp.SimulationAppScreen;
import hu.bme.aut.fox.robotvacuum.virtual.app.world.WorldScreen;
import hu.bme.aut.fox.robotvacuum.virtual.app.virtualworld.VirtualWorldScreen;

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

		JButton simulationButton = new JButton("StartSimulation");
		simulationButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		simulationButton.addActionListener(
				(event) -> navigate(new SimulationAppScreen())
		);

		JButton virtualWorldButton = new JButton("VirtualWorld");
		virtualWorldButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		virtualWorldButton.addActionListener(
				(event) -> navigate(new VirtualWorldScreen())
		);

		JButton interpretedWorldButton = new JButton("InterpretedWorld");
		interpretedWorldButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		interpretedWorldButton.addActionListener(
				(event) -> navigate(new WorldScreen())
		);

		JButton exitButton = new JButton("Exit");
		exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		exitButton.addActionListener(
				(event) -> System.exit(0)
		);

		add(Box.createVerticalGlue());
		add(clockButton);
		add(simulationButton);
		add(virtualWorldButton);
		add(interpretedWorldButton);
		add(exitButton);
		add(Box.createVerticalGlue());
	}
}
