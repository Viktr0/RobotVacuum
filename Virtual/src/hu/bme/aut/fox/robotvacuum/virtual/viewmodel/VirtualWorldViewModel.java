package hu.bme.aut.fox.robotvacuum.virtual.viewmodel;

import hu.bme.aut.fox.robotvacuum.virtual.components.Position;
import hu.bme.aut.fox.robotvacuum.virtual.components.VirtualWorld;
import hu.bme.aut.fox.robotvacuum.virtual.components.VirtualWorldField;
import io.reactivex.subjects.BehaviorSubject;


import java.util.ArrayList;
import java.util.List;

import static hu.bme.aut.fox.robotvacuum.virtual.components.VirtualWorldField.Status.NOTEMPTY;

public class VirtualWorldViewModel implements VirtualWorld.VirtualWorldListener {

    public final BehaviorSubject<List<VirtualWorldField>> world = BehaviorSubject.create();
    public final BehaviorSubject<Position> robotVacuum = BehaviorSubject.create();

    private VirtualWorld virtualWorld;

    public VirtualWorldViewModel(VirtualWorld vW){
        virtualWorld = vW;

    }

    public VirtualWorldViewModel() {

    }

    public VirtualWorld getVirtualWorld(){
        return virtualWorld;
    }

    @Override
    public void positionChanged(Position position) {

    }
}
