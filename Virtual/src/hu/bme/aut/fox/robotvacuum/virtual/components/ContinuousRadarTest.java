package hu.bme.aut.fox.robotvacuum.virtual.components;

import hu.bme.aut.fox.robotvacuum.virtual.app.Simulation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ContinuousRadarTest {
	@Test
	void testIntersectLines() {
		Simulation simulation = new Simulation();
		ContinuousRadar radar = simulation.getRadar();
		Vec2 point = radar.intersectWithLine(new Vec2(1, 2), new Vec2(3.1, 6.2), new Vec2(0, 3), Math.PI / 4);
		assertEquals( 3, (int)point.getX());
		assertEquals( 6, (int)point.getY());
		assertTrue(Math.abs(3 - point.getX()) < 0.00001);
		assertTrue(Math.abs(6 - point.getY()) < 0.00001);
	}

	void testRadarSpeed() {
		Simulation simulation = new Simulation();
		ContinuousRadar radar = simulation.getRadar();
		long t1 = System.currentTimeMillis();
		radar.getRadarData();
	}
}