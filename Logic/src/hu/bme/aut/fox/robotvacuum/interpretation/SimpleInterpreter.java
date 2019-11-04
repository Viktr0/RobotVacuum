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
		for (Radar.RadarData data : radarData) {
			double posX = currentState.getPositionX();
			double posY = currentState.getPositionX();
			double dirX = Math.cos(data.getDirection());
			double dirY = Math.sin(data.getDirection());

			for (double dist = 0; dist < data.getDistance(); dist += 0.1) {
				int x = currentWorld.toGridCoordinate(posX + dirX * dist);
				int y = currentWorld.toGridCoordinate(posY + dirY * dist);
				if (currentWorld.getField(x, y) == null) {
					newFields.add(new Field(x, y, false, false));
				}
			}

			if (data.isHit()) {
				newFields.add(new Field(
						currentWorld.toGridCoordinate(posX + dirX * data.getDistance()),
						currentWorld.toGridCoordinate(posY + dirY * data.getDistance()),
						true, false
				));
			}
		}

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
}
