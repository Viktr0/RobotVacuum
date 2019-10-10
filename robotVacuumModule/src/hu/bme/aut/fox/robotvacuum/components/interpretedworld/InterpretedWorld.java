package hu.bme.aut.fox.robotvacuum.components.interpretedworld;

import hu.bme.aut.fox.robotvacuum.Position;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class InterpretedWorld {
	public static final double WORLD_FIELD_SIZE = 1.0;

	private static final double PI_2 = Math.PI * 2;

	private static final Object matrixLock = new Object();
	private static final Object positionLock = new Object();
	private static final Object observableLock = new Object();

	private List<List<InterpretedWorldField>> worldMatrix;
	private InterpretedWorldField[][] worldMatrixDeepCopy;
	private int N, M;

	private Position robotVacuumPosition;

	private List<InterpretedWorldListener> listeners;


	public InterpretedWorld() {
		N = 0;
		M = 0;
		worldMatrix = new ArrayList<>(N);
		listeners = new LinkedList<>();
		robotVacuumPosition = new Position(0, 0, 0);
	}

	public InterpretedWorldField[][] getFields() {
		synchronized (matrixLock) {
			return worldMatrixDeepCopy;
		}
	}

	public void cleanField(final short i, final short j) {
		synchronized (matrixLock) {
			if (i >= N || j >= M) throw new IndexOutOfBoundsException();
			this.worldMatrix.get(i).get(j).clean();
			this.worldMatrixDeepCopy[i][j].clean();
			this.notifyListenersOfMatrixChange();
		}
	}

	public void setNewMatrix(List<List<InterpretedWorldField>> newMatrix, int n, int m) {
		synchronized (matrixLock) {
			this.worldMatrix = newMatrix;
			N = n;
			M = m;
			this.generateMatrixDeepCopy();
			this.notifyListenersOfMatrixChange();
		}
	}

	private void generateMatrixDeepCopy() {
		worldMatrixDeepCopy = new InterpretedWorldField[N][M];
		for (int i = 0; i < N; ++i)
			for (int j = 0; j < M; ++j){
				InterpretedWorldField field = worldMatrix.get(i).get(j);
				worldMatrixDeepCopy[i][j] = new InterpretedWorldField(field.getStatus(), field.getCleaned(), field.getFound());
			}
	}


	public Position getRobotVacuumPosition() {
		synchronized (positionLock){
			return new Position(robotVacuumPosition.x, robotVacuumPosition.y, robotVacuumPosition.direction);
		}
	}

	public void setRobotVacuumPosition(Position robotVacuumPosition) {
		synchronized (positionLock) {
			this.robotVacuumPosition = robotVacuumPosition;
			setProperPositionDirection();
			this.notifyListenersOfPositionChange();
		}
	}

	public void addListener(InterpretedWorldListener listener) {
		synchronized (observableLock) {
			if (listener == null) throw new NullPointerException();
			this.listeners.add(listener);
		}
	}

	private void setProperPositionDirection() {
		System.out.println(this.robotVacuumPosition.direction);
		if (this.robotVacuumPosition.direction > PI_2)
			while (this.robotVacuumPosition.direction > PI_2)
				robotVacuumPosition.direction -= PI_2;

		if (this.robotVacuumPosition.direction < 0)
			while (this.robotVacuumPosition.direction < 0)
				robotVacuumPosition.direction += PI_2;
	}

	private void notifyListenersOfMatrixChange() {
		synchronized (observableLock) {
			for (InterpretedWorldListener listener : listeners)
				listener.notifyWorldMatrixChange(worldMatrixDeepCopy, N, M);
		}
	}

	private void notifyListenersOfPositionChange() {
		synchronized (observableLock) {
			for (InterpretedWorldListener listener : listeners)
				listener.notifyPositionChange(getRobotVacuumPosition());
		}
	}

	public interface InterpretedWorldListener {
		void notifyWorldMatrixChange(InterpretedWorldField[][] matrix, int N, int M);
		void notifyPositionChange(Position position);
	}
}
