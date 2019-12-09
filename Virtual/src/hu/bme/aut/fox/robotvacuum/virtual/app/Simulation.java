package hu.bme.aut.fox.robotvacuum.virtual.app;

import hu.bme.aut.fox.robotvacuum.RobotVacuum;
import hu.bme.aut.fox.robotvacuum.interpretation.SimpleInterpreter;
import hu.bme.aut.fox.robotvacuum.movement.SimpleMovementController;
import hu.bme.aut.fox.robotvacuum.navigation.smart.SmartNavigator;
import hu.bme.aut.fox.robotvacuum.virtual.components.*;

import java.io.FileNotFoundException;

public class Simulation {

	private ContinuousWorld world;
	private ContinuousRadar radar;
	private ContinuousMotor motor;
	private RobotVacuum vacuum;

	public Simulation() {
		try {
			world = new ContinuousWorldLoader("world2").load();
			vacuum = new RobotVacuum(
				1.0,
				radar = new ContinuousRadar(world),
				motor = new ContinuousMotor(world),
				new SimpleInterpreter(),
				new SmartNavigator(),
				new SimpleMovementController()
			);
			motor.setSize(1.0);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	public void start() {
		vacuum.start();
	}

	public RobotVacuum getRobotVacuum() { return vacuum; }

	public ContinuousWorld getWorld() {
		return world;
	}

	public ContinuousRadar getRadar() {
		return radar;
	}

	public ContinuousMotor getMotor() {
		return motor;
	}
}
