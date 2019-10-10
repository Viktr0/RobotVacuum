package hu.bme.aut.fox.robotvacuum;

import hu.bme.aut.fox.robotvacuum.components.Discretiser;
import hu.bme.aut.fox.robotvacuum.components.Navigator;
import hu.bme.aut.fox.robotvacuum.components.WorldInterpreter;
import hu.bme.aut.fox.robotvacuum.components.interpretedworld.InterpretedWorld;
import hu.bme.aut.fox.robotvacuum.components.interpretedworld.InterpretedWorldField;
import hu.bme.aut.fox.robotvacuum.hardwareAbstractionLayer.Motor;
import hu.bme.aut.fox.robotvacuum.hardwareAbstractionLayer.Radar;
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
