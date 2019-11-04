package hu.bme.aut.fox.robotvacuum.virtual.app.world;

import hu.bme.aut.fox.robotvacuum.RobotVacuum;
import hu.bme.aut.fox.robotvacuum.virtual.viewmodel.WorldViewModel;
import hu.bme.aut.fox.robotvacuum.virtual.app.App.Screen;
import hu.bme.aut.fox.robotvacuum.world.Field;

import javax.swing.*;
import java.awt.*;


public class WorldScreen extends Screen {

	private int fieldSize = 10;
	private final int baseX = 0;
	private final int baseY = 0;
	private WorldViewModel viewModel;
	private JPanel myCanvas;
	private JButton increaseBtn = new JButton("+");
	private JButton decreaseBtn = new JButton("-");

	private Canvas canvas = new Canvas();



	public WorldScreen(RobotVacuum rv) {

		viewModel = new WorldViewModel(rv);
		setLayout (new BoxLayout (this, BoxLayout.LINE_AXIS));

        increaseBtn.addActionListener((event)-> {viewModel.increaseScalingFactor(); System.out.println(viewModel.getScalingFactor());});
        decreaseBtn.addActionListener((event) -> {viewModel.decreaseScalingFactor(); System.out.println(viewModel.getScalingFactor());});


		myCanvas = new RobotWorldCanvas();
		myCanvas.repaint();
		add(myCanvas);
		add(increaseBtn);
		add(decreaseBtn);
	}

	private void drawWorld(Field[][] fields){
		System.out.println("WorldScreen.drawWorld meghivodott meghivodott");
		myCanvas.repaint();
	}

	public class RobotWorldCanvas extends JPanel{

		private Field[][] fields;

		public RobotWorldCanvas(){}

		@Override
		public void paint(Graphics graphics) {
			System.out.println("RobotWorldCanvas.paint meghivodott");
			super.paint(graphics);
			fields = viewModel.getFields();
			fieldSize = (int) (600/viewModel.getScalingFactor());

			for(int i = 0; i < viewModel.getScalingFactor(); i++){
				for(int j = 0; j < viewModel.getScalingFactor(); j++){
					if(fields[j][i] != null){
						if(fields[j][i].isObstacle()){
							graphics.setColor(Color.BLACK);
						}
						else{
							if(fields[j][i].isCleaned()){
								graphics.setColor(Color.WHITE);
							}
							else{
								graphics.setColor(Color.LIGHT_GRAY);
							}
						}
					}
					else{
						//graphics.setColor(Color.BLACK);
						graphics.setColor(this.getBackground());
					}
					graphics.fillRect(baseX + i * fieldSize, baseY + j * fieldSize, fieldSize, fieldSize);
				}
			}

		}
	}


	@Override
	public void onAttach() {
		super.onAttach();
		subscribe(viewModel.world, (matrix) -> drawWorld(matrix));
	}


}
