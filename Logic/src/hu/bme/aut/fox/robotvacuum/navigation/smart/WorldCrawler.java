package hu.bme.aut.fox.robotvacuum.navigation.smart;

import hu.bme.aut.fox.robotvacuum.world.Field;
import hu.bme.aut.fox.robotvacuum.world.World;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

class FieldNode {

	final FieldNode parent;
	final int depth;
	final Field field;

	private FieldNode(FieldNode parent, Field field) {
		this.parent = parent;
		this.depth = parent == null ? 0 : parent.depth + 1;
		this.field = field;
	}

	static FieldNode findInWorld(
			World world,
			Field startField,
			boolean ignoreUnreachable,
			Function<FieldNode, Boolean> evaluator
	) {
		List<Field> scanned = new LinkedList<>();
		List<FieldNode> edge = new LinkedList<>();

		scanned.add(startField);
		edge.add(new FieldNode(null, startField));
		while (edge.size() > 0) {
			FieldNode node = edge.remove(0);
			if (evaluator.apply(node)) return node;

			Field field = node.field;
			Field[] neighbors = {
					world.getGridField(field.getX() + 1, field.getY()),
					world.getGridField(field.getX() - 1, field.getY()),
					world.getGridField(field.getX(), field.getY() + 1),
					world.getGridField(field.getX(), field.getY() - 1)
			};

			for (Field neighbor : neighbors) {
				if (neighbor != null) {
					if (ignoreUnreachable ? neighbor.isReachable() : !neighbor.isObstacle()) {
						if (!scanned.contains(neighbor)) {
							scanned.add(neighbor);
							edge.add(new FieldNode(node, neighbor));
						}
					}
				}
			}
		}

		return null;
	}
}
