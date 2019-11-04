package hu.bme.aut.fox.robotvacuum.virtual.app.virtualworld;

import hu.bme.aut.fox.robotvacuum.virtual.components.Position;
import hu.bme.aut.fox.robotvacuum.virtual.components.VirtualWorld;
import hu.bme.aut.fox.robotvacuum.virtual.components.VirtualWorldField;
import hu.bme.aut.fox.robotvacuum.virtual.app.App.Screen;
import hu.bme.aut.fox.robotvacuum.virtual.viewmodel.VirtualWorldViewModel;


import javax.swing.*;
import java.awt.*;

public class VirtualWorldScreen extends Screen {

    private VirtualWorldViewModel viewModel;
    private final int baseX = 0;
    private final int baseY = 0;
    private final int fieldSize = 10;
    private int recentPosX;
    private int recentPosY;
    private int actualPosX = 0;
    private int actualPosY = 0;
    private JPanel myCanvas;
    private JButton stepRightBtn;
    private JButton stepLeftBtn;
    private JButton stepUpBtn;
    private JButton stepDownBtn;


    public VirtualWorldScreen(VirtualWorld virtualWorld) {

        viewModel = new VirtualWorldViewModel(virtualWorld);
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
                    fields[recentPosX][recentPosY].status = VirtualWorldField.Status.CLEAN; //TODO

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
            g.fillRect(baseX + actualPosY * fieldSize, baseY + actualPosX * fieldSize, fieldSize, fieldSize);
            recentPosX = actualPosY;
            recentPosY = actualPosX;
        }
    }

    public void setRobotVacuumPos(Position pos){
        actualPosX = (int)pos.y;
        actualPosY = (int)pos.x;
        myCanvas.repaint();
    }

    @Override
    public void onAttach() {
        super.onAttach();
        subscribe(viewModel.robotVacuum, (position) -> setRobotVacuumPos(position));
        //subscribe(viewModel.robotVacuum, (position) -> myCanvas.repaint());
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
