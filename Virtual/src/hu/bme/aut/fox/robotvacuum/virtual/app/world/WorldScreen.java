package hu.bme.aut.fox.robotvacuum.virtual.app.world;

import hu.bme.aut.fox.robotvacuum.RobotVacuum;
import hu.bme.aut.fox.robotvacuum.virtual.viewmodel.WorldViewModel;
import hu.bme.aut.fox.robotvacuum.virtual.app.App.Screen;
import hu.bme.aut.fox.robotvacuum.world.*;

import javax.swing.*;
import java.awt.*;


public class WorldScreen extends Screen {

	private static final int fieldSize = 10;
	private WorldViewModel viewModel;
	private Canvas canvas = new Canvas();
	private RobotVacuum robotVacuum;
	//private Graphics g = getGraphics();

	private JButton increaseBtn = new JButton("+");
	private JButton decreaseBtn = new JButton("-");

	public WorldScreen(RobotVacuum rv) {

		robotVacuum = rv;
		viewModel = new WorldViewModel(robotVacuum);

		setLayout (new BoxLayout (this, BoxLayout.LINE_AXIS));

        increaseBtn.addActionListener((event)-> {viewModel.increaseScalingFactor(); System.out.println(viewModel.getScalingFactor());});
        decreaseBtn.addActionListener((event) -> {viewModel.decreaseScalingFactor(); System.out.println(viewModel.getScalingFactor());});

        canvas.setBackground(Color.WHITE);



		//add(canvas);
		add(increaseBtn);
		add(decreaseBtn);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.setColor(Color.BLUE);
		g.fillRect(0, 0, 100, 100);
	}

	private void drawWorld(Field[][] fields){
		Graphics g = canvas.getGraphics();
		clearCanvas(g);
		int n = viewModel.getScalingFactor();
		for(int i = 0; i < n; i++){
			for(int j = 0; j < n; j++){
				Field field = fields[i][j];
				if(field != null) {
					if (!field.isCleaned()) {
						if (!field.isObstacle()) {
							g.setColor(Color.LIGHT_GRAY);
						} else {
							g.setColor(Color.BLACK);
						}
					} else {
						g.setColor(Color.WHITE);
					}
				}

				g.fillRect(i * fieldSize, j * fieldSize, fieldSize, fieldSize);

			}
		}
		g.setColor(Color.RED);
		g.fillRect(viewModel.getScalingFactor()/2 * fieldSize, viewModel.getScalingFactor()/2 * fieldSize, fieldSize, fieldSize);
	}


	@Override
	public void onAttach() {
		super.onAttach();
		subscribe(viewModel.world, (matrix) -> drawWorld(matrix));
	}

	private void clearCanvas(Graphics graphics) {
		graphics.setColor(Color.WHITE);
		graphics.fillRect(0, 0, 1000, 1000);
	}

}
