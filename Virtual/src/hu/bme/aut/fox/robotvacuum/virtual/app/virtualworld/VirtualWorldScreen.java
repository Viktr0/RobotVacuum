package hu.bme.aut.fox.robotvacuum.virtual.app.virtualworld;

import hu.bme.aut.fox.robotvacuum.hardware.Radar;
import hu.bme.aut.fox.robotvacuum.virtual.components.*;
import hu.bme.aut.fox.robotvacuum.virtual.app.App.Screen;
import hu.bme.aut.fox.robotvacuum.virtual.viewmodel.VirtualWorldViewModel;


import javax.swing.*;
import java.awt.*;

public class VirtualWorldScreen extends Screen {

    private VirtualWorldViewModel viewModel;
    private final int baseX = 0;
    private final int baseY = 0;
    private final int fieldSize = 32;

    private double recentPosX;
    private double recentPosY;
    private double actualPosX = 0;
    private double actualPosY = 0;

    private Radar.RadarData[] radarData = new Radar.RadarData[0];

    private JPanel myCanvas;
    private JButton stepRightBtn;
    private JButton stepLeftBtn;
    private JButton stepUpBtn;
    private JButton stepDownBtn;

    public VirtualWorldScreen(
            VirtualWorld virtualWorld,
            VirtualRadar virtualRadar,
            VirtualMotor virtualMotor
    ) {

        viewModel = new VirtualWorldViewModel(
                virtualWorld,
                virtualRadar,
                virtualMotor
        );

        BoxLayout layout = new BoxLayout(this, BoxLayout.X_AXIS);
        setLayout(layout);

        stepRightBtn = new JButton("Right");
        stepRightBtn.addActionListener((event) -> stepRight());
        stepLeftBtn = new JButton("Left");
        stepLeftBtn.addActionListener((event) -> stepLeft());
        stepUpBtn = new JButton("Up");
        stepUpBtn.addActionListener((event) -> stepUp());
        stepDownBtn = new JButton("Down");
        stepDownBtn.addActionListener((event) -> stepDown());

        myCanvas = new FullWorldCanvas();

        add(myCanvas);
        add(stepUpBtn);
        add(stepDownBtn);
        add(stepLeftBtn);
        add(stepRightBtn);
    }

    public class FullWorldCanvas extends JPanel {

        //matrix adatok
        private int rows = 0;
        private int columns = 0;
        VirtualWorldField[][] fields;

        public FullWorldCanvas(){

            actualPosX = (int) viewModel.getVirtualWorld().getRobotVacuumPosition().y;
            actualPosY = (int) viewModel.getVirtualWorld().getRobotVacuumPosition().x;

            recentPosX = actualPosY;
            recentPosY = actualPosX;

            fields = viewModel.getVirtualWorld().getWorldMatrix();

            columns = viewModel.getVirtualWorld().getSize().N;
            rows = viewModel.getVirtualWorld().getSize().M;
        }

        @Override
        public void paint(Graphics g) {
            System.out.println("coordinates: (" + recentPosX + "," + recentPosY + ")");
            //System.out.println(recentPosY);
            for(int i = 0; i < columns; i++){
                for(int j = 0; j < rows; j++){
                    VirtualWorldField.Status stat = fields[i][j].status;
                    fields[(int) recentPosX][(int) recentPosY].status = VirtualWorldField.Status.CLEAN; //TODO

                    if(stat == VirtualWorldField.Status.DIRTY)
                        g.setColor(Color.LIGHT_GRAY);
                    else if(stat == VirtualWorldField.Status.NOTEMPTY)
                        g.setColor(Color.BLACK);
                    else{
                        g.setColor(Color.WHITE);
                    }

                    g.fillRect(baseX + i * fieldSize, baseY + j * fieldSize, fieldSize, fieldSize);

                }
            }

            g.setColor(Color.RED);
            g.fillRect(
                    (int) (baseX + (actualPosY - 0.5) * fieldSize),
                    (int) (baseY + (actualPosX - 0.5) * fieldSize),
                    fieldSize, fieldSize
            );

            g.setColor(Color.BLUE);
            for (Radar.RadarData data : radarData) {
                double startX = baseX + actualPosY * fieldSize;
                double startY = baseY + actualPosX * fieldSize;
                double rayLength = data.getDistance() * fieldSize;
                g.drawLine(
                        (int) startX,
                        (int) startY,
                        (int) (startX + Math.cos(data.getDirection()) * rayLength),
                        (int) (startY + Math.sin(data.getDirection()) * rayLength)
                );
            }

            recentPosX = actualPosY;
            recentPosY = actualPosX;
        }
    }

    private void setRobotVacuumPos(Position pos) {
        actualPosX = pos.y;
        actualPosY = pos.x;
        myCanvas.repaint();
    }

    private void setRadarData(Radar.RadarData[] data) {
        radarData = data;
        myCanvas.repaint();
    }

    @Override
    public void onAttach() {
        super.onAttach();
        subscribe(viewModel.robotVacuum, this::setRobotVacuumPos);
        subscribe(viewModel.radarData, this::setRadarData);
    }

    @Override
    public void onDetach() {
    }

    //Gombok
    public void stepRight(){
        actualPosY++;
        myCanvas.repaint();
    }
    public void stepLeft(){
        actualPosY--;
        myCanvas.repaint();
    }
    public void stepUp(){
        actualPosX--;
        myCanvas.repaint();
    }
    public void stepDown(){
        actualPosX++;
        myCanvas.repaint();
    }
}
