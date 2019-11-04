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
		private final boolean hit;

		public RadarData(double direction, double distance, boolean hit) {
			this.direction = direction;
			this.distance = distance;
			this.hit = hit;
		}

		public double getDirection() {
			return direction;
		}

		public double getDistance() {
			return distance;
		}

		public boolean isHit() {
			return hit;
		}
	}
}
