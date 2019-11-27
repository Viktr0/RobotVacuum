package hu.bme.aut.fox.robotvacuum;

import hu.bme.aut.fox.robotvacuum.hardware.OldMotor;
import hu.bme.aut.fox.robotvacuum.hardware.OldRadar;
import hu.bme.aut.fox.robotvacuum.interpretation.Interpreter;
import hu.bme.aut.fox.robotvacuum.movement.MovementController;
import hu.bme.aut.fox.robotvacuum.navigation.Navigator;
import hu.bme.aut.fox.robotvacuum.world.World;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class RobotVacuum {

	private final OldRadar oldRadar;
	private final OldMotor oldMotor;

	private final Interpreter interpreter;
	private final Navigator navigator;
	private final MovementController movementController;

	private final Object interpretationLock = new Object();
	private State state;
	private World world;

	private Queue<Navigator.Target> targets = new LinkedList<>();
	private Navigator.Target target;

	public RobotVacuum(
		OldRadar oldRadar, OldMotor oldMotor,
		Interpreter interpreter, Navigator navigator, MovementController movementController
	) {
		this.oldRadar = oldRadar;
		this.oldMotor = oldMotor;

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

	public List<Navigator.Target> getTargets() {
		List<Navigator.Target> targets = new LinkedList<>();
		targets.add(target);
		targets.addAll(this.targets);
		return targets;
	}

	public void start() {
		oldRadar.addOnUpdateListener(this::onRadarUpdate);
		oldMotor.addOnMovementListener(this::onMotorMovement);
		oldMotor.addOnRotationListener(this::onMotorRotation);

		oldRadar.start();
		oldMotor.start();
	}

	public void stop() {
		oldRadar.stop();
		oldMotor.stop();

		oldRadar.removeOnUpdateListener(this::onRadarUpdate);
		oldMotor.removeOnMovementListener(this::onMotorMovement);
		oldMotor.removeOnRotationListener(this::onMotorRotation);
	}

	private void onRadarUpdate(OldRadar.RadarData[] data) {
		synchronized (interpretationLock) {
			Interpreter.Interpretation interpretation = interpreter.interpretRadar(world, state, data);
			setWorld(interpretation.getWorld());
			setState(interpretation.getState());
		}
	}

	private void onMotorMovement(double distance) {
		synchronized (interpretationLock) {
			Interpreter.Interpretation interpretation = interpreter.interpretMovement(world, state, distance);
			setWorld(interpretation.getWorld());
			setState(interpretation.getState());
		}
	}

	private void onMotorRotation(double angle) {
		synchronized (interpretationLock) {
			Interpreter.Interpretation interpretation = interpreter.interpretRotation(world, state, angle);
			setWorld(interpretation.getWorld());
			setState(interpretation.getState());
		}
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
					if (movement.getAngle() != 0) {
						oldMotor.rotate(movement.getAngle());
					} else if (movement.getDistance() != 0) {
						oldMotor.move(movement.getDistance());
					}
					break;
				}
			}

			if (targets.size() == 0) {
				Navigator.Target[] targets = navigator.getTargetPath(world, state);
				if (targets != null) Collections.addAll(this.targets, targets);
			}

			if (targets.size() > 0) {
				target = targets.remove();
			} else {
				oldMotor.rotate(Math.PI);
				break;
			}
		}
	}

	private void onWorldChanged() {

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
