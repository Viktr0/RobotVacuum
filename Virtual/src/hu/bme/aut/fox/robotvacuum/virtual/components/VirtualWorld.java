package hu.bme.aut.fox.robotvacuum.virtual.components;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class VirtualWorld {
	private static final double PI_2 = Math.PI * 2;
	public static final double WORLD_FIELD_SIZE = 1.0;
	private static final Object positionLock = new Object();
	private static final Object observableLock = new Object();

	private Position robotVacuumPosition;
	private int N;
	private int M;

	private VirtualWorldField[][] worldMatrix;

	private List<VirtualWorldListener> listeners;

	public VirtualWorld(VirtualWorldField[][] fields, int n, int m, Position p) {
		N = n;
		M = m;
		worldMatrix = fields;
		listeners = new LinkedList<>();
		robotVacuumPosition = p;
	}

	public boolean isFieldEmpty(int i, int j) {
		if (i >= N || j >= M || i < 0 || j < 0) throw new IndexOutOfBoundsException();
		return worldMatrix[i][j].status != VirtualWorldField.Status.NOTEMPTY;
	}

	public VirtualWorldField[][] getWorldMatrix() {
		return worldMatrix;
	}

	public Size getSize() {
		return new Size(N, M);
	}

	public void setRobotVacuumPosition(Position position) {
		synchronized (positionLock){
			this.robotVacuumPosition = position;
			this.cleanField();
			this.setProperPositionDirection();
			this.notifyListenersOfPositionChange();
		}
	}

	public Position getRobotVacuumPosition() {
		synchronized (positionLock){
			return new Position(robotVacuumPosition.x, robotVacuumPosition.y, robotVacuumPosition.direction);
		}
	}

	private void setProperPositionDirection() {
		while (this.robotVacuumPosition.direction > PI_2) robotVacuumPosition.direction -= PI_2;
		while (this.robotVacuumPosition.direction < 0) robotVacuumPosition.direction += PI_2;
	}

	private void cleanField() {
		int i = (int) Math.floor(robotVacuumPosition.x);
		int j = (int) Math.floor(robotVacuumPosition.y);
		worldMatrix[i][j].status = VirtualWorldField.Status.CLEAN;
	}

	public void addListener(VirtualWorldListener listener) {
		synchronized (observableLock) {
			listeners.add(listener);
		}
	}

	private void notifyListenersOfPositionChange() {
		synchronized (observableLock) {
			for (VirtualWorldListener listener : listeners)
				listener.positionChanged(getRobotVacuumPosition());
		}
	}


	public interface VirtualWorldListener {
		void positionChanged(Position position);
	}

	public class Size {
		public int N;
		public int M;
		public Size(int n, int m){
			N = n;
			M = m;
		}
	}
}
