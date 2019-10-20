package hu.bme.aut.fox.robotvacuum.virtual.app.world;

import hu.bme.aut.fox.robotvacuum.virtual.components.VirtualWorldField;
import hu.bme.aut.fox.robotvacuumsimulator.app.Screen;
import hu.bme.aut.fox.robotvacuum.virtual.viewmodel.WorldViewModel;


import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;


public class WorldScreen extends Screen {

    private WorldViewModel viewModel = new WorldViewModel();

    //proba
    private final JPanel gui = new JPanel();
    private JButton[][] firstMapSquares = new JButton[50][64];
    private JPanel firstMap;



    public WorldScreen() {


    /*
        //proba
        firstMap = new JPanel(new GridLayout(50, 64));
        firstMap.setBorder(new LineBorder(Color.BLACK));
        gui.add(firstMap);
        Insets buttonMargin = new Insets(0,0,0,0);
        for (int ii = 0; ii < 50; ii++) {
            for (int jj = 0; jj < 64; jj++) {
                JButton b = new JButton();
                b.setPreferredSize(new Dimension(10,10));
                b.setMargin(buttonMargin);
                ImageIcon icon = new ImageIcon(
                        new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB));
                b.setIcon(icon);
                if (ii == 0 || ii == 49
                        || (ii == 16 && jj < 8) || (ii == 16 && jj > 11 && jj < 26 ) || (ii == 16 && jj > 29 && jj < 47) || (ii == 16 && jj > 50)
                        || (ii == 34 && jj < 17) || (ii == 34 && jj > 20 && jj < 43) || (ii == 34 && jj > 46)
                        || jj == 0 || jj == 63
                        || (jj == 20 && ii < 8 ) || (jj == 20 && ii > 11 && ii <16 )
                        || (jj == 40 && ii < 8 ) || (jj == 40 && ii > 11 && ii <16 )
                        || (jj == 30 && ii > 34 && ii < 40) || (jj == 30 && ii > 43)) {
                    b.setBackground(Color.BLACK);
                }
                else {
                    b.setBackground(Color.WHITE);
                }
                firstMapSquares[ii][jj] = b;
            }
        }
        for (int ii = 0; ii < 50; ii++) {
            for (int jj = 0; jj < 64; jj++) {
                        firstMap.add(firstMapSquares[ii][jj]);
            }
        }
    */

        viewModel.setDefaultWorld();
        int posX = (int) viewModel.getVirtualWorld().getRobotVacuumPosition().x;
        int posY = (int) viewModel.getVirtualWorld().getRobotVacuumPosition().y;

        List<List<VirtualWorldField>> fields = viewModel.getVirtualWorld().getWorldMatrix();


        int rows = viewModel.getVirtualWorld().getSize().N;
        int columns = viewModel.getVirtualWorld().getSize().M;


        firstMap = new JPanel(new GridLayout(rows, columns));
        firstMap.setBorder(new LineBorder(Color.BLACK));
        gui.add(firstMap);
        Insets buttonMargin = new Insets(0,0,0,0);

        for (int i = 0; i < rows; i++){
            for (int j = 0; j < columns; j++ ){

                JButton b = new JButton();
                b.setPreferredSize(new Dimension(10,10));
                b.setMargin(buttonMargin);

                ImageIcon icon = new ImageIcon(new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB));
                b.setIcon(icon);

                VirtualWorldField.Status stat = fields.get(i).get(j).status;
                if(stat == VirtualWorldField.Status.DIRTY)
                    b.setBackground(Color.LIGHT_GRAY);
                else if(stat == VirtualWorldField.Status.NOTEMPTY)
                    b.setBackground(Color.BLACK);
                else{ b.setBackground(Color.WHITE);}

                firstMapSquares[i][j] = b;
                firstMap.add(firstMapSquares[i][j]);
            }
        }

        firstMapSquares[posX][posY].setBackground(Color.RED);

        add(gui);
        add(firstMap);
        setVisible(true);

    }

    @Override
    public void onAttach() {



    }

    @Override
    public void onDetach() {

    }
}
