package hu.bme.aut.fox.robotvacuum.movement;

import hu.bme.aut.fox.robotvacuum.RobotVacuum;

public class SimpleMovementController implements MovementController {

	private static final double DISTANCE_THRESHOLD = 0.01;
	private static final double DIRECTION_THRESHOLD = 0.01;

	private static final double MAX_DISTANCE = 0.1f;
	private static final double MAX_ANGLE = 0.1;

	@Override
	public Movement getNextMovement(RobotVacuum.State state, double targetX, double targetY) {
		// Calculating the direction and distance to the target
		double targetDirectionX = targetX - state.getPositionX();
		double targetDirectionY = targetY - state.getPositionY();
		double targetDistance = Math.sqrt(targetDirectionX * targetDirectionX + targetDirectionY * targetDirectionY);

		// Calculating movement towards the target if the distance is over the threshold
		if (targetDistance > DISTANCE_THRESHOLD) {

			// Calculating the direction towards the target
			double targetDirection = Math.acos(targetDirectionX / targetDistance);
			if (targetDirectionY < 0) targetDirection = -targetDirection;

			// Calculating the angle between the current and target direction
			double angle = targetDirection - state.getDirection();
			angle = angle % (2 * Math.PI);
			if (angle < -Math.PI) angle += 2 * Math.PI;
			if (angle > Math.PI) angle -= 2 * Math.PI;

			// Calculating movement towards the target if the angle is below the threshold
			if (angle < DIRECTION_THRESHOLD) {
				return new Movement(
						Math.min(targetDistance, MAX_DISTANCE),
						Math.min(angle, MAX_ANGLE)
				);
			} else {
				// Correcting the rotational error
				return new Movement(0, Math.min(angle, MAX_ANGLE));
			}
		} else {
			return null;
		}
	}
}