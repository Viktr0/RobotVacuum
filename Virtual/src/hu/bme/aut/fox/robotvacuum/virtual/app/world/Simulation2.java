package hu.bme.aut.fox.robotvacuum.virtual.app.world;

import hu.bme.aut.fox.robotvacuum.RobotVacuum;
import hu.bme.aut.fox.robotvacuum.interpretation.SimpleInterpreter;
import hu.bme.aut.fox.robotvacuum.movement.SimpleMovementController;
import hu.bme.aut.fox.robotvacuum.navigation.SimpleNavigator;
import hu.bme.aut.fox.robotvacuum.virtual.components.*;

import java.io.FileNotFoundException;

public class Simulation2 {

	private ContinuousWorld world;
	private ContinuousRadar radar;
	private ContinuousMotor motor;
	private RobotVacuum vacuum;

	public Simulation2() {
		try {
			world = new ContinuousWorldLoader("world2").load();
			vacuum = new RobotVacuum(
				1.0,
				radar = new ContinuousRadar(world),
				motor = new ContinuousMotor(world),
				new SimpleInterpreter(),
				new SimpleNavigator(),
				new SimpleMovementController()
			);
			start();
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