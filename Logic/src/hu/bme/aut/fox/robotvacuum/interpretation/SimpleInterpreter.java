package hu.bme.aut.fox.robotvacuum.interpretation;

import hu.bme.aut.fox.robotvacuum.RobotVacuum;
import hu.bme.aut.fox.robotvacuum.hardware.Radar;
import hu.bme.aut.fox.robotvacuum.world.Field;
import hu.bme.aut.fox.robotvacuum.world.World;

import java.util.LinkedList;
import java.util.List;

public class SimpleInterpreter implements Interpreter {

	private static final double ERROR = 0.25f;

	@Override
	public Interpretation interpretRadar(
			double size,
			World currentWorld,
			RobotVacuum.State currentState,
			Radar.RadarData[] radarData
	) {
		// TODO: Better implementation

		double gridScale = currentWorld.getGridScale();
		double posX = currentState.getPositionX();
		double posY = currentState.getPositionY();
		double gridPosX = currentState.getPositionX() / gridScale;
		double gridPosY = currentState.getPositionY() / gridScale;

		List<Field> emptyFields = new LinkedList<>();
		List<Field> obstacleFields = new LinkedList<>();
		for (Radar.RadarData data : radarData) {
//			List<Field> intersectedFields = new LinkedList<>();
//
			double dirX = Math.cos(data.getDirection());
			double dirY = Math.sin(data.getDirection());
//			double gridDist = data.getDistance() / gridScale;
//
//			double gridDirX = dirX * gridDist;
//			double gridDirY = dirY * gridDist;
//
//			double gridEndX = gridPosX + gridDirX;
//			double gridEndY = gridPosY + gridDirY;
//
//			int closestX = (int) Math.floor(gridPosX) + (dirX > 0 ? 1 : 0);
//			int furthestX = (int) Math.floor(gridPosX + dirX * gridDist) + (dirX > 0 ? 0 : 1);
//
//			int closestY = (int) Math.floor(gridPosY) + (dirY > 0 ? 1 : 0);
//			int furthestY = (int) Math.floor(gridPosY + dirY * gridDist) + (dirY > 0 ? 0 : 1);
//
////			System.out.println(dirX + ", " + dirY + ", " + gridDist + ", " + closestX + ", " + furthestX);
//
//			int startX = dirX > 0 ? closestX : furthestX;
//			int endX = dirX > 0 ? furthestX : closestX;
//			for (int x = startX; x <= endX; x++) {
//				int fieldX = dirX > 0 ? x : x - 1;
//				int fieldY = (int) Math.floor((x - gridPosX) / dirX * dirY + gridPosY);
//
//				if (currentWorld.getGridField(fieldX, fieldY) == null) {
//					emptyFields.add(new Field(fieldX, fieldY, false, false));
//				}
//			}
//
//			int startY = dirY > 0 ? closestY : furthestY;
//			int endY = dirY > 0 ? furthestY : closestY;
//			for (int y = startY; y <= endY; y++) {
//				int fieldY = dirX > 0 ? y : y - 1;
//				int fieldX = (int) Math.floor((y - gridPosY) / dirY * dirX + gridPosX);
//
//				if (currentWorld.getGridField(fieldX, fieldY) == null) {
//					emptyFields.add(new Field(fieldX, fieldY, false, false));
//				}
//			}

			for (double dist = 0; dist < data.getDistance(); dist += 0.1) {
				int x = currentWorld.toGridCoordinate(posX + dirX * dist);
				int y = currentWorld.toGridCoordinate(posY + dirY * dist);
				if (currentWorld.getGridField(x, y) == null) {
					emptyFields.add(new Field(x, y, false, false));
				}
			}

//			if (data.isHit()) {
//				newFields.add(new Field(
//						currentWorld.toGridCoordinate(posX + dirX * data.getDistance()),
//						currentWorld.toGridCoordinate(posY + dirY * data.getDistance()),
//						true, false
//				));
//			}

			if (data.isHit()) {
				Field hitField = getHitField(currentWorld,
						posX + dirX * data.getDistance(),
						posY + dirY * data.getDistance(),
						dirX, dirY
				);
				if (hitField != null) obstacleFields.add(hitField);
			}
		}

		List<Field> newFields = new LinkedList<>();
		newFields.addAll(emptyFields);
		newFields.addAll(obstacleFields);

		return new Interpretation(
				new World(currentWorld, newFields.toArray(new Field[0])),
				currentState
		);
	}

	private List<Field> getIntersectedFields() {
		return null;
	}

	private Field getHitField(World world, double x, double y, double dirX, double dirY) {
		int gridX = world.toGridCoordinate(x);
		int gridY = world.toGridCoordinate(y);

		double xInField = x / world.getGridScale() - gridX;
		double yInField = y / world.getGridScale() - gridY;

		int regionX = xInField < ERROR ? -1 : (xInField > 1.0 - ERROR ? 1 : 0);
		int regionY = yInField < ERROR ? -1 : (yInField > 1.0 - ERROR ? 1 : 0);

		if (regionX == 0 && regionY == 0) {
			return new Field(gridX, gridY, true, false);
		} else if (regionX == 0) {
			return new Field(gridX, regionY < 0 ?
					dirY < 0 ? gridY - 1 : gridY :
					dirY > 0 ? gridY + 1 : gridY,
					true, false
			);
		} else if (regionY == 0){
			return new Field(regionX < 0 ?
					dirX < 0 ? gridX - 1 : gridX :
					dirX > 0 ? gridX + 1 : gridX,
					gridY, true, false
			);
		}

		return null;
	}

	@Override
	public Interpretation interpretMovement(
			double size,
			World currentWorld,
			RobotVacuum.State currentState,
			double distance
	) {
		RobotVacuum.State state = new RobotVacuum.State(
				currentState.getPositionX() + Math.cos(currentState.getDirection()) * distance,
				currentState.getPositionY() + Math.sin(currentState.getDirection()) * distance,
				currentState.getDirection()
		);

		Field currentField = currentWorld.getField(state.getPositionX(), state.getPositionY());
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
	public Interpretation interpretRotation(
			double size,
			World currentWorld,
			RobotVacuum.State currentState,
			double angle
	) {
		RobotVacuum.State state = new RobotVacuum.State(
				currentState.getPositionX(),
				currentState.getPositionY(),
				currentState.getDirection() + angle
		);

		return new Interpretation(currentWorld, state);
	}
}
