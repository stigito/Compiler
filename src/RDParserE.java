import java.util.*;

public class RDParserE {

private class SyntaxErrorException extends Exception {
    SyntaxErrorException(String message) {
        super(message);
    }
}

private class Node extends ArrayList<Node> {
    private final String name;
    public Node(String name, Node... children) {
        this.name = Objects.requireNonNull(name);
        addAll(Arrays.asList(children));
    }
    @Override public String toString() {
        return isEmpty()? name: name + super.toString();
    }
    void prettyPrint() {
        prettyPrint(1);
    }
    void prettyPrint(int depth) {
        System.out.printf("%" + (depth * 4) + "s%s%n", "", name);
        forEach(child -> child.prettyPrint(depth + 1));
    }
}

public static void main(String... args) throws SyntaxErrorException {
	Node parseTree = new RDParserE().parse(args[0]);
	System.out.println(parseTree);  // Parsebaum in einer Zeile
	parseTree.prettyPrint();        // Parsebaum gekippt, mehrzeilig
}

public RDParserE() {}

public Node parse(String input) throws SyntaxErrorException {

	Node startNode = new Node("E");
	String newInput = createChilds(startNode, input);
	if(newInput.length() > 0) {
		throw new SyntaxErrorException("Expected nothing, but was " + newInput);
	};
	return startNode;
}

private String createChilds(Node node, String input) throws SyntaxErrorException {
	if(node.name.toLowerCase().equals(node.name)) {
		if(input.startsWith(node.name)) {
			return input.substring(1);
		}
		else {
			throw new SyntaxErrorException("Expected (one of) " + node.name + ", but was " + input.substring(0, 1));
		}
	}

	if(node.name.equals("E")) {
		if(input.startsWith("(")) {
			node.add(new Node("("));
			node.add(new Node("E"));
			node.add(new Node("O"));
			node.add(new Node("E"));
			node.add(new Node(")"));
		}
		else if(input.startsWith("-") || input.startsWith("n")) {
			node.add(new Node("F"));
		}
		else { throw new SyntaxErrorException("Expected (one of) " + "(," + "-," + "n," + " but was " + input.substring(0, 1));}
	}
	else if(node.name.equals("F")) {
		if(input.startsWith("-")) {
			node.add(new Node("-"));
			node.add(new Node("E"));
		}
		else if(input.startsWith("n")) {
			node.add(new Node("n"));
		}
		else { throw new SyntaxErrorException("Expected (one of) " + "-," + "n," + " but was " + input.substring(0, 1));}
	}
	else if(node.name.equals("O")) {
		if(input.startsWith("+")) {
			node.add(new Node("+"));
		}
		else if(input.startsWith("-")) {
			node.add(new Node("-"));
		}
		else { throw new SyntaxErrorException("Expected (one of) " + "+," + "-," + " but was " + input.substring(0, 1));}
	}

	String newInput = input;
	for(Node n : node) {
		newInput = createChilds(n, newInput);
	}
	return newInput;
}

}
