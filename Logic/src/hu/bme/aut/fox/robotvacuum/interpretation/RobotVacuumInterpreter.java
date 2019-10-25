package hu.bme.aut.fox.robotvacuum.interpretation;

import hu.bme.aut.fox.robotvacuum.RobotVacuum;
import hu.bme.aut.fox.robotvacuum.hardware.Radar;
import hu.bme.aut.fox.robotvacuum.world.Field;
import hu.bme.aut.fox.robotvacuum.world.World;

import java.util.LinkedList;
import java.util.List;

public class RobotVacuumInterpreter implements Interpreter {
	@Override
	public World interpretRadar(World currentWorld, RobotVacuum.State currentState, Radar.RadarData[] radarData) {
		List<Field> newFields = new LinkedList<>();
		for (Radar.RadarData data : radarData)
			interpretRadarRay(currentWorld, currentState, data, newFields);
		return new World(currentWorld, newFields.toArray(new Field[0]));
	}

	@Override
	public RobotVacuum.State interpretMovement(World currentWorld, RobotVacuum.State currentState, double movementDistance) {
		return null;
	}

	@Override
	public RobotVacuum.State interpretRotation(World currentWorld, RobotVacuum.State currentState, double rotationAngle) {
		return null;
	}

	private void interpretRadarRay(
		World currentWorld,
		RobotVacuum.State currentState,
		Radar.RadarData ray,
		List<Field> newFields) {

		final double gridScale = currentWorld.getGridScale();
		final double ds = gridScale / 8.0;
		final double dx = Math.cos(currentState.getDirection() + ray.getDirection()) * ds;
		final double dy = Math.sin(currentState.getDirection() + ray.getDirection()) * ds;
		final double distance = ray.getDistance();
		double x = currentState.getPositionX();
		double y = currentState.getPositionY();
		for (double s = 0; s < distance; s += ds, x += dx, y += dy){
			Field field = currentWorld.getFieldAt(x, y);
			if (field != null)
				newFields.add(new Field(field.getX(), field.getY(), false, field.isCleaned()));
			else newFields.add(new Field((int) Math.floor(x / gridScale), (int) Math.floor(y / gridScale), false, false));
		}

		if (ray.isObstacle()) {
			final double endX = Math.cos(currentState.getDirection()) * distance + currentState.getPositionX();
			final double endY = Math.sin(currentState.getDirection()) * distance + currentState.getPositionY();
			Field obstacle = currentWorld.getFieldAt(endX + gridScale / 10.0, endY + gridScale / 10.0);
			if (obstacle != null && !obstacle.isObstacle())
				newFields.add(new Field(obstacle.getX(), obstacle.getY(), true, false));
			else newFields.add(new Field((int) Math.floor(x / gridScale), (int) Math.floor(y / gridScale), true, false));
		}
	}
}
