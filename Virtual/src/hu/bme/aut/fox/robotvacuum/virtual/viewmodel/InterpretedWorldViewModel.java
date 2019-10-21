package hu.bme.aut.fox.robotvacuum.virtual.viewmodel;

import hu.bme.aut.fox.robotvacuum.Position;
import hu.bme.aut.fox.robotvacuum.components.world.InterpretedWorld;
import hu.bme.aut.fox.robotvacuum.components.world.InterpretedWorldField;
import io.reactivex.subjects.BehaviorSubject;

public class InterpretedWorldViewModel implements InterpretedWorld.InterpretedWorldListener {
	public BehaviorSubject<InterpretedWorldField[][]> matrixSubject;


	public InterpretedWorldViewModel(final InterpretedWorld world) {
		world.addListener(this);
	}

	@Override
	public void worldMatrixChanged(InterpretedWorldField[][] matrix, int N, int M) {
		matrixSubject.onNext(matrix);
	}

	@Override
	public void positionChanged(Position position) {

	}
}
