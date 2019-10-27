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
    private DisplayGraphics myCanvas;



    public VirtualWorldScreen(VirtualWorld virtualWorld) {

        viewModel = new VirtualWorldViewModel(virtualWorld);
        BoxLayout layout = new BoxLayout(this, BoxLayout.X_AXIS);
        setLayout(layout);

        myCanvas = new DisplayGraphics();
        myCanvas.posX = 30;
        myCanvas.posY = 40;
        myCanvas.repaint();
        add(myCanvas);

    }

    public class DisplayGraphics extends Canvas {

        //matrix adatok
        private int posX = 0;
        private int posY = 0;
        private int rows = 0;
        private int columns = 0;
        List<List<VirtualWorldField>> fields;

        public DisplayGraphics(){

            posX = (int) viewModel.getVirtualWorld().getRobotVacuumPosition().x;
            posY = (int) viewModel.getVirtualWorld().getRobotVacuumPosition().y;

            recentPosX = posX;
            recentPosY = posY;

            fields = viewModel.getVirtualWorld().getWorldMatrix();


            rows = viewModel.getVirtualWorld().getSize().N;
            columns = viewModel.getVirtualWorld().getSize().M;
        }

        public void paint(Graphics g) {

            for(int i = 0; i < columns; i++){
                for(int j = 0; j < rows; j++){

                    VirtualWorldField.Status stat = fields.get(j).get(i).status;

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

            //porszivo helyzete
            g.fillRect(baseX + posX * fieldSize, baseY + posY * fieldSize, fieldSize, fieldSize);

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

    @Override
    public void onAttach() {
        super.onAttach();
        subscribe(viewModel.robotVacuum, (position) -> drawRobotVacuum(position));
    }

    @Override
    public void onDetach() {

    }

}
