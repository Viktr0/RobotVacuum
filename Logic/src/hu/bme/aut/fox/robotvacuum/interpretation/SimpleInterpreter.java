package hu.bme.aut.fox.robotvacuum.interpretation;

import hu.bme.aut.fox.robotvacuum.RobotVacuum;
import hu.bme.aut.fox.robotvacuum.hardware.Radar;
import hu.bme.aut.fox.robotvacuum.world.Field;
import hu.bme.aut.fox.robotvacuum.world.World;

import java.util.LinkedList;
import java.util.List;

public class SimpleInterpreter implements Interpreter {

	@Override
	public Interpretation interpretRadar(
			World currentWorld,
			RobotVacuum.State currentState,
			Radar.RadarData[] radarData
	) {
		double gridScale = currentWorld.getGridScale();
		double posX = currentState.getPositionX();
		double posY = currentState.getPositionY();

		List<Field> emptyFields = new LinkedList<>();
		List<Field> unreachableFields = new LinkedList<>();
		List<Field> obstacleFields = new LinkedList<>();

		for (Radar.RadarData data : radarData) {
			double dirX = Math.cos(data.getDirection());
			double dirY = Math.sin(data.getDirection());
			double dist = data.getDistance();

			double stepSize = gridScale * 0.1;
			for (double progress = 0; progress < dist; progress += stepSize) {
				int gridX = currentWorld.toGridCoordinate(posX + dirX * progress);
				int gridY = currentWorld.toGridCoordinate(posY + dirY * progress);
				if (currentWorld.getGridField(gridX, gridY) == null) {
					emptyFields.add(new Field(gridX, gridY, false, true, false));
				}
			}

			if (data.isHit()) {
				int hitGridX = currentWorld.toGridCoordinate(posX + dirX * dist);
				int hitGridY = currentWorld.toGridCoordinate(posY + dirY * dist);
				double hitCenterX = hitGridX + 0.5;
				double hitCenterY = hitGridY + 0.5;

				double r = currentState.getSize() / 2 / gridScale;
				for (int gridY = (int) (hitGridY - r); gridY < Math.ceil(hitGridY + r); gridY++) {
					for (int gridX = (int) (hitGridX - r); gridX < Math.ceil(hitGridX + r); gridX++) {
						double distanceX = gridX + 0.5 - hitCenterX;
						double distanceY = gridY + 0.5 - hitCenterY;
						if (Math.sqrt(distanceX * distanceX + distanceY * distanceY) < r) {
							Field field = currentWorld.getGridField(gridX, gridY);
							if (field == null || !field.isObstacle()) {
								unreachableFields.add(new Field(
										gridX, gridY,
										false, false,
										field != null && field.isCleaned()
								));
							}
						}
					}
				}

				if (currentWorld.getGridField(hitGridX, hitGridY) != null) {
					obstacleFields.add(new Field(hitGridX, hitGridY, true, false, false));
				}
			}
		}

		List<Field> newFields = new LinkedList<>();
		newFields.addAll(emptyFields);
		newFields.addAll(unreachableFields);
		newFields.addAll(obstacleFields);

		return new Interpretation(
				new World(currentWorld, newFields.toArray(new Field[0])),
				currentState
		);
	}

	@Override
	public Interpretation interpretMovement(
			World currentWorld,
			RobotVacuum.State currentState,
			double distance
	) {
		RobotVacuum.State state = new RobotVacuum.State(
				currentState.getSize(),
				currentState.getPositionX() + Math.cos(currentState.getDirection()) * distance,
				currentState.getPositionY() + Math.sin(currentState.getDirection()) * distance,
				currentState.getDirection()
		);

		Field currentField = currentWorld.getField(state.getPositionX(), state.getPositionY());
		if (currentField == null) {
			currentField = new Field(
					currentWorld.toGridCoordinate(state.getPositionX()),
					currentWorld.toGridCoordinate(state.getPositionY()),
					false, true, false
			);
		}

		Field cleanedField = new Field(
				currentField.getX(), currentField.getY(),
				currentField.isObstacle(), currentField.isReachable(), true
		);
		World world = new World(currentWorld, cleanedField);

		return new Interpretation(world, state);
	}

	@Override
	public Interpretation interpretRotation(
			World currentWorld,
			RobotVacuum.State currentState,
			double angle
	) {
		RobotVacuum.State state = new RobotVacuum.State(
				currentState.getSize(),
				currentState.getPositionX(),
				currentState.getPositionY(),
				currentState.getDirection() + angle
		);

		return new Interpretation(currentWorld, state);
	}
}
