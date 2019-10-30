package hu.bme.aut.fox.robotvacuum.virtual.app;

import hu.bme.aut.fox.robotvacuum.RobotVacuum;
import hu.bme.aut.fox.robotvacuum.virtual.components.*;

import java.io.FileNotFoundException;

public class Simulation {
	private VirtualWorld world;
	private RobotVacuum vacuum;

	public Simulation() {
		try {
			world = new WorldLoader("world1").load();
			vacuum = new RobotVacuum(new VirtualRadar(world), new VirtualMotor(world));
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
}
