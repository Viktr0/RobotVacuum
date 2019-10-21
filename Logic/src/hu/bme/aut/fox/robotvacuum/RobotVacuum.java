package hu.bme.aut.fox.robotvacuum;

import hu.bme.aut.fox.robotvacuum.components.Discretiser;
import hu.bme.aut.fox.robotvacuum.components.navigation.Navigator;
import hu.bme.aut.fox.robotvacuum.components.WorldInterpreter;
import hu.bme.aut.fox.robotvacuum.components.world.InterpretedWorld;
import hu.bme.aut.fox.robotvacuum.components.world.InterpretedWorldField;
import hu.bme.aut.fox.robotvacuum.hal.Motor;
import hu.bme.aut.fox.robotvacuum.hal.Radar;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

public class RobotVacuum implements Radar.RadarListener, Motor.MotorListener {
	private Radar radar;
	private Motor motor;
	private InterpretedWorldField nextField;
	private Components components;

	public RobotVacuum(Radar radar, Motor motor) {
		this.radar = radar;
		this.motor = motor;
		components = new Components();
		nextField = null;
	}

	public void start() {
		throw new NotImplementedException(); //TODO
	}

	public void stop() {
		this.radar.stop();
		this.motor.stop();
	}

	@Override
	public void motorMovedForward(double deltaS) {
		throw new NotImplementedException(); //TODO
	}

	@Override
	public void motorRotated(double deltaPhi) {
		throw new NotImplementedException(); //TODO
	}

	@Override
	public void newRadarData(List<Radar.RadarData> data) {
		throw new NotImplementedException(); //TODO
	}

	private static class Components {
		private WorldInterpreter worldInterpreter;
		private InterpretedWorld world;
		private Navigator navigator;
		private Discretiser discretiser;

		private Components() {
			worldInterpreter = new WorldInterpreter();
			world = new InterpretedWorld();
			navigator = new Navigator();
			discretiser = new Discretiser();
		}
	}
}
