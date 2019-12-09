package hu.bme.aut.fox.robotvacuum.navigation;

import hu.bme.aut.fox.robotvacuum.RobotVacuum;
import hu.bme.aut.fox.robotvacuum.world.Field;
import hu.bme.aut.fox.robotvacuum.world.World;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

public class SmartNavigator implements Navigator {

	@Override
	public Target[] getTargetPath(World world, RobotVacuum.State state) {
		Field onField = world.getField(state.getPositionX(), state.getPositionY());
		if (onField == null) return null;

		Node targetNode = findNode(world, onField, true, node -> !node.field.isCleaned());

		if (targetNode != null) {
			Field targetField = targetNode.field;
			if (targetField.isObstacle() || !targetField.isReachable()) {
				targetNode = null;
			}
		}

		List<Target> path = new LinkedList<>();
		while (targetNode != null) {
			path.add(0, new Target(
					world.toWorldCoordinate(targetNode.field.getX()) + world.getGridScale() / 2,
					world.toWorldCoordinate(targetNode.field.getY()) + world.getGridScale() / 2
			));
			targetNode = targetNode.parent;
		}

		return path.toArray(new Target[0]);
	}

	private Node findNode(
			World world,
			Field startField,
			boolean ignoreUnreachable,
			Function<Node, Boolean> evaluator
	) {
		List<Field> scanned = new LinkedList<>();
		List<Node> edge = new LinkedList<>();

		scanned.add(startField);
		edge.add(new Node(null, startField));
		while (edge.size() > 0) {
			Node node = edge.remove(0);
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
							edge.add(new Node(node, neighbor));
						}
					}
				}
			}
		}

		return null;
	}

	private static final class Node {

		private final Node parent;
		private final int depth;
		private final Field field;

		private Node(Node parent, Field field) {
			this.parent = parent;
			this.depth = parent == null ? 0 : parent.depth + 1;
			this.field = field;
		}
	}
}
