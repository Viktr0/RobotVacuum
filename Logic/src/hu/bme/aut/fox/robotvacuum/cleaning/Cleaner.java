package hu.bme.aut.fox.robotvacuum.cleaning;

import hu.bme.aut.fox.robotvacuum.RobotVacuum;
import hu.bme.aut.fox.robotvacuum.world.Field;
import hu.bme.aut.fox.robotvacuum.world.World;

public class Cleaner {

	public World cleanCurrentField(World currentWorld, RobotVacuum.State currentState) {
		Field currentField = currentWorld.getFieldAt(currentState.getPositionX(), currentState.getPositionY());
		if (currentField.isCleaned())
			return currentWorld;
		Field[] newField = {new Field(currentField.getX(), currentField.getY(), false, true)};
		return new World(currentWorld, newField);
	}

}
