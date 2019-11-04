package hu.bme.aut.fox.robotvacuum.navigation;

import hu.bme.aut.fox.robotvacuum.RobotVacuum;
import hu.bme.aut.fox.robotvacuum.world.Field;
import hu.bme.aut.fox.robotvacuum.world.World;

public class SimpleNavigator implements Navigator {

	@Override
	public Target[] getTargetPath(World world, RobotVacuum.State state) {
		for (double rad = 1; rad < 1000; rad++) {
			for (double dir = 0; dir < Math.PI * 2; dir += 1 / rad) {
				double x = state.getPositionX() + Math.cos(dir) * rad;
				double y = state.getPositionY() + Math.cos(dir) * rad;
				Field field = world.getField(x, y);
				if (field == null || !field.isCleaned()) {
					new Target(x, y);
				}
			}
		}

		return new Target[] {
				new Target(
						state.getPositionX() + Math.random() * 2 - 1,
						state.getPositionY() + Math.random() * 2 - 1
				)
		};
	}
}
