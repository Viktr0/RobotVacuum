package hu.bme.aut.fox.robotvacuum.hal;

import java.util.List;

public interface Radar {

	void addRadarListener(RadarListener listener);
	void removeRadarListener(RadarListener listener);

	void start();
	void stop();

	interface RadarListener {
		void onRadar(List<RadarData> data);
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
