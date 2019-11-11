package hu.bme.aut.fox.robotvacuum.virtual.app.combined;

import hu.bme.aut.fox.robotvacuum.RobotVacuum;
import hu.bme.aut.fox.robotvacuum.hardware.Radar;
import hu.bme.aut.fox.robotvacuum.navigation.Navigator;
import hu.bme.aut.fox.robotvacuum.virtual.app.App;
import hu.bme.aut.fox.robotvacuum.virtual.components.*;
import hu.bme.aut.fox.robotvacuum.virtual.viewmodel.VirtualWorldViewModel;
import hu.bme.aut.fox.robotvacuum.virtual.viewmodel.WorldViewModel;
import hu.bme.aut.fox.robotvacuum.world.Field;

import javax.swing.*;
import java.awt.*;

public class CombinedScreen extends App.Screen {
    private VirtualWorldViewModel virtualWorldViewModel;
    private WorldViewModel worldViewModel;
    private JPanel combinedCanvas;

    //porszivo adatai
    private double actualPosX;
    private double actualPosY;
    private double recentPosX;
    private double recentPosY;
    private Radar.RadarData[] radarData = new Radar.RadarData[0];

    //kép adatai
    private int fieldSize;
    private final int baseX = 0;
    private final int baseY = 0;

    //virtualis vilag adatai
    private int vwBaseX;
    private int vwBaseY;

    //vilag adatai
    private int wBaseX;
    private int wBaseY;


    public CombinedScreen(VirtualWorld virtualWorld, VirtualRadar virtualRadar, VirtualMotor virtualMotor, RobotVacuum robotVacuum){
        worldViewModel = new WorldViewModel(robotVacuum);
        virtualWorldViewModel = new VirtualWorldViewModel(
                virtualWorld,
                virtualRadar,
                virtualMotor
        );
        BoxLayout layout = new BoxLayout(this, BoxLayout.X_AXIS);
        setLayout(layout);
        combinedCanvas = new CombinedCanvas();
        add(combinedCanvas);
    }


    public class CombinedCanvas extends JPanel{
        private Field[][] fields;
        private VirtualWorldField[][] virtualFields;
        private int rows = 0;
        private int columns = 0;
        private int SF;

        public CombinedCanvas(){
            actualPosX = (int) virtualWorldViewModel.getVirtualWorld().getRobotVacuumPosition().x;
            actualPosY = (int) virtualWorldViewModel.getVirtualWorld().getRobotVacuumPosition().y;
            recentPosX = actualPosX;
            recentPosY = actualPosY;

            virtualFields = virtualWorldViewModel.getVirtualWorld().getWorldMatrix();
            columns = virtualWorldViewModel.getVirtualWorld().getSize().N;
            rows = virtualWorldViewModel.getVirtualWorld().getSize().M;

            int rp, rn, cp, cn;
            rp = rows-(int)actualPosY;
            rn = (int)actualPosY;
            cp = columns-(int)actualPosX;
            cn = (int)actualPosX;
            int r = (rp > rn)? rp : rn;
            int c = (cn > cp)? cn : cp;
            worldViewModel.setScalingFactor(2 * ((r > c)? r : c));
            SF = worldViewModel.getScalingFactor();

            fieldSize = 700/SF;
            vwBaseX = (SF/2 - (int)actualPosX) * fieldSize;
            vwBaseY = (SF/2 - (int)actualPosY) * fieldSize;
            wBaseX = baseX;
            wBaseY = baseY;
        }

        @Override
        public void paint(Graphics graphics) {
            super.paint(graphics);
            fields = worldViewModel.getFields();

            //Az alap
            for (int i = 0; i < columns; i++) {
                for (int j = 0; j < rows; j++) {
                    virtualFields[(int) recentPosX][(int) recentPosY].status = VirtualWorldField.Status.CLEAN; //TODO
                    VirtualWorldField.Status stat = virtualFields[i][j].status;
                    if (stat == VirtualWorldField.Status.DIRTY)
                        graphics.setColor(Color.LIGHT_GRAY);
                    else if (stat == VirtualWorldField.Status.NOTEMPTY)
                        graphics.setColor(Color.BLACK);
                    else {
                        graphics.setColor(Color.WHITE);
                    }
                    graphics.fillRect(vwBaseX + i * fieldSize, vwBaseY + j * fieldSize, fieldSize, fieldSize);
                }
            }

            //Robot kepe
            for (int i = 0; i < SF; i++) {
                for (int j = 0; j < SF; j++) {
                    if (fields[j][i] != null) {
                        if (fields[j][i].isObstacle()) {
                            graphics.setColor(Color.BLUE);
                        } else {
                            if (fields[j][i].isCleaned()) {
                                graphics.setColor(Color.WHITE);
                            } else {
                                graphics.setColor(new Color(127, 160, 255));
                            }
                        }
                        graphics.fillRect(baseX + i * fieldSize, baseY + j * fieldSize, fieldSize, fieldSize);
                    } else {
                    }
                }
            }

            //A megcelzott utvonal
            int worldSize = worldViewModel.getScalingFactor();

            int prevX = 0;
            int prevY = 0;
            boolean first = true;

            graphics.setColor(new Color(127, 255, 200));
            for (Navigator.Target target : worldViewModel.getTargets()) {
                int x = (int) ((worldSize / 2 + target.getX()) * fieldSize);
                int y = (int) ((worldSize / 2 + target.getY()) * fieldSize);

                if (!first) {
                    graphics.drawLine(x, y, prevX, prevY);
                }

                prevX = x;
                prevY = y;
                first = false;
            }

            graphics.setColor(Color.GREEN);
            if (!first) {
                graphics.drawRect(
                        prevX - fieldSize / 2,
                        prevY - fieldSize / 2,
                        fieldSize,
                        fieldSize
                );
            }

            //A porszivo
            graphics.setColor(Color.DARK_GRAY);
            graphics.fillRect(
                    (int) (vwBaseX + (actualPosX - 0.5) * fieldSize),
                    (int) (vwBaseY + (actualPosY - 0.5) * fieldSize),
                    fieldSize, fieldSize
            );

            //A radar
            graphics.setColor(Color.GRAY);
            for (Radar.RadarData data : radarData) {
                double startX = vwBaseX + actualPosX * fieldSize;
                double startY = vwBaseY + actualPosY * fieldSize;
                double rayLength = data.getDistance() * fieldSize;
                graphics.drawLine(
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
        combinedCanvas.repaint();
    }

    public void setRadarData(Radar.RadarData[] data) {
        radarData = data;
        combinedCanvas.repaint();
    }

    public void drawWorld(Field[][] fields) {
        System.out.println("WorldScreen.drawWorld meghivodott meghivodott");
        combinedCanvas.repaint();
    }

    @Override
    public void onAttach() {
        super.onAttach();
        subscribe(virtualWorldViewModel.robotVacuum, this::setRobotVacuumPos);
        subscribe(virtualWorldViewModel.radarData, this::setRadarData);
        subscribe(worldViewModel.world, (matrix) -> drawWorld(matrix));
    }



}
