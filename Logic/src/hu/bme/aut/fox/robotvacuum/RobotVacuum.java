package hu.bme.aut.fox.robotvacuum;

import hu.bme.aut.fox.robotvacuum.hardware.Motor;
import hu.bme.aut.fox.robotvacuum.hardware.Radar;
import hu.bme.aut.fox.robotvacuum.interpretation.Interpreter;
import hu.bme.aut.fox.robotvacuum.navigation.Navigator;
import hu.bme.aut.fox.robotvacuum.world.World;

public class RobotVacuum {

	private Radar radar;
	private Motor motor;

	private Interpreter interpreter;
	private Navigator navigator;

	private State state;
	private World world;

	public RobotVacuum(
			Radar radar, Motor motor,
			Interpreter interpreter, Navigator navigator
	) {
		this.radar = radar;
		this.motor = motor;

		this.interpreter = interpreter;
		this.navigator = navigator;

		state = new State();
		world = new World();
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

	private void onRadarUpdate(Radar.RadarData[] data) {
		world = interpreter.interpretRadar(world, state, data);
	}

	private void onMotorMovement(double distance) {
		state = interpreter.interpretMovement(world, state, distance);
	}

	private void onMotorRotation(double angle) {
		state = interpreter.interpretRotation(world, state, angle);
	}

	public static class State {

		private final double positionX;
		private final double positionY;
		private final double direction;

		public State() {
			this(0, 0, 0);
		}

		public State(double positionX, double positionY, double direction) {
			this.positionX = positionX;
			this.positionY = positionY;
			this.direction = direction;
		}

		public double getPositionX() {
			return positionX;
		}

		public double getPositionY() {
			return positionY;
		}

		public double getDirection() {
			return direction;
		}
	}
}
