package hu.bme.aut.fox.robotvacuum.navigation;

import hu.bme.aut.fox.robotvacuum.RobotVacuum;
import hu.bme.aut.fox.robotvacuum.world.World;

public class SimpleNavigator implements Navigator {

	@Override
	public Target[] getTargetPath(World world, RobotVacuum.State state) {
		return new Target[] {
				new Target(
						state.getPositionX() + 0.3f,
						state.getPositionY() + 0.0f
				)
		};
	}
}
