package hu.bme.aut.fox.robotvacuum.hardware;

public interface OldMotor {

	void move(double distance);
	void rotate(double angle);

	void addOnMovementListener(OnMovementListener listener);
	void removeOnMovementListener(OnMovementListener listener);

	void addOnRotationListener(OnRotationListener listener);
	void removeOnRotationListener(OnRotationListener listener);

	void start();
	void stop();

	interface OnMovementListener {
		void onMovement(double distance);
	}

	interface OnRotationListener {
		void onRotation(double angle);
	}
}
