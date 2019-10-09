package hu.bme.aut.fox.robotvacuum;

import hu.bme.aut.fox.robotvacuum.hardwareAbstractionLayer.Motor;
import hu.bme.aut.fox.robotvacuum.hardwareAbstractionLayer.Radar;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

public class RobotVacuum implements Radar.RadarListener, Motor.MotorListener {
	private Radar radar;
	private Motor motor;

	public RobotVacuum(Radar radar, Motor motor) {
		this.radar = radar;
		this.motor = motor;
	}

	@Override
	public void motorMovedForward(double deltaS) {
		throw new NotImplementedException(); //TODO
	}

	@Override
	public void motorRotated(double deltaPhi) {
		throw new NotImplementedException(); //TODO
	}

	@Override
	public void newRadarData(List<Radar.RadarData> data) {
		throw new NotImplementedException(); //TODO
	}
}
