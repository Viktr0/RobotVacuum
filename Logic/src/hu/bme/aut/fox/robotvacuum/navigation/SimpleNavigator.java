package hu.bme.aut.fox.robotvacuum.navigation;

import hu.bme.aut.fox.robotvacuum.RobotVacuum;
import hu.bme.aut.fox.robotvacuum.world.Field;
import hu.bme.aut.fox.robotvacuum.world.World;

public class SimpleNavigator implements Navigator {

	@Override
	public Target[] getTargetPath(World world, RobotVacuum.State state) {
		for (double rad = 1; rad < 1000; rad++) {
			for (double dir = 0; dir < Math.PI * 2; dir += 1.0 / rad) {
				double x = state.getPositionX() + Math.cos(dir) * rad;
				double y = state.getPositionY() + Math.sin(dir) * rad;
				Field field = world.getField(x, y);
				if (field != null && !field.isObstacle() && !field.isCleaned()) {
					return new Target[]{
							new Target(
									world.toWorldCoordinate(field.getX()) + world.getGridScale() / 2,
									world.toWorldCoordinate(field.getY()) + world.getGridScale() / 2
							)
					};
				}
			}
		}

		return new Target[]{
				new Target(
						state.getPositionX() + Math.random() * 2 - 1,
						state.getPositionY() + Math.random() * 2 - 1
				)
		};

//		return new Target[] {
//				new Target(1.5, 0.5),
//				new Target(1.5, -2.5),
//				new Target(-0.5, -2.5),
//				new Target(-0.5, -5.5),
//				new Target(1.5, -5.5),
//				new Target(1.5, -2.5),
//				new Target(16.5, -2.5),
//				new Target(16.5, -5.5),
//				new Target(3.5, -5.5),
//				new Target(3.5, -1.5),
//				new Target(-0.5, 0.5),
//				new Target(-0.5, 3.5),
//				new Target(16.5, 3.5),
//				new Target(16.5, 5.5),
//				new Target(4.5, 5.5),
//				new Target(11.5, 3.5),
//				new Target(11.5, -0.5),
//				new Target(16.5, 1.5),
//				new Target(8.5, 1.5),
//				new Target(11.5, 1.5),
//				new Target(11.5, 3.5),
//				new Target(1.5, 3.5)
//		};
	}
}
