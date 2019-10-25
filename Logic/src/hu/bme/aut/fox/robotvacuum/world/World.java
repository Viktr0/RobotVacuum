package hu.bme.aut.fox.robotvacuum.world;

import java.util.ArrayList;
import java.util.List;

public class World {

	private final double gridScale;

	private final int width00;
	private final int height00;
	private final Field[][] fields00;

	private final int width01;
	private final int height01;
	private final Field[][] fields01;

	private final int width10;
	private final int height10;
	private final Field[][] fields10;

	private final int width11;
	private final int height11;
	private final Field[][] fields11;

	public World() {
		this(1);
	}

	public World(double gridScale) {
		this.gridScale = gridScale;

		this.width00 = 0;
		this.height00 = 0;
		fields00 = new Field[0][0];

		this.width01 = 0;
		this.height01 = 0;
		fields01 = new Field[0][0];

		this.width10 = 0;
		this.height10 = 0;
		fields10 = new Field[0][0];

		this.width11 = 0;
		this.height11 = 0;
		fields11 = new Field[0][0];
	}

	public World(World parent, Field... fields) {
		gridScale = parent.gridScale;

		int width00 = parent.width00;
		int height00 = parent.height00;

		int width01 = parent.width01;
		int height01 = parent.height01;

		int width10 = parent.width10;
		int height10 = parent.height10;

		int width11 = parent.width11;
		int height11 = parent.height11;

		for (Field field : fields) {
			if (field.getX() >= 0 && field.getY() >= 0) {
				width00 = Math.max(width00, field.getX() + 1);
				height00 = Math.max(height00, field.getY() + 1);
			} else if (field.getX() < 0 && field.getY() >= 0) {
				width01 = Math.max(width01, -field.getX());
				height01 = Math.max(height01, field.getY() + 1);
			} else if (field.getX() >= 0 && field.getY() < 0) {
				width10 = Math.max(width10, field.getX() + 1);
				height10 = Math.max(height10, -field.getY());
			} else if (field.getX() < 0 && field.getY() < 0) {
				width11 = Math.max(width11, -field.getX());
				height11 = Math.max(height11, -field.getY());
			}
		}

		this.width00 = width00;
		this.height00 = height00;
		fields00 = new Field[width00][height00];

		this.width01 = width01;
		this.height01 = height01;
		fields01 = new Field[width01][height01];

		this.width10 = width10;
		this.height10 = height10;
		fields10 = new Field[width10][height10];

		this.width11 = width11;
		this.height11 = height11;
		fields11 = new Field[width11][height11];

		for (int x = 0; x < parent.width00; x++) {
			System.arraycopy(parent.fields00[x], 0, fields00[x], 0, parent.height00);
		}
		for (int x = 0; x < parent.width01; x++) {
			System.arraycopy(parent.fields01[x], 0, fields01[x], 0, parent.height01);
		}
		for (int x = 0; x < parent.width10; x++) {
			System.arraycopy(parent.fields10[x], 0, fields10[x], 0, parent.height10);
		}
		for (int x = 0; x < parent.width11; x++) {
			System.arraycopy(parent.fields11[x], 0, fields11[x], 0, parent.height11);
		}

		for (Field field : fields) {
			if (field.getX() >= 0 && field.getY() >= 0) {
				fields00[field.getX()][field.getY()] = field;
			} else if (field.getX() < 0 && field.getY() >= 0) {
				fields01[-(field.getX() + 1)][field.getY()] = field;
			} else if (field.getX() >= 0 && field.getY() < 0) {
				fields10[field.getX()][-(field.getY() + 1)] = field;
			} else if (field.getX() < 0 && field.getY() < 0) {
				fields11[-(field.getX() + 1)][-(field.getY() + 1)] = field;
			}
		}
	}

	public double getGridScale() {
		return gridScale;
	}

	public Field getField(int x, int y) {
		if (x >= 0 && y >= 0) {
			return x < width00 && y < height00 ? fields00[x][y] : null;
		} else if (x < 0 && y >= 0) {
			x = -x - 1;
			return x < width01 && y < height01 ? fields01[x][y] : null;
		} else if (x >= 0) {
			y = -y - 1;
			return x < width10 && y < height10 ? fields10[x][y] : null;
		} else {
			x = -x - 1;
			y = -y - 1;
			return x < width11 && y < height11 ? fields11[x][y] : null;
		}
	}

	public Field getFieldAt(double x, double y) {
		return getField(
				(int) Math.floor(x / gridScale),
				(int) Math.floor(y / gridScale)
		);
	}

	public List<Field[][]> shallowCopy() {
		List<Field[][]> copy = new ArrayList<>(4);

		copy.set(0, new Field[width00][height00]);
		copy.set(1, new Field[width01][height01]);
		copy.set(2, new Field[width11][height11]);
		copy.set(3, new Field[width10][height10]);

		for (int i = 0; i < width00; ++i)
			if (height00 >= 0) System.arraycopy(fields00[i], 0, copy.get(0)[i], 0, height00);
		for (int i = 0; i < width01; ++i)
			if (height01 >= 0) System.arraycopy(fields01[i], 0, copy.get(1)[i], 0, height01);
		for (int i = 0; i < width11; ++i)
			if (height11 >= 0) System.arraycopy(fields11[i], 0, copy.get(2)[i], 0, height11);
		for (int i = 0; i < width10; ++i)
			if (height10 >= 0) System.arraycopy(fields10[i], 0, copy.get(3)[i], 0, height10);

		return copy;
	}
}
