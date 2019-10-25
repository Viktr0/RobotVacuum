package hu.bme.aut.fox.robotvacuum.hardware;

public interface Radar {

	void addOnUpdateListener(OnUpdateListener listener);
	void removeOnUpdateListener(OnUpdateListener listener);

	void start();
	void stop();

	interface OnUpdateListener {
		void onUpdate(RadarData[] data);
	}

	final class RadarData {

		private final double direction;
		private final double distance;
		private final boolean obstacle;

		public RadarData(double dir, double s, boolean o) {
			direction = dir;
			distance = s;
			obstacle = o;
		}

		public double getDirection() {
			return direction;
		}

		public double getDistance() {
			return distance;
		}

		public boolean isObstacle() {
			return obstacle;
		}
	}
}
