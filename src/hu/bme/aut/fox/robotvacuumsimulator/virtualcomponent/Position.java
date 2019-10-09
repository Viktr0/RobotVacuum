package hu.bme.aut.fox.robotvacuumsimulator.virtualcomponent;


public class Position {
	public double x;
	public double y;
	public double direction;

	public Position(final double x, final double y, final double dir) {
		this.x = x;
		this.y = y;
		this.direction = dir;
	}
}
