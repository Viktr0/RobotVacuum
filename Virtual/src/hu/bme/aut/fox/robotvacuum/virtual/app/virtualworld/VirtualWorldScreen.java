package hu.bme.aut.fox.robotvacuum.virtual.app.virtualworld;

import hu.bme.aut.fox.robotvacuum.virtual.components.VirtualWorldField;
import hu.bme.aut.fox.robotvacuum.virtual.app.App.Screen;
import hu.bme.aut.fox.robotvacuum.virtual.viewmodel.VirtualWorldViewModel;


import javax.swing.*;
import java.awt.*;
import java.util.List;


public class VirtualWorldScreen extends Screen {

    private VirtualWorldViewModel viewModel = new VirtualWorldViewModel();


    //proba
    private final JPanel gui = new JPanel();
    private JButton[][] firstMapSquares = new JButton[50][64];
    private JPanel firstMap;



    public VirtualWorldScreen() {
/*
        viewModel.setDefaultWorld();
        int posX = (int) viewModel.getVirtualWorld().getRobotVacuumPosition().x;
        int posY = (int) viewModel.getVirtualWorld().getRobotVacuumPosition().y;

        List<List<VirtualWorldField>> fields = viewModel.getVirtualWorld().getWorldMatrix();


        int rows = viewModel.getVirtualWorld().getSize().N;
        int columns = viewModel.getVirtualWorld().getSize().M;
*/

        BoxLayout layout = new BoxLayout(this, BoxLayout.X_AXIS);
        setLayout(layout);
        Canvas myCanvas = new DisplayGraphics();
        add(myCanvas);




    }

    public class DisplayGraphics extends Canvas {

        //matrix adatok
        private int baseX = 50;
        private int baseY = 100;
        private int fieldSize = 10;
        private int posX = 0;
        private int posY = 0;
        private int rows = 0;
        private int columns = 0;
        List<List<VirtualWorldField>> fields;

        public DisplayGraphics(){

            viewModel.setDefaultWorld();
            posX = (int) viewModel.getVirtualWorld().getRobotVacuumPosition().x;
            posY = (int) viewModel.getVirtualWorld().getRobotVacuumPosition().y;

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

                    /*if(i % 2 == 0){
                        g.setColor(Color.BLACK);
                        g.fillRect(baseX + i * fieldSize, baseY + j * fieldSize, fieldSize, fieldSize);
                    }
                    else {
                        g.setColor(Color.LIGHT_GRAY);
                        g.fillRect(baseX + i * fieldSize, baseY + j * fieldSize, fieldSize, fieldSize);
                    }

                     */
                }
            }
            g.setColor(Color.RED);

            //porszivo helyzete
            g.fillRect(baseX + posX * fieldSize, baseY + posY * fieldSize, fieldSize, fieldSize);

        }
    }

    @Override
    public void onAttach() {
        super.onAttach();


    }

    @Override
    public void onDetach() {

    }
}
