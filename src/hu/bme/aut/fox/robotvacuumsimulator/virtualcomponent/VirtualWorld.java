package hu.bme.aut.fox.robotvacuumsimulator.virtualcomponent;

import hu.bme.aut.fox.robotvacuum.Position;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class VirtualWorld {
	private static final Object positionLock = new Object();
	private static final Object observableLock = new Object();

	private Position robotVacuumPosition;

	private List<List<VirtualWorldField>> worldMatrix;

	private List<VirtualWorldListener> listeners;

	public VirtualWorld(List<List<VirtualWorldField>> fields) {
		worldMatrix = Collections.unmodifiableList(fields);
		listeners = new LinkedList<>();
	}

	public List<List<VirtualWorldField>> getWorldMatrix() {
		return worldMatrix;
	}

	public void setRobotVacuumPosition(Position position) {
		synchronized (positionLock){
			this.robotVacuumPosition = position;
			this.notifyListenersOfPositionChange();
		}
	}

	public Position getRobotVacuumPosition() {
		synchronized (positionLock){
			return new Position(robotVacuumPosition.x, robotVacuumPosition.y, robotVacuumPosition.direction);
		}
	}

	public void addListener(VirtualWorldListener listener) {
		synchronized (observableLock) {
			listeners.add(listener);
		}
	}

	private void notifyListenersOfPositionChange() {
		synchronized (observableLock) {
			for (VirtualWorldListener listener : listeners)
				listener.notifyPositionChange(getRobotVacuumPosition());
		}
	}


	public interface VirtualWorldListener {
		void notifyPositionChange(Position position);
	}
}
