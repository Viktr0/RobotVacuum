package hu.bme.aut.fox.robotvacuum.virtual.app.simulationapp;

import hu.bme.aut.fox.robotvacuum.virtual.app.App;
import hu.bme.aut.fox.robotvacuum.virtual.app.Simulation;
import hu.bme.aut.fox.robotvacuum.virtual.app.virtualworld.VirtualWorldScreen;
import hu.bme.aut.fox.robotvacuum.virtual.app.world.WorldScreen;
import hu.bme.aut.fox.robotvacuum.world.*;

import javax.swing.*;
import java.awt.*;

public class SimulationAppScreen extends App.Screen {

    private Simulation simulation;


    public SimulationAppScreen(){


        setLayout(new GridLayout(1, 2));


        simulation = new Simulation();
        WorldScreen worldScreen = new WorldScreen(simulation.getRobotVacuum());
        VirtualWorldScreen virtualWorldScreen = new VirtualWorldScreen(simulation.getWorld(), simulation.getRadar(), simulation.getMotor());

        JPanel virtualPanel = new JPanel(new GridBagLayout());
        virtualPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        virtualPanel.setPreferredSize(new Dimension(700,700));
        JPanel robotPanel = new JPanel(new GridBagLayout());
        robotPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        robotPanel.setSize(new Dimension(700,700));

        add(virtualPanel, BorderLayout.EAST);
        add(robotPanel, BorderLayout.WEST);


        //add(worldScreen);
        //add(virtualWorldScreen);
    }

}
