package hu.bme.aut.fox.robotvacuumsimulator.virtualcomponent;

import hu.bme.aut.fox.robotvacuum.Position;
import hu.bme.aut.fox.robotvacuum.hardwareAbstractionLayer.Motor;

import java.util.LinkedList;
import java.util.List;

public class VirtualMotor implements Motor {
	private static final Object controlLock = new Object();
	private static final Object observableLock = new Object();
	private static final int movingInterval = 300;
	private static final double movingSpeed = 0.2;
	private static final int rotationInterval = 300;
	private static final double rotationSpeed = 0.2;

	private boolean isRunning = false;
	private Thread motorThread;

	private List<MotorListener> listeners;
	private VirtualWorld world;

	private boolean rotating = false;
	private double remainingRotation = 0;

	private boolean moving = false;
	private double remainingTranslation = 0;

	public VirtualMotor(VirtualWorld world) {
		this.world = world;
		this.listeners = new LinkedList<>();
		motorThread = null;
	}

	private void notifyMotorListenersOfMove(final double deltaS) {
		synchronized (observableLock) {
			for(MotorListener listener : listeners)
				listener.motorMovedForward(deltaS);
		}
	}

	private void notifyMotorListenersOfRotation(final double deltaPhi) {
		synchronized (observableLock) {
			for(MotorListener listener : listeners)
				listener.motorRotated(deltaPhi);
		}
	}

	@Override
	public void addMotorListener(MotorListener listener) {
		synchronized (observableLock) {
			this.listeners.add(listener);
		}
	}

	@Override
	public void rotate(double deltaPhi) {
		synchronized (controlLock) {
			rotating = true;
			moving = false;
			remainingRotation = deltaPhi;
			remainingTranslation = 0;
			controlLock.notify();
		}
	}

	@Override
	public void moveForward(double deltaS) {
		synchronized (controlLock) {
			moving = true;
			rotating = false;
			remainingTranslation = deltaS;
			remainingRotation = 0;
			controlLock.notify();
		}
	}

	@Override
	public void start() {
		if (isRunning) return;
		isRunning = true;
		motorThread = new Thread(this::run);
		motorThread.start();
	}

	@Override
	public void stop() {
		isRunning = false;
		try {
			motorThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void run() {
		while (isRunning)
			try {
				long sleep = 0;
				synchronized (controlLock) {
					while (!rotating && !moving) controlLock.wait();
					if (rotating) {
						double deltaPhi = performRotation();
						notifyMotorListenersOfRotation(deltaPhi);
						sleep = movingInterval;
					} else if (moving) {
						double deltaS = performTranslation();
						notifyMotorListenersOfMove(deltaS);
						sleep = movingInterval;
					}
				}
				Thread.sleep(sleep);
			}
			catch(InterruptedException e) {
				e.printStackTrace();
			}
	}
	private double performTranslation() {
		Position p = world.getRobotVacuumPosition();
		double translationX, translationY, deltaS;
		if (movingSpeed < remainingTranslation) {
			deltaS = movingSpeed;
			remainingTranslation -= movingSpeed;
		}
		else {
			deltaS = remainingTranslation;
			remainingTranslation = 0;
			moving = false;
		}
		translationX = Math.cos(p.direction) * deltaS;
		translationY = -Math.sin(p.direction) * deltaS;
		p.x += translationX;
		p.y += translationY;
		world.setRobotVacuumPosition(p);
		return deltaS;
	}

	private double performRotation() {
		Position p = world.getRobotVacuumPosition();
		double deltaPhi = 0;
		if (Math.abs(remainingRotation) < rotationSpeed) {
			p.direction += remainingRotation;
			deltaPhi = remainingRotation;
			remainingRotation = 0;
			rotating = false;
		} else {
			deltaPhi = (remainingRotation < 0 ? -1 : 1) * rotationSpeed;
			p.direction += deltaPhi;
			remainingRotation -= deltaPhi;
		}
		world.setRobotVacuumPosition(p);
		return deltaPhi;
	}
}
