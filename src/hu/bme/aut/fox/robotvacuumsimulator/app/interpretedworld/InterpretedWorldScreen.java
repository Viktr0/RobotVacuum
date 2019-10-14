package hu.bme.aut.fox.robotvacuumsimulator.app.interpretedworld;

import hu.bme.aut.fox.robotvacuum.components.interpretedworld.InterpretedWorldField;
import hu.bme.aut.fox.robotvacuumsimulator.app.App;
import hu.bme.aut.fox.robotvacuumsimulator.viewmodel.InterpretedWorldViewModel;

import java.awt.*;

public class InterpretedWorldScreen extends App.Screen {
	private static final int fieldSize = 8;
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
