package hu.bme.aut.fox.robotvacuum.hal;

public interface Motor {

	void move(double deltaS);
	void rotate(double deltaPhi);

	void addMotorListener(MotorListener listener);
	void removeMotorListener(MotorListener listener);

	void start();
	void stop();

	interface MotorListener {
		void onMovement(double distance);
		void onRotation(double angle);
	}
}
