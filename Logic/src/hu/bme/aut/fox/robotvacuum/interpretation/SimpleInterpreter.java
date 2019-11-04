package hu.bme.aut.fox.robotvacuum.interpretation;

import hu.bme.aut.fox.robotvacuum.RobotVacuum;
import hu.bme.aut.fox.robotvacuum.hardware.Radar;
import hu.bme.aut.fox.robotvacuum.world.Field;
import hu.bme.aut.fox.robotvacuum.world.World;

import java.util.LinkedList;
import java.util.List;

public class SimpleInterpreter implements Interpreter {

	@Override
	public Interpretation interpretRadar(World currentWorld, RobotVacuum.State currentState, Radar.RadarData[] radarData) {
		List<Field> newFields = new LinkedList<>();
		for (Radar.RadarData data : radarData) interpretRadarRay(currentWorld, currentState, data, newFields);

		return new Interpretation(
				new World(currentWorld, newFields.toArray(new Field[0])),
				currentState
		);
	}

	@Override
	public Interpretation interpretMovement(World currentWorld, RobotVacuum.State currentState, double distance) {
		RobotVacuum.State state = new RobotVacuum.State(
				currentState.getPositionX() + Math.cos(currentState.getDirection()) * distance,
				currentState.getPositionY() + Math.sin(currentState.getDirection()) * distance,
				currentState.getDirection()
		);

		Field currentField = currentWorld.getFieldAt(state.getPositionX(), state.getPositionY());
		if (currentField == null) {
			currentField = new Field(
					currentWorld.toGridCoordinate(state.getPositionX()),
					currentWorld.toGridCoordinate(state.getPositionY()),
					false, false
			);
		}

		Field cleanedField = new Field(currentField.getX(), currentField.getY(), currentField.isObstacle(), true);
		World world = new World(currentWorld, cleanedField);

		return new Interpretation(world, state);
	}

	@Override
	public Interpretation interpretRotation(World currentWorld, RobotVacuum.State currentState, double angle) {
		RobotVacuum.State state = new RobotVacuum.State(
				currentState.getPositionX(),
				currentState.getPositionY(),
				currentState.getDirection() + angle
		);

		return new Interpretation(currentWorld, state);
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
		for (double s = 0; s < distance; s += ds, x += dx, y += dy) {
			Field field = currentWorld.getFieldAt(x, y);
			if (field != null)
				newFields.add(new Field(field.getX(), field.getY(), false, field.isCleaned()));
			else
				newFields.add(new Field((int) Math.floor(x / gridScale), (int) Math.floor(y / gridScale), false, false));
		}

		if (ray.isObstacle()) {
			final double endX = Math.cos(currentState.getDirection()) * distance + currentState.getPositionX();
			final double endY = Math.sin(currentState.getDirection()) * distance + currentState.getPositionY();
			Field obstacle = currentWorld.getFieldAt(endX + gridScale / 10.0, endY + gridScale / 10.0);
			if (obstacle != null && (!obstacle.isObstacle() || true)) // TODO: Hotfix
				newFields.add(new Field(obstacle.getX(), obstacle.getY(), true, false));
			else
				newFields.add(new Field((int) Math.floor(x / gridScale), (int) Math.floor(y / gridScale), true, false));
		}
	}
}
