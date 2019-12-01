package hu.bme.aut.fox.robotvacuum.virtual.components;

import hu.bme.aut.fox.robotvacuum.hardware.Radar;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ContinuousRadar implements Radar {
	private static final double angle = 2.0 / 3.0 * Math.PI;
	private static final double dPhi = 0.15;
	private static final double maxLength = 5.0;

	private boolean isRunning;
	ContinuousWorld world;

	public ContinuousRadar(ContinuousWorld world) {
		this.world = world;
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

		return data.toArray(new RadarData[0]);
	}

	private RadarData measure(ContinuousWorld.WorldObject[] objects, Position position, double phi) {
		List<RadarData> data = new LinkedList<>();
		for (ContinuousWorld.WorldObject object : objects) {
			RadarData intersection = intersectWithObject(object, position, phi);
			if (intersection != null) {
				if (intersection.getDistance() > maxLength)
					data.add(new RadarData(phi, maxLength, false));
				else
					data.add(intersection);
			}
		}
		return Collections.min(data, (a, b) -> (int) ((a.getDistance() - b.getDistance()) * 100));
	}

	private RadarData intersectWithObject(ContinuousWorld.WorldObject object, Position p, double phi) {
		Vec2 position = new Vec2(p.x, p.y);
		List<Double> intersections = new LinkedList<>();

		ContinuousWorld.Coordinate[] vertices = object.getVertices();
		final int length = vertices.length;
		for (int i = 0, j = 1; i < length; ++i, j = j + 1 % length) {
			Vec2 intersection = intersectWithLine(vertices[i], vertices[j], position, phi);
			if (intersection != null)
				intersections.add(position.minus(intersection).getLength());
		}

		if (intersections.size() == 0)
			return null;

		double min = Collections.min(intersections, (a, b) -> (int) (a - b));

		return new RadarData(min, phi, true);
	}

	private Vec2 intersectWithLine(Vec2 a, Vec2 b, Vec2 from, double phi) {
		Vec2 dir = a.minus(b).normalize();
		Vec2 rayDir = new Vec2(1, Math.tan(phi)).normalize();

		double d = a.minus(from).scalarProduct(dir);

		Vec2 dist = from.minus(a.plus(dir.scale(d)));
		double dist2 = dist.getLengthSquared();

		double e = dist2 / dist.scalarProduct(dir);
		Vec2 intersection = from.plus(rayDir.scale(e));

		double segmentLength2 = a.minus(b).getLengthSquared();
		if (a.minus(intersection).getLengthSquared() < segmentLength2 &&
			b.minus(intersection).getLengthSquared() < segmentLength2)
			return intersection;
		return null;

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
