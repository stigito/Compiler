package edu.hm.stiglmeier.andreas.astgenerator;

import java.util.Stack;

import edu.hm.cs.rs.compiler.toys.base.SemanticError;
import edu.hm.cs.rs.compiler.toys.base.Tree;
import edu.hm.cs.rs.compiler.toys.base.TreeWalker;
import edu.hm.cs.rs.compiler.toys.compiler.ASTGenerator;

public class AbstrakterBaumGenerator extends TreeWalker implements ASTGenerator {
	
	private final Stack<Tree> stack = new Stack<>();
	
	public AbstrakterBaumGenerator() {
		/*
		 * 
		 * möglichkeiten der Node-Adressierung:
		 * 	- Nodename as String "Element"
		 * 	- Nodename/Childname "Element/open"
		 * 	- Nodename^Parentname
		 * 	- Nodename/- (keine Kindknoten)
		 */
		
		up("Element/open", node -> stack.push(new Tree("element")));
		
		down("TERM2", (node,message) -> {
			if(node.getNextChild(null) == null) {
				node.getParent().remove(node);
			}
		});
		
	}
	@Override
	public Tree analyze(Tree root) throws SemanticError {
		walk(root);
		if(stack.size() != 1) {
			throw new AssertionError("this compiler is broken!");
		}
		return stack.pop();
	}  

}
