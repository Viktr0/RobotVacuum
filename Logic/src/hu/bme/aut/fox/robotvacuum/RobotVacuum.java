package hu.bme.aut.fox.robotvacuum;

import hu.bme.aut.fox.robotvacuum.hardware.Motor;
import hu.bme.aut.fox.robotvacuum.hardware.Radar;
import hu.bme.aut.fox.robotvacuum.interpretation.Interpreter;
import hu.bme.aut.fox.robotvacuum.movement.MovementController;
import hu.bme.aut.fox.robotvacuum.navigation.Navigator;
import hu.bme.aut.fox.robotvacuum.world.World;

import java.util.*;

public class RobotVacuum {

	private final double size;

	private final Radar radar;
	private final Motor motor;

	private final Interpreter interpreter;
	private final Navigator navigator;
	private final MovementController movementController;

	private boolean running = false;
	private Thread thread = null;

	private State state;
	private World world;

	private Queue<Navigator.Target> targets = new LinkedList<>();

	public RobotVacuum(
			double size,
			Radar radar, Motor motor,
			Interpreter interpreter, Navigator navigator, MovementController movementController
	) {
		this.size = size;

		this.radar = radar;
		this.motor = motor;

		this.interpreter = interpreter;
		this.navigator = navigator;
		this.movementController = movementController;

		state = new State();
		world = new World(0.2);
	}

	public State getState() {
		return state;
	}

	public World getWorld() {
		return world;
	}

	public List<Navigator.Target> getTargets() {
		return new ArrayList<>(this.targets);
	}

	public void start() {
		if (running) throw new IllegalStateException("The vacuum has already been started");
		running = true;

		radar.start();
		motor.start();

		thread = new Thread(() -> {
			while (running) loop();
		});
		thread.start();
	}

	public void stop() {
		if (!running) throw new IllegalStateException("The vacuum hasn't been started");
		running = false;

		try {
			thread.join();
		} catch (InterruptedException ignored) {

		}

		radar.stop();
		motor.stop();
	}

	private void loop() {
		Interpreter.Interpretation interpretation;
		interpretation = interpreter.interpretRadar(size, world, state, radar.getRadarData());
		world = interpretation.getWorld();
		state = interpretation.getState();

		MovementController.Movement movement = getMovement();
		double rotation = motor.rotate(movement.getAngle());
		double distance = motor.move(movement.getDistance());

		interpretation = interpreter.interpretRotation(size, world, state, rotation);
		world = interpretation.getWorld();
		state = interpretation.getState();

		interpretation = interpreter.interpretMovement(size, world, state, distance);
		world = interpretation.getWorld();
		state = interpretation.getState();
	}

	private MovementController.Movement getMovement() {
		Navigator.Target target;
		MovementController.Movement movement;

		do {
			target = getTarget();
			if (target == null) {
				movement = new MovementController.Movement(0, Math.PI);
			} else {
				movement = movementController.getNextMovement(
						state,
						target.getX(),
						target.getY()
				);
				if (movement == null) nextTarget();
			}
		} while (movement == null);

		return movement;
	}

	private Navigator.Target getTarget() {
		if (targets.size() == 0) {
			Navigator.Target[] targets = navigator.getTargetPath(size, world, state);
			if (targets != null) Collections.addAll(this.targets, targets);
		}

		if (targets.size() > 0) {
			return targets.peek();
		} else {
			return null;
		}
	}

	private void nextTarget() {
		targets.remove();
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
