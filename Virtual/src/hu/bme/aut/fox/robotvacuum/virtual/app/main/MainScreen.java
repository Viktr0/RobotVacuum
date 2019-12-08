package hu.bme.aut.fox.robotvacuum.virtual.app.main;

import hu.bme.aut.fox.robotvacuum.virtual.app.App;
import hu.bme.aut.fox.robotvacuum.virtual.app.Simulation;
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

		JButton simulationButton = new JButton("SideBySide");
		simulationButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		simulationButton.addActionListener(
				(event) -> navigate(new SimulationAppScreen())
		);

		JButton virtualWorldButton = new JButton("VirtualWorld");
		virtualWorldButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		virtualWorldButton.addActionListener(
				(event) -> {
					Simulation simulation = new Simulation();
					navigate(new VirtualWorldScreen(
							simulation.getWorld(),
							simulation.getRadar(),
							simulation.getMotor()
					));
					//simulation.start();
				}
		);

		JButton worldButton = new JButton("RobotVacuum's world");
		worldButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		worldButton.addActionListener(
				(event) -> {
					WorldScreen ws = new WorldScreen(new Simulation().getRobotVacuum());
					navigate(ws);
				});


		JButton combinedButton = new JButton("Combined Screen");
		combinedButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		combinedButton.addActionListener(
				(event) -> {
					Simulation simulation = new Simulation();
//					navigate(new CombinedScreen(
//							simulation.getWorld(),
//							simulation.getRadar(),
//							simulation.getMotor(),
//							simulation.getRobotVacuum()
//					));
				}
		);

		add(Box.createVerticalGlue());
		add(clockButton);
		add(simulationButton);
		add(virtualWorldButton);
		add(worldButton);
		add(combinedButton);
		add(Box.createVerticalGlue());
	}
}
