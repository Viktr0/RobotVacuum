package hu.bme.aut.fox.robotvacuum;

import hu.bme.aut.fox.robotvacuum.hardware.Motor;
import hu.bme.aut.fox.robotvacuum.hardware.Radar;
import hu.bme.aut.fox.robotvacuum.interpretation.Interpreter;
import hu.bme.aut.fox.robotvacuum.movement.MovementController;
import hu.bme.aut.fox.robotvacuum.navigation.Navigator;
import hu.bme.aut.fox.robotvacuum.world.Field;
import hu.bme.aut.fox.robotvacuum.world.World;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;

public class RobotVacuum {

	private final Radar radar;
	private final Motor motor;

	private final Interpreter interpreter;
	private final Navigator navigator;
	private final MovementController movementController;

	private State state;
	private World world;

	private Queue<Navigator.Target> targets = new LinkedList<>();
	private Navigator.Target target;

	public RobotVacuum(
			Radar radar, Motor motor,
			Interpreter interpreter, Navigator navigator, MovementController movementController
	) {
		this.radar = radar;
		this.motor = motor;

		this.interpreter = interpreter;
		this.navigator = navigator;
		this.movementController = movementController;

		state = new State();
		world = new World();

		onStateChanged();
		onWorldChanged();
	}

	public State getState() {
		return state;
	}

	private void setState(State state) {
		State prevState = this.state;
		this.state = state;

		if (this.state != prevState) {
			onStateChanged();
		}
	}

	public World getWorld(){
		return world;
	}

	private void setWorld(World world) {
		World prevWorld = this.world;
		this.world = world;

		if (this.world != prevWorld) {
			onWorldChanged();
		}
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
		Interpreter.Interpretation interpretation = interpreter.interpretRadar(world, state, data);
		setWorld(interpretation.getWorld());
		setState(interpretation.getState());
	}

	private void onMotorMovement(double distance) {
		Interpreter.Interpretation interpretation = interpreter.interpretMovement(world, state, distance);
		setWorld(interpretation.getWorld());
		setState(interpretation.getState());
	}

	private void onMotorRotation(double angle) {
		Interpreter.Interpretation interpretation = interpreter.interpretRotation(world, state, angle);
		setWorld(interpretation.getWorld());
		setState(interpretation.getState());
	}

	private void onStateChanged() {
		while (true) {
			if (target != null) {
				MovementController.Movement movement = movementController.getNextMovement(
						state,
						target.getX(),
						target.getY()
				);

				if (movement != null) {
					if (movement.getDistance() != 0) motor.move(movement.getDistance());
					if (movement.getAngle() != 0) motor.rotate(movement.getAngle());
					break;
				}
			}

			if (targets.size() == 0) {
				Collections.addAll(
						targets,
						navigator.getTargetPath(world, state)
				);
			}

			if (targets.size() > 0) {
				target = targets.remove();
			} else {
				break;
			}
		}
	}

	private void onWorldChanged() {
		// TODO: Remove logging
		for (int y = -5; y <= 5; y++) {
			for (int x = -5; x <= 5; x++) {
				Field field = world.getGridField(x, y);
				System.out.print(field == null ? "  " : field.isObstacle() ? "##" : "||");
			}
			System.out.println();
		}
		System.out.println();
		System.out.println();
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
