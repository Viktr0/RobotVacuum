package hu.bme.aut.fox.robotvacuum.interpretation;

import hu.bme.aut.fox.robotvacuum.RobotVacuum;
import hu.bme.aut.fox.robotvacuum.hardware.Radar;
import hu.bme.aut.fox.robotvacuum.world.World;

public interface Interpreter {

	World interpretRadar(
			World currentWorld,
			RobotVacuum.State currentState,
			Radar.RadarData[] radarData
	);

	RobotVacuum.State interpretMovement(
			World currentWorld,
			RobotVacuum.State currentState,
			double movementDistance
	);

	RobotVacuum.State interpretRotation(
			World currentWorld,
			RobotVacuum.State currentState,
			double rotationAngle
	);
}
