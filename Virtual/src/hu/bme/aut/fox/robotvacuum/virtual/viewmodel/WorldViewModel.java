package hu.bme.aut.fox.robotvacuum.virtual.viewmodel;


import hu.bme.aut.fox.robotvacuum.RobotVacuum;
import hu.bme.aut.fox.robotvacuum.world.Field;
import io.reactivex.subjects.BehaviorSubject;

public class WorldViewModel {

    public BehaviorSubject<Field[][]> world = BehaviorSubject.create();
    private Thread timerThread;
    private RobotVacuum robotVacuum;
    private Field[][] fields;
    private int scalingFactor = 40;

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
        System.out.println("WorldViewModel.updateWorld meghivodott");
        fields = new Field[scalingFactor][scalingFactor];
        int min = 0 - scalingFactor/2;
        int max = scalingFactor/2;
        for(int i = min; i < max; ++i)
            for(int j = min; j < max; ++j)
                fields[i + max][j + max] = robotVacuum.getWorld().getGridField(j, i);
        world.onNext(fields);
    }

    public Field[][] getFields(){
        return fields;
    }


    public int getScalingFactor() {
        return scalingFactor;
    }

    public void setScalingFactor(int scalingFactor) {
        this.scalingFactor = scalingFactor;
    }

    public void increaseScalingFactor(){
        if(scalingFactor < 66){
            scalingFactor = scalingFactor + 2;
        }
    }

    public void decreaseScalingFactor(){
        if(scalingFactor > 4){
            scalingFactor = scalingFactor -2;
        }
    }

}
