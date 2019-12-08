package hu.bme.aut.fox.robotvacuum.virtual.components;

import hu.bme.aut.fox.robotvacuum.hardware.Radar;

import java.util.*;

public class ContinuousRadar implements Radar {
	private static final double angle = 2.0 / 3.0 * Math.PI;
	private static final double dPhi = 0.15;
	private static final double maxLength = 5.0;

	private final Set<RadarListener> listeners;

	private boolean isRunning;
	ContinuousWorld world;

	public ContinuousRadar(ContinuousWorld world) {
		this.world = world;
		listeners = new HashSet<>();
	}

	@Override
	public RadarData[] getRadarData() {
		if (!isRunning)
			return null;
		ContinuousWorld.WorldObject[] objects = world.getObjects();
		Position position = world.getRobotVacuum();

		List<RadarData> data = new LinkedList<>();
		for (double phi = position.direction - angle / 2; phi < position.direction + angle / 2; phi += dPhi)
			data.add(measure(objects, position, phi));

		notifyListeners(data);
		return data.toArray(new RadarData[0]);
	}

	public void addRadarListener(RadarListener listener) {
		listeners.add(listener);
	}

	public void removeRadarListener(RadarListener listener) {
		listeners.remove(listener);
	}

	RadarData measure(ContinuousWorld.WorldObject[] objects, Position position, double phi) {
		List<RadarData> data = new LinkedList<>();
		for (ContinuousWorld.WorldObject object : objects) {
			Double distance = getObjectsDistance(object, position, phi);
			if (distance != null) {
				if (distance > maxLength)
					data.add(new RadarData(phi, maxLength, false));
				else
					data.add(new RadarData(phi, distance, true));
			}
		}
		return Collections.min(data, Comparator.comparingDouble(RadarData::getDistance));
	}

	static Double getObjectsDistance(ContinuousWorld.WorldObject object, Position p, double phi) {
		Vec2 position = new Vec2(p.x, p.y);
		List<Double> intersections = new LinkedList<>();

		ContinuousWorld.Coordinate[] vertices = object.getVertices();
		final int length = vertices.length;
		for (int i = 0, j = 1; i < length; ++i, j = (j + 1) % length) {
			Vec2 intersection = intersectWithLine(vertices[i], vertices[j], position, phi);
			if (intersection != null)
				intersections.add(position.minus(intersection).getLength());
		}

		if (intersections.size() == 0)
			return null;

		double min = Collections.min(intersections, Double::compare);

		return min;
	}

	static Vec2 intersectWithLine(Vec2 a, Vec2 b, Vec2 from, double phi) {
		Vec2 dir = b.minus(a).normalize();
		Vec2 rayDir = new Vec2(1, Math.tan(phi)).normalize();

		double d = dir.scalarProduct(from.minus(a));

		Vec2 dist = a.plus(dir.scale(d)).minus(from);
		double dist2 = dist.getLengthSquared();

		double e = dist2 / dist.scalarProduct(rayDir);

		Vec2 intersection = from.plus(rayDir.scale(e));

		double segmentLength2 = a.minus(b).getLengthSquared();
		if (a.minus(intersection).getLengthSquared() < segmentLength2 &&
			b.minus(intersection).getLengthSquared() < segmentLength2)
			return intersection;
		return null;

	}

	private void notifyListeners(List<RadarData> data) {
		for (RadarListener listener : listeners)
			listener.notifyNewData(data.toArray(new RadarData[0]));
	}

	@Override
	public void start() {
		isRunning = true;
	}

	@Override
	public void stop() {
		isRunning = false;
	}

	public interface RadarListener {
		void notifyNewData(RadarData[] data);
	}

}
