package hu.bme.aut.fox.robotvacuum.components;

import hu.bme.aut.fox.robotvacuum.components.interpretedworld.InterpretedWorld;
import hu.bme.aut.fox.robotvacuum.hardwareAbstractionLayer.Radar;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

public class WorldInterpreter {
	public void refreshWorld(InterpretedWorld world, List<Radar.RadarData> radarData) {
		// If world expands recalculate position
		throw new NotImplementedException(); //TODO
	}
}
