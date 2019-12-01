package hu.bme.aut.fox.robotvacuum.virtual.components;

import hu.bme.aut.fox.robotvacuum.hardware.Motor;

public class ContinuousMotor implements Motor {
	private static final double speed = 1.0;
	private static final double rotationSpeed = 0.1;
	private boolean isRunning;
	ContinuousWorld world;

	public ContinuousMotor(ContinuousWorld world) {
		this.world = world;
	}

	@Override
	public double move(double distance) {
		final Position prevPosition = world.getRobotVacuum();
		final double time = distance / speed;
		sleep(time);
		final double dx = Math.cos(prevPosition.direction) * distance;
		final double dy = Math.sin(prevPosition.direction) * distance;
		world.setRobotVacuum(new Position(prevPosition.x + dx, prevPosition.y + dy, prevPosition.direction));
		return distance;
	}

	@Override
	public double rotate(double angle) {
		final Position prevPosition = world.getRobotVacuum();
		final double time = angle / rotationSpeed;
		sleep(time);
		world.setRobotVacuum(new Position(prevPosition.x, prevPosition.y, prevPosition.direction + angle));
		return angle;
	}

	private static void sleep(double d) {
		try {
			Thread.sleep((long)(d * 1000));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}


	@Override
	public void start() {
		isRunning = true;
	}

	@Override
	public void stop() {
		isRunning = false;
	}
}
