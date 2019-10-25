package hu.bme.aut.fox.robotvacuum.virtual.viewmodel;


import hu.bme.aut.fox.robotvacuum.RobotVacuum;
import hu.bme.aut.fox.robotvacuum.world.Field;
import io.reactivex.subjects.BehaviorSubject;

public class WorldViewModel {


    public BehaviorSubject<Field[][]> world = BehaviorSubject.createDefault(null);
    private RobotVacuum robotVacuum;
    private Thread timerThread;
    public WorldViewModel(){
        timerThread = new Thread(() -> {
            while(true) {
                this.updateWorld();
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        timerThread.start();
    }

    private void updateWorld() {
        Field[][] fields = new Field[40][40];
        for(int i = -20; i < 20; ++i)
            for(int j = -20; j < 20;++j)
                fields[i + 20][j + 20] = robotVacuum.getWorld().getField(i, j);

        world.onNext(fields);
        world.getValue();
    }

}
