package hu.bme.aut.fox.robotvacuum.virtual.app.interpretedworld;

import hu.bme.aut.fox.robotvacuum.components.world.InterpretedWorldField;
import hu.bme.aut.fox.robotvacuum.virtual.viewmodel.InterpretedWorldViewModel;
import hu.bme.aut.fox.robotvacuumsimulator.app.Screen;

import java.awt.*;

public class InterpretedWorldScreen extends Screen {
	private static final int fieldSize = 10;
	InterpretedWorldViewModel viewModel;
	Canvas canvas;
	public InterpretedWorldScreen() {
		canvas = new Canvas();
		add(canvas);
	}

	@Override
	public void onAttach() {
		super.onAttach();
		subscribe(viewModel.matrixSubject, (matrix) -> {drawNewMatrix(matrix, matrix.length, matrix[0].length);});

	}

	private void drawNewMatrix(InterpretedWorldField[][] matrix, int N, int M) {
		Graphics graphics = canvas.getGraphics();
		clearCanvas(graphics);
		graphics.drawRect(1, 2, 3, 4);
	}

	private void clearCanvas(Graphics graphics) {
		graphics.setColor(Color.WHITE);
		graphics.fillRect(0, 0, 30, 30);

	}
}
