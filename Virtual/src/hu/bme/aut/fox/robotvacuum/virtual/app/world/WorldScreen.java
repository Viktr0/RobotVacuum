package hu.bme.aut.fox.robotvacuum.virtual.app.world;

import hu.bme.aut.fox.robotvacuum.virtual.viewmodel.WorldViewModel;
import hu.bme.aut.fox.robotvacuum.virtual.app.App.Screen;
import hu.bme.aut.fox.robotvacuum.world.*;

import javax.swing.*;
import java.awt.*;

public class WorldScreen extends Screen {

	private static final int fieldSize = 10;
	private WorldViewModel viewModel = new WorldViewModel();
	private Canvas canvas;

	public WorldScreen() {
		canvas = new MyCanvas();
        BoxLayout layout = new BoxLayout(this, BoxLayout.X_AXIS);
        setLayout(layout);

		add(canvas);
	}

	public void paint(Graphics graphics){
        graphics.setColor(Color.LIGHT_GRAY);
        graphics.drawRect(10,10,10,10);
        graphics.fillRect(200,100,90,90);
        graphics.setColor(Color.BLUE);
        graphics.fillRect(290,100,10,10);
        graphics.setColor(Color.BLUE);

        for(int i = 0; i < 100; i++){
            if(i % 2 == 0){
                graphics.setColor(Color.LIGHT_GRAY);
                graphics.fillRect(i * 10,300,10,10);
            }
            else{
                graphics.setColor(Color.BLACK);
                graphics.fillRect(i * 10,300,10,10);
            }
        }
	}

	@Override
	public void onAttach() {
		super.onAttach();
		//subscribe(viewModel.matrixSubject, (matrix) -> {drawNewMatrix(matrix, matrix.length, matrix[0].length);});

	}

	private void drawNewWorld(World world, int N, int M) {
		Graphics graphics = canvas.getGraphics();
		clearCanvas(graphics);
		graphics.drawRect(1, 2, 3, 4);
	}

	private void clearCanvas(Graphics graphics) {
		graphics.setColor(Color.WHITE);
		graphics.fillRect(0, 0, 30, 30);

	}

	private class MyCanvas extends Canvas{

		@Override
		public void paint(Graphics graphics) {
			//super.paint(graphics);
			graphics.setColor(Color.LIGHT_GRAY);
			graphics.drawRect(10,10,10,10);
			graphics.fillRect(200,100,90,90);
			graphics.setColor(Color.BLUE);
			graphics.fillRect(290,100,10,10);
			graphics.setColor(Color.BLUE);

			for(int i = 0; i < 100; i++){
			    if(i % 2 == 0){
			        graphics.setColor(Color.LIGHT_GRAY);
                    graphics.fillRect(i * 10,300,10,10);
                }
			    else{
                    graphics.setColor(Color.BLACK);
                    graphics.fillRect(i * 10,300,10,10);
                }
            }


		}
	}
}
