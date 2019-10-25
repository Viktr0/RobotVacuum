package hu.bme.aut.fox.robotvacuum.virtual.viewmodel;

import hu.bme.aut.fox.robotvacuum.virtual.components.Position;
import hu.bme.aut.fox.robotvacuum.virtual.components.VirtualWorld;
import hu.bme.aut.fox.robotvacuum.virtual.components.VirtualWorldField;
import io.reactivex.subjects.BehaviorSubject;


import java.util.List;


public class VirtualWorldViewModel implements VirtualWorld.VirtualWorldListener {

    private List<List<VirtualWorldField>> worldFields;

    public final BehaviorSubject<Position> robotVacuum = BehaviorSubject.create();

    private VirtualWorld virtualWorld;


    public VirtualWorldViewModel(VirtualWorld vW){
        virtualWorld = vW;
        worldFields = virtualWorld.getWorldMatrix();
        virtualWorld.addListener(this);
    }


    public VirtualWorld getVirtualWorld() {
        return virtualWorld;
    }

    @Override
    public void positionChanged(Position position) {
        robotVacuum.onNext(position);
    }

    public List<List<VirtualWorldField>> getWorld() {
        return worldFields;
    }
}