package hu.bme.aut.fox.robotvacuum.virtual.app.virtualworld;

import hu.bme.aut.fox.robotvacuum.virtual.components.Position;
import hu.bme.aut.fox.robotvacuum.virtual.components.VirtualWorld;
import hu.bme.aut.fox.robotvacuum.virtual.components.VirtualWorldField;
import hu.bme.aut.fox.robotvacuum.virtual.app.App.Screen;
import hu.bme.aut.fox.robotvacuum.virtual.viewmodel.VirtualWorldViewModel;


import javax.swing.*;
import java.awt.*;
import java.util.List;


public class VirtualWorldScreen extends Screen {

    private VirtualWorldViewModel viewModel;
    private int baseX = 50;
    private int baseY = 100;
    private int fieldSize = 10;
    private int recentPosX;
    private int recentPosY;
    private int actualPosX = 0;
    private int actualPosY = 0;
    private DisplayGraphics myCanvas;
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

        myCanvas = new DisplayGraphics();

        add(myCanvas);
        add(stepUpBtn);
        add(stepDownBtn);
        add(stepLeftBtn);
        add(stepRightBtn);
    }




    public class DisplayGraphics extends Canvas {

        //matrix adatok
        private int rows = 0;
        private int columns = 0;
        List<List<VirtualWorldField>> fields;

        public DisplayGraphics(){

            actualPosX = (int) viewModel.getVirtualWorld().getRobotVacuumPosition().x;
            actualPosY = (int) viewModel.getVirtualWorld().getRobotVacuumPosition().y;

            recentPosX = actualPosX;
            recentPosY = actualPosY;

            fields = viewModel.getVirtualWorld().getWorldMatrix();


            rows = viewModel.getVirtualWorld().getSize().N;
            columns = viewModel.getVirtualWorld().getSize().M;
        }

        public void paint(Graphics g) {
            //System.out.println(recentPosX);
            //System.out.println(recentPosY);
            for(int i = 0; i < columns; i++){
                for(int j = 0; j < rows; j++){
                    VirtualWorldField.Status stat = fields.get(j).get(i).status;
                    fields.get(recentPosY).get(recentPosX).status = VirtualWorldField.Status.CLEAN;

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
            g.fillRect(baseX + actualPosX * fieldSize, baseY + actualPosY * fieldSize, fieldSize, fieldSize);
            recentPosX = actualPosX;
            recentPosY = actualPosY;
        }
    }

    public void drawRobotVacuum(Position pos){
        Graphics g = getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(baseX + recentPosX * fieldSize, baseY + recentPosY * fieldSize, fieldSize, fieldSize);
        g.setColor(Color.RED);
        g.fillRect(baseX + (int)pos.x * fieldSize, baseY + (int)pos.y * fieldSize, fieldSize, fieldSize);
        recentPosX = (int)pos.x;
        recentPosY = (int)pos.y;
    }

    public void setRobotVacuumPos(Position pos){
        actualPosX = (int)pos.x;
        actualPosY = (int)pos.y;
        myCanvas.repaint();
        add(myCanvas);
    }

    @Override
    public void onAttach() {
        super.onAttach();
        //subscribe(viewModel.robotVacuum, (position) -> drawRobotVacuum(position));
        //subscribe(viewModel.robotVacuum, (position) -> setRobotVacuumPos(position));
        subscribe(viewModel.robotVacuum, (position) -> myCanvas.repaint());
    }

    @Override
    public void onDetach() {

    }
    public void stepRight(){
        actualPosX++;
        myCanvas.repaint();
        add(myCanvas);
    }
    public void stepLeft(){
        actualPosX--;
        myCanvas.repaint();
        add(myCanvas);
    }
    public void stepUp(){
        actualPosY--;
        myCanvas.repaint();
        add(myCanvas);
    }
    public void stepDown(){
        actualPosY++;
        myCanvas.repaint();
        add(myCanvas);
    }

}
