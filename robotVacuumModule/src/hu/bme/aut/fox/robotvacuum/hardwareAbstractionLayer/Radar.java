package hu.bme.aut.fox.robotvacuum.hardwareAbstractionLayer;

import java.util.List;

public interface Radar {
	void addRadarListener(RadarListener listener);
	void start();
	void stop();

	interface RadarListener {
		void newRadarData(List<RadarData> data);
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
