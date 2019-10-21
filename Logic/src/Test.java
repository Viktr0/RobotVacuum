import hu.bme.aut.fox.robotvacuum.Direction;
import hu.bme.aut.fox.robotvacuum.Position;
import hu.bme.aut.fox.robotvacuum.components.Discretiser;
import hu.bme.aut.fox.robotvacuum.components.world.InterpretedWorld;
import hu.bme.aut.fox.robotvacuum.components.world.InterpretedWorldField;

import java.util.ArrayList;
import java.util.List;

public class Test {
	public static void main(String[] args) {
		Discretiser d = new Discretiser();

		List<List<InterpretedWorldField>> fields = new ArrayList<>();
		fields.add(new ArrayList<>());
		fields.get(0).add(new InterpretedWorldField(InterpretedWorldField.Status.CLEANED));
		fields.get(0).add(new InterpretedWorldField(InterpretedWorldField.Status.NOTEMPTY));
		fields.get(0).add(new InterpretedWorldField(InterpretedWorldField.Status.DIRTY));
		fields.add(new ArrayList<>());
		fields.get(1).add(new InterpretedWorldField(InterpretedWorldField.Status.CLEANED));
		fields.get(1).add(new InterpretedWorldField(InterpretedWorldField.Status.NOTEMPTY));
		fields.get(1).add(new InterpretedWorldField(InterpretedWorldField.Status.DIRTY));

		InterpretedWorld world = new InterpretedWorld();
		world.addListener(new InterpretedWorld.InterpretedWorldListener() {
			@Override
			public void worldMatrixChanged(InterpretedWorldField[][] matrix, int N, int M) {
				for(int i = 0; i < N; ++i)
					for(int j = 0; j < M; ++j)
						System.out.println(i +" " + j + " " + matrix[i][j]);
			}

			@Override
			public void positionChanged(Position position) {
				System.out.println("(" + position.x + ", " + position.y + " " + position.direction + ")");
			}
		});

		world.setNewMatrix(fields, 2, 3);
		world.setRobotVacuumPosition(new Position(1, 3, -Math.PI / 2 - 0.08));

		InterpretedWorldField result = d.getDiscreteLocation(world);
		Direction direction = d.getDiscreteDirection(world);

		System.out.println(result);
	}
}
