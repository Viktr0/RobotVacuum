package hu.bme.aut.fox.robotvacuum.virtual.viewmodel;


import hu.bme.aut.fox.robotvacuum.RobotVacuum;
import hu.bme.aut.fox.robotvacuum.world.Field;
import io.reactivex.subjects.BehaviorSubject;

public class WorldViewModel {


    public BehaviorSubject<Field[][]> world = BehaviorSubject.create();
    private Thread timerThread;
    private RobotVacuum robotVacuum;
    private int scalingFactor = 40;

    public void setRobotVacuum(RobotVacuum rv){
        robotVacuum = rv;
    }

    public RobotVacuum getRobotVacuum(){
        return robotVacuum;
    }

    public WorldViewModel(RobotVacuum rv){
        robotVacuum = rv;
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
        Field[][] fields = new Field[scalingFactor][scalingFactor];
        int min = 0 - scalingFactor/2;
        int max = scalingFactor/2;
        for(int i = min; i < max; ++i)
            for(int j = min; j < max; ++j)
                fields[i + max][j + max] = robotVacuum.getWorld().getField(i, j);
        world.onNext(fields);
    }


    public int getScalingFactor() {
        return scalingFactor;
    }

    public void setScalingFactor(int scalingFactor) {
        this.scalingFactor = scalingFactor;
    }

    public void increaseScalingFactor(){
        if(scalingFactor < 65){
            scalingFactor++;
        }
    }

    public void decreaseScalingFactor(){
        if(scalingFactor > 3){
            scalingFactor--;
        }
    }

}
