package hu.bme.aut.fox.robotvacuumsimulator.virtualcomponent;

import hu.bme.aut.fox.robotvacuum.Position;
import hu.bme.aut.fox.robotvacuum.hardwareAbstractionLayer.Motor;

import java.util.LinkedList;
import java.util.List;

public class VirtualMotor implements Motor, Runnable {
	private static final Object observableLock = new Object();
	private static final int movingInterval = 300;
	private static final double movingSpeed = 0.2;
	private static final int rotationInterval = 300;
	private static final double rotationSpeed = 0.2;

	private List<MotorListener> listeners;
	private VirtualWorld world;

	private boolean rotating = false;
	private double remainingRotation = 0;

	public VirtualMotor(VirtualWorld world) {
		this.world = world;
		this.listeners = new LinkedList<>();
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
		rotating = true;
		remainingRotation = deltaPhi;
	}

	@Override
	public void run() {
		while (true) {
			try {
				if (rotating) {
					double deltaPhi = performRotation();
					notifyMotorListenersOfRotation(deltaPhi);
					Thread.sleep(rotationInterval);
				}
				else {
					performTranslation();
					notifyMotorListenersOfMove(movingSpeed);
					Thread.sleep(movingInterval);
				}
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void performTranslation() {
		Position p = world.getRobotVacuumPosition();
		double translationX, translationY;
		translationX = Math.cos(p.direction) * movingSpeed;
		translationY = -Math.sin(p.direction) * movingSpeed;
		p.x += translationX;
		p.y += translationY;
		world.setRobotVacuumPosition(p);
	}

	private double performRotation() {
		Position p = world.getRobotVacuumPosition();
		double deltaPhi = 0;
		if (Math.abs(remainingRotation) < rotationSpeed){
			p.direction += remainingRotation;
			deltaPhi = remainingRotation;
			remainingRotation = 0;
			rotating = false;
		}
		else {
			deltaPhi = (remainingRotation < 0 ? -1 : 1) * rotationSpeed;
			p.direction += deltaPhi;
			remainingRotation -= deltaPhi;
		}
		world.setRobotVacuumPosition(p);
		return deltaPhi;
	}
}
