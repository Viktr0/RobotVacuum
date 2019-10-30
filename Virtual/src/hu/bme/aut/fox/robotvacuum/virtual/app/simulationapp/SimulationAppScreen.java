package hu.bme.aut.fox.robotvacuum.virtual.app.simulationapp;

import hu.bme.aut.fox.robotvacuum.virtual.app.App;
import hu.bme.aut.fox.robotvacuum.virtual.app.Simulation;
import hu.bme.aut.fox.robotvacuum.virtual.app.virtualworld.VirtualWorldScreen;
import hu.bme.aut.fox.robotvacuum.virtual.app.world.WorldScreen;
import hu.bme.aut.fox.robotvacuum.world.*;

import javax.swing.*;

public class SimulationAppScreen extends App.Screen {

    private Simulation simulation;


    public SimulationAppScreen(){
        BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(layout);
        add(Box.createHorizontalGlue());

        simulation = new Simulation();
        WorldScreen worldScreen = new WorldScreen(simulation.getRobotVacuum());
        VirtualWorldScreen virtualWorldScreen = new VirtualWorldScreen(simulation.getWorld());

        add(worldScreen);
        add(virtualWorldScreen);
    }

}
