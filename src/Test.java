import hu.bme.aut.fox.robotvacuum.Position;
import hu.bme.aut.fox.robotvacuumsimulator.virtualcomponent.VirtualMotor;
import hu.bme.aut.fox.robotvacuumsimulator.virtualcomponent.VirtualWorld;
import hu.bme.aut.fox.robotvacuumsimulator.virtualcomponent.VirtualWorldField;

import java.util.ArrayList;
import java.util.List;

public class Test {
	public static void main(String[] args) {
		List<List<VirtualWorldField>> fields = new ArrayList<>();
		fields.add(new ArrayList<VirtualWorldField>());
		fields.get(0).add(new VirtualWorldField(VirtualWorldField.Status.DIRTY));
		fields.get(0).add(new VirtualWorldField(VirtualWorldField.Status.NOTEMPTY));
		fields.get(0).add(new VirtualWorldField(VirtualWorldField.Status.DIRTY));
		fields.add(new ArrayList<>());
		fields.get(1).add(new VirtualWorldField(VirtualWorldField.Status.CLEAN));
		fields.get(1).add(new VirtualWorldField(VirtualWorldField.Status.NOTEMPTY));
		fields.get(1).add(new VirtualWorldField(VirtualWorldField.Status.DIRTY));

		VirtualWorld world = new VirtualWorld(fields);
		VirtualMotor motor = new VirtualMotor(world);
		world.setRobotVacuumPosition(new Position(0, 0, Math.PI * 1.5));

		world.addListener((Position p) -> System.out.println(Math.round(p.x * 100) / 100.0  + " " + Math.round(p.y * 100) / 100.0  + " " + Math.round(p.direction * 180.0 / Math.PI)));

		Thread t = new Thread(motor);
		Thread t2 = new Thread(() -> {
			try {
				Thread.sleep(1400);
				motor.rotate(Math.PI / 2);
				Thread.sleep(5000);
				motor.rotate(-Math.PI);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
		t.start();
		t2.start();
	}
}
