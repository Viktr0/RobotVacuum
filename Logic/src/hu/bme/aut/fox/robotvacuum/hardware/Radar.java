package hu.bme.aut.fox.robotvacuum.hardware;

import java.util.List;

public interface Radar {

	void addOnUpdateListener(OnUpdateListener listener);
	void removeOnUpdateListener(OnUpdateListener listener);

	void start();
	void stop();

	interface OnUpdateListener {
		void onUpdate(List<RadarData> data);
	}

	final class RadarData {

		public final double direction;
		public final double distance;

		public RadarData(double dir, double s) {
			direction = dir;
			distance = s;
		}
	}
}
