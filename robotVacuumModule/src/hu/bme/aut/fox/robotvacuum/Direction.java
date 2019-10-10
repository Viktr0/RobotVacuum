package hu.bme.aut.fox.robotvacuum;

public enum Direction {
	NORTH(Math.PI / 2), SOUTH(Math.PI / 2 * 3), WEST(Math.PI), EAST(0), UNDEFINED(-1);

	public final double direction;

	Direction(double dir) {
		direction = dir;
	}
}
