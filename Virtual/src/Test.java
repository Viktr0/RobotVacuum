import hu.bme.aut.fox.robotvacuum.virtual.components.Position;
import hu.bme.aut.fox.robotvacuum.virtual.components.VirtualMotor;
import hu.bme.aut.fox.robotvacuum.virtual.components.VirtualWorld;
import hu.bme.aut.fox.robotvacuum.virtual.components.VirtualWorldField;

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

		VirtualWorld world = new VirtualWorld(fields, 2, 3);
		VirtualMotor motor = new VirtualMotor(world);
		world.setRobotVacuumPosition(new Position(0, 0, Math.PI * 1.5));

		world.addListener((Position p) -> System.out.println(Math.round(p.x * 100) / 100.0  + " " + Math.round(p.y * 100) / 100.0  + " " + Math.round(p.direction * 180.0 / Math.PI)));

		Thread t2 = new Thread(() -> {
			try {
				motor.move(100);
				Thread.sleep(1400);
				motor.rotate(Math.PI / 2);
				Thread.sleep(5000);
				motor.move(100);
				Thread.sleep(7000);
				motor.rotate(-Math.PI);
				motor.stop();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
		motor.start();
		t2.start();
	}
}
