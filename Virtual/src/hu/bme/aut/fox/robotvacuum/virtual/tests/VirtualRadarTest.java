package hu.bme.aut.fox.robotvacuum.virtual.tests;

import hu.bme.aut.fox.robotvacuum.RobotVacuum;
import hu.bme.aut.fox.robotvacuum.hardware.Radar;
import hu.bme.aut.fox.robotvacuum.interpretation.Interpreter;
import hu.bme.aut.fox.robotvacuum.interpretation.RobotVacuumInterpreter;
import hu.bme.aut.fox.robotvacuum.virtual.app.Simulation;
import hu.bme.aut.fox.robotvacuum.virtual.components.VirtualRadar;
import hu.bme.aut.fox.robotvacuum.virtual.components.VirtualWorld;
import hu.bme.aut.fox.robotvacuum.world.Field;
import hu.bme.aut.fox.robotvacuum.world.World;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VirtualRadarTest {

	static VirtualRadar radar;
	static VirtualWorld world;

	public static void main(String[] args) {
		assertDoesNotThrow(() -> {
			Simulation s = new Simulation();
			world = s.getWorld();
			radar = new VirtualRadar(world);
			radar.addOnUpdateListener(radarData -> {
				interpreterTest(radarData);
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				radar.stop();
			});
			radar.start();
		});
	}

	static void interpreterTest(Radar.RadarData[] data) {
		Interpreter interpreter = new RobotVacuumInterpreter();
		World world = interpreter.interpretRadar(new World(1.0), new RobotVacuum.State(), data);
		for (int i = -20; i < 20; ++i) {
			for (int j = -20; j < 20; ++j){
				if ((j == -20 || j == 19) || (i == -20 || i == 19)) System.out.print("..");
				else {
					Field f = world.getField(i, j);
					if (f == null) System.out.print("??");
					else if (!f.isObstacle()) System.out.print("  ");
					else System.out.print("##");
				}
			}
			System.out.println();
		}

	}
}