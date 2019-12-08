package hu.bme.aut.fox.robotvacuum.virtual.components;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

public class ContinuousWorldLoader {
	private static final String worldsFolder = "Virtual/src/hu/bme/aut/fox/robotvacuum/virtual/worlds/";
	private final String fileName;
	private int lines = 0;

	public ContinuousWorldLoader(final String file) {
		fileName = file;
	}

	public ContinuousWorld load() throws FileNotFoundException {
		Scanner scanner = new Scanner(new FileInputStream(worldsFolder + fileName + ".world"));
        Locale locale = Locale.ENGLISH;
        // set the locale to the scanner object
        scanner.useLocale(locale);
        try {
			final int width = scanner.nextInt();
			final int height = scanner.nextInt();
			final int posX = scanner.nextInt();
			final int posY = scanner.nextInt();
			final double dir = scanner.nextDouble();

			List<ContinuousWorld.WorldObject> worldObjects = new LinkedList<>();

			final int numOfObjects = scanner.nextInt();
			lines = 3;
			for (int i = 0; i < numOfObjects; ++i)
				worldObjects.add(nextObject(scanner));

			return new ContinuousWorld(
				width,
				height,
				worldObjects.toArray(new ContinuousWorld.WorldObject[0]),
				new Position(posX, posY, dir));

		}
        catch(InputMismatchException e) {
        	throw e;
		}
        catch(Throwable e) {
        	throw new InputMismatchException("Invalid format for width / height / posX / posY / dir / number of objects");
		}
	}
	
	private ContinuousWorld.WorldObject nextObject(Scanner scanner) {
		try {
			scanner.nextLine();
			lines++;
			final int numOfVertexes = scanner.nextInt();
			List<ContinuousWorld.Coordinate> vertices = new LinkedList<>();
			for (int i = 0; i< numOfVertexes; ++i)
				vertices.add(nextVertex(scanner));

			return new ContinuousWorld.WorldObject(vertices.toArray(new ContinuousWorld.Coordinate[0]));
		}
		catch(InputMismatchException e) {
			throw e;
		}
		catch(Throwable e) {
			throw new InputMismatchException("Invalid format for object on line " + (lines + 1));
		}
	}

	private ContinuousWorld.Coordinate nextVertex(Scanner scanner) {
		try {
			scanner.nextLine();
			lines++;
			final double x = scanner.nextDouble();
			final double y = scanner.nextDouble();
			return new ContinuousWorld.Coordinate(x, y);
		}
		catch(Throwable e) {
			throw new InputMismatchException("Invalid format for vertex on line " + (lines + 1));
		}
	}


}
