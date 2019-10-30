package hu.bme.aut.fox.robotvacuum.virtual.viewmodel;

import hu.bme.aut.fox.robotvacuum.virtual.components.Position;
import hu.bme.aut.fox.robotvacuum.virtual.components.VirtualWorld;
import hu.bme.aut.fox.robotvacuum.virtual.components.VirtualWorldField;
import io.reactivex.subjects.BehaviorSubject;


public class VirtualWorldViewModel implements VirtualWorld.VirtualWorldListener {


    public final BehaviorSubject<Position> robotVacuum = BehaviorSubject.create();

    private VirtualWorld virtualWorld;


    public VirtualWorldViewModel(VirtualWorld vW){
        virtualWorld = vW;
        virtualWorld.addListener(this);
    }


    @Override
    public void positionChanged(Position position) {
        robotVacuum.onNext(position);
    }

    public VirtualWorldField[][] getWorld() {
        return virtualWorld.getWorldMatrix();
    }

    public VirtualWorld getVirtualWorld() {
        return virtualWorld;
    }
}