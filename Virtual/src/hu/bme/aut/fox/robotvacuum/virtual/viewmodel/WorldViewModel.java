package hu.bme.aut.fox.robotvacuum.virtual.viewmodel;

import hu.bme.aut.fox.robotvacuum.virtual.components.Position;
import hu.bme.aut.fox.robotvacuum.virtual.components.VirtualWorld;
import hu.bme.aut.fox.robotvacuum.virtual.components.VirtualWorldField;
import io.reactivex.subjects.BehaviorSubject;


import java.util.ArrayList;
import java.util.List;

import static hu.bme.aut.fox.robotvacuum.virtual.components.VirtualWorldField.Status.NOTEMPTY;

public class WorldViewModel implements VirtualWorld.VirtualWorldListener {

    public final BehaviorSubject<List<VirtualWorldField>> world = BehaviorSubject.create();
    public final BehaviorSubject<Position> robotVacuum = BehaviorSubject.create();

    private VirtualWorld virtualWorld;

    public WorldViewModel(VirtualWorld vW){
        virtualWorld = vW;
        //vW.addListener();
    }

    public WorldViewModel() {

    }

    public void setDefaultWorld(){
        List<List<VirtualWorldField>> fields = new ArrayList<>();
        for(int i = 0; i < 50; i++){
            fields.add(new ArrayList<>());
            for(int j = 0; j < 64; j++){
                fields.get(i).add(new VirtualWorldField(VirtualWorldField.Status.DIRTY));
            }
        }
        for (int ii = 0; ii < 50; ii++) {
            for (int jj = 0; jj < 64; jj++) {
                if (ii == 0 || ii == 49
                        || (ii == 16 && jj < 8) || (ii == 16 && jj > 11 && jj < 26) || (ii == 16 && jj > 29 && jj < 47) || (ii == 16 && jj > 50)
                        || (ii == 34 && jj < 17) || (ii == 34 && jj > 20 && jj < 43) || (ii == 34 && jj > 46)
                        || jj == 0 || jj == 63
                        || (jj == 20 && ii < 8) || (jj == 20 && ii > 11 && ii < 16)
                        || (jj == 40 && ii < 8) || (jj == 40 && ii > 11 && ii < 16)
                        || (jj == 30 && ii > 34 && ii < 40) || (jj == 30 && ii > 43)) {
                    fields.get(ii).get(jj).status = NOTEMPTY;
                }
            }
        }
        virtualWorld = new VirtualWorld(fields, 50, 64);
        virtualWorld.setRobotVacuumPosition(new hu.bme.aut.fox.robotvacuum.Position(1, 1, Math.PI * 1.5));
    }

    public VirtualWorld getVirtualWorld(){
        return virtualWorld;
    }

    @Override
    public void positionChanged(hu.bme.aut.fox.robotvacuum.Position position) {

    }
}
