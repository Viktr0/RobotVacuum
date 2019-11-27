package hu.bme.aut.fox.robotvacuum.virtual.app;

import hu.bme.aut.fox.robotvacuum.RobotVacuum;
import hu.bme.aut.fox.robotvacuum.interpretation.SimpleInterpreter;
import hu.bme.aut.fox.robotvacuum.movement.SimpleMovementController;
import hu.bme.aut.fox.robotvacuum.navigation.SimpleNavigator;
import hu.bme.aut.fox.robotvacuum.virtual.components.*;

import java.io.FileNotFoundException;

public class Simulation {

	private VirtualWorld world;
	private VirtualRadar radar;
	private VirtualMotor motor;
	private RobotVacuum vacuum;

	public Simulation() {
		try {
			world = new VirtualWorldLoader("world1").load();
			vacuum = new RobotVacuum(
					radar = new VirtualRadar(world),
					motor = new VirtualMotor(world),
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

	public VirtualWorld getWorld() {
		return world;
	}

	public VirtualRadar getRadar() {
		return radar;
	}

	public VirtualMotor getMotor() {
		return motor;
	}
}
