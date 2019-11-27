package hu.bme.aut.fox.robotvacuum.virtual.components;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class ContinuousWorldLoader {
	private static final String worldsFolder = "Virtual/src/hu/bme/aut/fox/robotvacuum/virtual/worlds/";
	private final String fileName;

	public ContinuousWorldLoader(final String file) {
		fileName = file;
	}

	public ContinuousWorld load() throws FileNotFoundException {
		Scanner scanner = new Scanner(new FileInputStream(worldsFolder + fileName + ".world"));
		final int width = scanner.nextInt();
		final int height = scanner.nextInt();
		final int posX = scanner.nextInt();
		final int posY = scanner.nextInt();
		final double dir = scanner.nextDouble();

		List<ContinuousWorld.WorldObject> worldObjects = new LinkedList<>();
		
		final int numOfObjects = scanner.nextInt();
		for (int i = 0; i < numOfObjects; ++i)
			worldObjects.add(nextObject(scanner));
		
		return new ContinuousWorld(
			width,
			height,
			worldObjects.toArray(new ContinuousWorld.WorldObject[0]), 
			new Position(posX, posY, dir));
	}
	
	private ContinuousWorld.WorldObject nextObject(Scanner scanner) {
		scanner.nextLine();
		final int numOfVertexes = scanner.nextInt();
		List<ContinuousWorld.Coordinate> vertices = new LinkedList<>();
		for (int i = 0; i< numOfVertexes; ++i)
			vertices.add(nextVertex(scanner));
		
		return new ContinuousWorld.WorldObject(vertices.toArray(new ContinuousWorld.Coordinate[0]));
	}

	private ContinuousWorld.Coordinate nextVertex(Scanner scanner) {
		scanner.nextLine();
		final double x = scanner.nextDouble();
		final double y = scanner.nextDouble();
		return new ContinuousWorld.Coordinate(x, y);
	}


}
