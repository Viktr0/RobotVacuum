package hu.bme.aut.fox.robotvacuum.hardwareAbstractionLayer;

public interface Motor {
	void addMotorListener(MotorListener listener);
	void rotate(double deltaPhi);
	void moveForward(double deltaS);
	void start();
	void stop();

	interface MotorListener {
		void motorMovedForward(double deltaS);
		void motorRotated(double deltaPhi);
	}
}
