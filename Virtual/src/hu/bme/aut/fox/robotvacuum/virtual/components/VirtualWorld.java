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

	private List<List<VirtualWorldField>> worldMatrix;

	private List<VirtualWorldListener> listeners;

	public VirtualWorld(List<List<VirtualWorldField>> fields, int n, int m) {
		N = n;
		M = m;
		worldMatrix = Collections.unmodifiableList(fields);
		listeners = new LinkedList<>();
		robotVacuumPosition = new Position(0, 0, 0);
	}

	public boolean isFieldEmpty(int i, int j) {
		if (i >= N || j >= M || i < 0 || j < 0) throw new IndexOutOfBoundsException();
		return worldMatrix.get(i).get(j).status != VirtualWorldField.Status.NOTEMPTY;
	}

	public List<List<VirtualWorldField>> getWorldMatrix() {
		return worldMatrix;
	}

	public Size getSize() {
		return new Size(N, M);
	}

	public void setRobotVacuumPosition(Position position) {
		synchronized (positionLock){
			this.robotVacuumPosition = position;
			this.notifyListenersOfPositionChange();
			this.setProperPositionDirection();
		}
	}

	public Position getRobotVacuumPosition() {
		synchronized (positionLock){
			return new Position(robotVacuumPosition.x, robotVacuumPosition.y, robotVacuumPosition.direction);
		}
	}

	private void setProperPositionDirection() {
		if (this.robotVacuumPosition.direction > PI_2)
			while (this.robotVacuumPosition.direction > PI_2)
				robotVacuumPosition.direction -= PI_2;

		if (this.robotVacuumPosition.direction < 0)
			while (this.robotVacuumPosition.direction < 0)
				robotVacuumPosition.direction += PI_2;
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
