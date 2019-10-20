package hu.bme.aut.fox.robotvacuum.virtual.components;

import hu.bme.aut.fox.robotvacuum.hal.Radar;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.LinkedList;
import java.util.List;

public class VirtualRadar implements Radar {
	private static final Object observableLock = new Object();
	private static final int pingInterval = 500;
	private static final double angle = 2.0 / 3.0 * Math.PI;

	private List<RadarListener> listeners;
	private VirtualWorld world;
	private boolean isRunning = false;
	private Thread radarThread;

	public VirtualRadar(VirtualWorld world) {
		this.world = world;
		this.listeners = new LinkedList<>();
		radarThread = null;
	}

	private void notifyRadarListeners(List<RadarData> data) {
		synchronized (observableLock) {
			for(RadarListener listener : listeners)
				listener.newRadarData(data);
		}
	}
	@Override
	public void addRadarListener(RadarListener listener) {
		synchronized (observableLock) {
			this.listeners.add(listener);
		}
	}

	@Override
	public void start() {
		if (isRunning) return;
		isRunning = true;
		radarThread = new Thread(this::run);
		radarThread.start();
	}

	@Override
	public void stop() {
		isRunning = false;
		try {
			radarThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void run() {
		throw new NotImplementedException(); //TODO
	}
}
