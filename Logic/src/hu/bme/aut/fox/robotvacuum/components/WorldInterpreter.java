package hu.bme.aut.fox.robotvacuum.components;

import hu.bme.aut.fox.robotvacuum.components.world.InterpretedWorld;
import hu.bme.aut.fox.robotvacuum.hardware.Radar;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

public class WorldInterpreter {

	public void refreshWorld(InterpretedWorld world, List<Radar.RadarData> radarData) {
		// If world expands recalculate position
		throw new NotImplementedException(); //TODO
	}
}
