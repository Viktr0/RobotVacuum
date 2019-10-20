package hu.bme.aut.fox.robotvacuum.components;

import hu.bme.aut.fox.robotvacuum.Direction;
import hu.bme.aut.fox.robotvacuum.Position;
import hu.bme.aut.fox.robotvacuum.components.world.InterpretedWorld;
import hu.bme.aut.fox.robotvacuum.components.world.InterpretedWorldField;

public class Discretiser {
	private static final double locationRadiusEpsilonSquared = 0.1 * 0.1;
	private static final double directionAngleEpsilon = 0.1;

	public InterpretedWorldField getDiscreteLocation(InterpretedWorld world) {
		InterpretedWorldField[][] fields = world.getFields();
		Position position = world.getRobotVacuumPosition();
		double worldFieldSize = InterpretedWorld.WORLD_FIELD_SIZE;

		int i, j;
		i = new Double(position.x / worldFieldSize).intValue();
		j = new Double(position.y / worldFieldSize).intValue();

		double centerX, centerY;
		centerX = i * worldFieldSize + worldFieldSize / 2;
		centerY = j * worldFieldSize + worldFieldSize / 2;

		double a = position.x - centerX, b = position.y - centerY;
		if (a * a + b * b <= locationRadiusEpsilonSquared)
			return fields[i][j];
		return null;
	}

	public Direction getDiscreteDirection(InterpretedWorld world) {
		Position position = world.getRobotVacuumPosition();
		if (Math.abs(position.direction - Direction.NORTH.direction) < directionAngleEpsilon)
			return Direction.NORTH;
		if (Math.abs(position.direction - Direction.SOUTH.direction) < directionAngleEpsilon)
			return Direction.SOUTH;
		if (Math.abs(position.direction - Direction.EAST.direction) < directionAngleEpsilon)
			return Direction.EAST;
		if (Math.abs(position.direction - Direction.WEST.direction) < directionAngleEpsilon)
			return Direction.WEST;
		return Direction.UNDEFINED;
	}
}
