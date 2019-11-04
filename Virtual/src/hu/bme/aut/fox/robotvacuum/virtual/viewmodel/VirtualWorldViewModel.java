package hu.bme.aut.fox.robotvacuum.virtual.viewmodel;

import hu.bme.aut.fox.robotvacuum.hardware.Radar;
import hu.bme.aut.fox.robotvacuum.virtual.components.*;
import io.reactivex.subjects.BehaviorSubject;


public class VirtualWorldViewModel {


    public final BehaviorSubject<Position> robotVacuum = BehaviorSubject.create();
    public final BehaviorSubject<Radar.RadarData[]> radarData = BehaviorSubject.create();

    private VirtualWorld virtualWorld;
    private VirtualRadar virtualRadar;
    private VirtualMotor virtualMotor;

    public VirtualWorldViewModel(
            VirtualWorld virtualWorld,
            VirtualRadar virtualRadar,
            VirtualMotor virtualMotor
    ){
        this.virtualWorld = virtualWorld;
        this.virtualRadar = virtualRadar;
        this.virtualMotor = virtualMotor;

        virtualWorld.addListener(this::positionChanged);
        virtualRadar.addOnUpdateListener(this::onUpdate);
    }

    private void positionChanged(Position position) {
        robotVacuum.onNext(position);
    }

    private void onUpdate(Radar.RadarData[] data) {
        radarData.onNext(data.clone());
    }

    public VirtualWorldField[][] getWorld() {
        return virtualWorld.getWorldMatrix();
    }

    public VirtualWorld getVirtualWorld() {
        return virtualWorld;
    }
}