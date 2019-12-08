package hu.bme.aut.fox.robotvacuum.virtual.app.virtualworld;

import hu.bme.aut.fox.robotvacuum.virtual.components.*;
import hu.bme.aut.fox.robotvacuum.virtual.app.App.Screen;
import hu.bme.aut.fox.robotvacuum.virtual.viewmodel.VirtualWorldViewModel;


import javax.swing.*;
import java.awt.*;

public class VirtualWorldScreen extends Screen {
    private VirtualWorldPanel virtualWorldPanel;

    public VirtualWorldScreen(ContinuousWorld virtualWorld, ContinuousRadar radar, ContinuousMotor virtualMotor){
        virtualWorldPanel = new VirtualWorldPanel(virtualWorld, radar, virtualMotor);
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        add(virtualWorldPanel);
    }


    public static class VirtualWorldPanel extends JPanel {


        private VirtualWorldViewModel viewModel;
        private final int baseX = 0;
        private final int baseY = 0;
        private final int fieldSize = 20;

        private double recentPosX;
        private double recentPosY;
        private double actualPosX = 0;
        private double actualPosY = 0;

        private ContinuousRadar.RadarData[] radarData = new ContinuousRadar.RadarData[0];


        private JPanel myCanvas;

        public VirtualWorldPanel(
            ContinuousWorld virtualWorld,
            ContinuousRadar radar,
            ContinuousMotor virtualMotor
        ) {
            radarData = radar.getRadarData();
            viewModel = new VirtualWorldViewModel(
                virtualWorld,
                radar,
                virtualMotor
            );

            setPreferredSize(new Dimension(700,700));
            BoxLayout layout = new BoxLayout(this, BoxLayout.X_AXIS);
            setLayout(layout);

            myCanvas = new FullWorldCanvas();
            add(myCanvas);
        }

        public VirtualWorldViewModel getViewModel(){
            return viewModel;
        }

        public class FullWorldCanvas extends JPanel {

            //matrix adatok
            private int rows = 0;
            private int columns = 0;
            ContinuousWorld.WorldObject[] objects;

            public FullWorldCanvas() {

                actualPosX = (int) viewModel.getVirtualWorld().getRobotVacuum().x;
                actualPosY = (int) viewModel.getVirtualWorld().getRobotVacuum().y;

                recentPosX = actualPosX;
                recentPosY = actualPosY;

                objects = viewModel.getVirtualWorld().getObjects();
                int max = 0;
                int len = objects.length;
                for (int i = 0; i < len; i++) {
                    if (objects[i].getVertices().length > max)
                        max = objects[i].getVertices().length;
                }
                System.out.println(objects.length);

                columns = (int) viewModel.getVirtualWorld().getWidth();
                rows = (int) viewModel.getVirtualWorld().getHeight();
            }

            @Override
            public void paint(Graphics g) {
                super.paint(g);
                for (int i = 0; i < objects.length; i++) {
                    ContinuousWorld.Coordinate[] coos = objects[i].getVertices();
                    int[] xCoos = new int[coos.length];
                    int[] yCoos = new int[coos.length];
                    for(int j = 0; j < coos.length; j++) {
                        xCoos[j] = (int)(coos[j].getX() * fieldSize + fieldSize);
                        yCoos[j] = (int)(coos[j].getY() * fieldSize + fieldSize);
                    }
                    g.setColor(Color.BLACK);
                    g.fillPolygon(new Polygon(xCoos, yCoos, coos.length));
                    //    fields[(int) recentPosX][(int) recentPosY].status = VirtualWorldField.Status.CLEAN; //TODO


                }


                g.setColor(Color.RED);
                g.fillOval((int) (baseX + (actualPosX - 0.5) * fieldSize),
                        (int) (baseY + (actualPosY- 0.5) * fieldSize),
                        fieldSize, fieldSize);


                g.setColor(Color.BLUE);
                for (ContinuousRadar.RadarData data : radarData) {
                    double startX = baseX + actualPosX * fieldSize;
                    double startY = baseY + actualPosY * fieldSize;
                    double rayLength = data.getDistance() * fieldSize;
                    g.drawLine(
                            (int) startX,
                            (int) startY,
                            (int) (startX + Math.cos(data.getDirection()) * rayLength),
                            (int) (startY + Math.sin(data.getDirection()) * rayLength)
                    );
                }

                recentPosX = actualPosX;
                recentPosY = actualPosY;
            }
        }

        public void setRobotVacuumPos(Position pos) {
            actualPosX = pos.x;
            actualPosY = pos.y;

            myCanvas.repaint();
        }

        public void setRadarData(ContinuousRadar.RadarData[] data) {
            radarData = data;
            myCanvas.repaint();
        }
    }

    @Override
    public void onAttach() {
        super.onAttach();
        subscribe(virtualWorldPanel.viewModel.robotVacuum, virtualWorldPanel::setRobotVacuumPos);
        //subscribe(virtualWorldPanel.viewModel.radarData, virtualWorldPanel::setRadarData);
    }


}