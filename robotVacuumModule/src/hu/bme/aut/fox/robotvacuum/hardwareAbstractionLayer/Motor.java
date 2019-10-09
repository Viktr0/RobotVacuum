package hu.bme.aut.fox.robotvacuum.hardwareAbstractionLayer;

public interface Motor {
	void addMotorListener(MotorListener listener);
	void rotate(double deltaPhi);

	interface MotorListener {
		void motorMovedForward(double deltaS);
		void motorRotated(double deltaPhi);
	}
}
