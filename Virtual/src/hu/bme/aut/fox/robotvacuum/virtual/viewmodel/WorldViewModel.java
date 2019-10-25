package hu.bme.aut.fox.robotvacuum.virtual.viewmodel;

import java.util.ArrayList;

public class WorldViewModel {

//    private InterpretedWorld interpretedWorld;

//	public BehaviorSubject<InterpretedWorldField[][]> matrixSubject;

	public WorldViewModel(){

    }

//	public InterpretedWorldViewModel(InterpretedWorld iW) {
//		interpretedWorld = iW;
//	    //interpretedWorld.addListener(this);
//	}

//    public void setDefaultInterpretedWorld(){
//        List<List<InterpretedWorldField>> fields = new ArrayList<>();
//        for(int i = 0; i < 50; i++){
//            fields.add(new ArrayList<>());
//            for(int j = 0; j < 64; j++){
//                fields.get(i).add(new InterpretedWorldField(DIRTY));
//            }
//        }
//        for (int ii = 0; ii < 50; ii++) {
//            for (int jj = 0; jj < 64; jj++) {
//                if (ii == 0 || ii == 49
//                        || (ii == 16 && jj < 8) || (ii == 16 && jj > 11 && jj < 26) || (ii == 16 && jj > 29 && jj < 47) || (ii == 16 && jj > 50)
//                        || (ii == 34 && jj < 17) || (ii == 34 && jj > 20 && jj < 43) || (ii == 34 && jj > 46)
//                        || jj == 0 || jj == 63
//                        || (jj == 20 && ii < 8) || (jj == 20 && ii > 11 && ii < 16)
//                        || (jj == 40 && ii < 8) || (jj == 40 && ii > 11 && ii < 16)
//                        || (jj == 30 && ii > 34 && ii < 40) || (jj == 30 && ii > 43)) {
//                    fields.get(ii).get(jj).setStatus(NOTEMPTY);
//                }
//            }
//        }
//        interpretedWorld = new InterpretedWorld();
//        interpretedWorld.setNewMatrix(fields, 50, 64);
//        interpretedWorld.setRobotVacuumPosition(new hu.bme.aut.fox.robotvacuum.Position(1, 1, Math.PI * 1.5));
//    }

//	@Override
//	public void worldMatrixChanged(InterpretedWorldField[][] matrix, int N, int M) {
//		matrixSubject.onNext(matrix);
//	}

//	@Override
//	public void positionChanged(Position position) {
//
//	}
}
