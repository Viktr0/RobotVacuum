import hu.bme.aut.fox.robotvacuum.virtual.components.Position;
import hu.bme.aut.fox.robotvacuum.virtual.components.VirtualMotor;
import hu.bme.aut.fox.robotvacuum.virtual.components.VirtualWorld;
import hu.bme.aut.fox.robotvacuum.virtual.components.WorldLoader;

import java.io.FileNotFoundException;

public class Test {

	public static void main(String[] args) {

		VirtualWorld world = null;
		try {
			world = new WorldLoader("world1").load();
			VirtualMotor motor = new VirtualMotor(world);

			world.addListener(p -> {
				System.out.println(Math.round(p.x * 100) / 100.0  + " " + Math.round(p.y * 100) / 100.0  + " " + Math.round(p.direction * 180.0 / Math.PI));
			});

			motor.addOnMovementListener(distance -> {
				System.out.println("Distance moved: " + distance);
			});

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
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
