package hu.bme.aut.fox.robotvacuum.navigation;

	import hu.bme.aut.fox.robotvacuum.RobotVacuum;

public class MotorController {
	private static final double rotationEpsilon = 0.01;
	private static final double movementEpsilon = 0.01;

	public boolean isRotationCompleted (RobotVacuum.State currentState, Navigator.Target nextTarget) {
		final double ang = getNextTargetDirection(currentState, nextTarget);
		return Math.abs(getPositiveAngle(currentState.getDirection()) - ang) < rotationEpsilon;
	}

	public boolean isMovementCompleted (RobotVacuum.State currentState, Navigator.Target nextTarget) {
		return 	Math.abs(currentState.getPositionX()  - nextTarget.getX()) < movementEpsilon &&
				Math.abs(currentState.getPositionY()  - nextTarget.getY()) < movementEpsilon;
	}

	public double getNextTargetDirection (RobotVacuum.State currentState, Navigator.Target nextTarget) {
		final double a = nextTarget.getX() - currentState.getPositionX();
		final double b = nextTarget.getY() - currentState.getPositionY();

		double ang;
		if (a == 0) ang = b > 0 ? Math.PI : 2.0/3.0 * Math.PI;
		else ang = Math.atan(b / a);

		return getPositiveAngle(ang);
	}

	public double getNextTargetDistance(RobotVacuum.State currentState, Navigator.Target nextTarget) {
		final double a = nextTarget.getX() - currentState.getPositionX();
		final double b = nextTarget.getY() - currentState.getPositionY();
		return Math.sqrt(a * a + b * b);
	}

	private double getPositiveAngle(double angle) {
		final double PI_2 = Math.PI * 2;
		while (angle > PI_2) angle -= PI_2;
		while (angle < 0) angle += PI_2;
		return angle;
	}
}
