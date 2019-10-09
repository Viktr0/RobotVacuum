package hu.bme.aut.fox.robotvacuumsimulator.virtualcomponent;

import hu.bme.aut.fox.robotvacuum.hardwareAbstractionLayer.Radar;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.LinkedList;
import java.util.List;

public class VirtualRadar implements Radar,Runnable {
	private static final Object observableLock = new Object();
	private static final int pingInterval = 500;
	private static final double angle = 2.0 / 3.0 * Math.PI;

	private List<RadarListener> listeners;
	private VirtualWorld world;

	public VirtualRadar(VirtualWorld world) {
		this.world = world;
		this.listeners = new LinkedList<>();
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
	public void run() {
		throw new NotImplementedException(); //TODO
	}
}
