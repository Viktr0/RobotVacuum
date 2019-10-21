package hu.bme.aut.fox.robotvacuum;

import hu.bme.aut.fox.robotvacuum.components.Discretiser;
import hu.bme.aut.fox.robotvacuum.components.navigation.Navigator;
import hu.bme.aut.fox.robotvacuum.components.WorldInterpreter;
import hu.bme.aut.fox.robotvacuum.components.world.InterpretedWorld;
import hu.bme.aut.fox.robotvacuum.components.world.InterpretedWorldField;
import hu.bme.aut.fox.robotvacuum.hardware.Motor;
import hu.bme.aut.fox.robotvacuum.hardware.Radar;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

public class RobotVacuum {

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
		radar.addOnUpdateListener(this::onRadarUpdate);
		motor.addOnMovementListener(this::onMotorMovement);
		motor.addOnRotationListener(this::onMotorRotation);

		radar.start();
		motor.start();
	}

	public void stop() {
		radar.stop();
		motor.stop();

		radar.removeOnUpdateListener(this::onRadarUpdate);
		motor.removeOnMovementListener(this::onMotorMovement);
		motor.removeOnRotationListener(this::onMotorRotation);
	}

	private void onRadarUpdate(List<Radar.RadarData> data) {
		throw new NotImplementedException(); //TODO
	}

	private void onMotorMovement(double distance) {
		throw new NotImplementedException(); //TODO
	}

	private void onMotorRotation(double angle) {
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
