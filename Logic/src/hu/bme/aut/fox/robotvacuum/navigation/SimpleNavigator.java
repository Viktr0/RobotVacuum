package hu.bme.aut.fox.robotvacuum.navigation;

import hu.bme.aut.fox.robotvacuum.RobotVacuum;
import hu.bme.aut.fox.robotvacuum.world.World;

public class SimpleNavigator implements Navigator {

	@Override
	public Target[] getTargetPath(World world, RobotVacuum.State state) {
		// TODO:
		return new Target[0];
	}
}
