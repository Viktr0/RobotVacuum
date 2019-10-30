import hu.bme.aut.fox.robotvacuum.virtual.components.*;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
public class Test {
	public static void main(String[] args) {

		VirtualWorld world = null;
		try {
			world = new WorldLoader("world1").load();
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
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
