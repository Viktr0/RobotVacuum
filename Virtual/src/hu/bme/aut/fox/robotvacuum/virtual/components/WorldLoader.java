package hu.bme.aut.fox.robotvacuum.virtual.components;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.InvalidParameterException;
import java.util.Scanner;

public class WorldLoader {
	private static final String worldsFolder = "Virtual/src/hu/bme/aut/fox/robotvacuum/virtual/worlds/";
	private final String fileName;

	public WorldLoader(final String file) {
		fileName = file;
	}

	public VirtualWorld load() throws FileNotFoundException {
		Scanner scanner = new Scanner(new FileInputStream(worldsFolder + fileName + ".world"));
		final int N = scanner.nextInt();
		final int M = scanner.nextInt();
		final int posX = scanner.nextInt();
		final int posY = scanner.nextInt();
		scanner.nextLine();
		final VirtualWorldField[][] fields = new VirtualWorldField[N][M];
		for (int j = 0; j < M; j++) {
			String line = scanner.nextLine();
			System.out.println(line);
			for (int i = 0; i < N; ++i) {
				fields[i][j] = charToField(line.charAt(i));
			}
		}
		return new VirtualWorld(fields, N, M, new Position(posX, posY, 0));
	}

	private VirtualWorldField charToField(char c) {
		if (c == '#') return new VirtualWorldField(VirtualWorldField.Status.NOTEMPTY);
		else if (c == ' ') return new VirtualWorldField(VirtualWorldField.Status.DIRTY);
		else throw new InvalidParameterException("Invalid token in world file: " + fileName);
	}
}
