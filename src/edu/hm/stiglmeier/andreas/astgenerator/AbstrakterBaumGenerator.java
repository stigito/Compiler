package edu.hm.stiglmeier.andreas.astgenerator;

import java.util.Stack;

import edu.hm.cs.rs.compiler.toys.base.SemanticError;
import edu.hm.cs.rs.compiler.toys.base.Tree;
import edu.hm.cs.rs.compiler.toys.base.TreeWalker;
import edu.hm.cs.rs.compiler.toys.compiler.ASTGenerator;

public class AbstrakterBaumGenerator extends TreeWalker implements ASTGenerator {
	
	private final Stack<Tree> stack = new Stack<>();
	
	/*
		 * moeglichkeiten der Node-Adressierung:
		 * 
		 * 	- Nodename as String "Element"
		 * 	- Nodename/Childname "Element/open"
		 * 	- Nodename^Parentname
		 * 	- Nodename/- (keine Kindknoten)
	*/

	
	@Override
	public Tree analyze(Tree root) throws SemanticError {
		walk(root);
		if (stack.size() != 1) {
			throw new AssertionError("this compiler is broken!");
		}
		return stack.pop();
	}  
	
	public AbstrakterBaumGenerator() {
	        //verbose(true);
	        setHtmlOutput(true);

	        down("numeral", node -> {
	            Tree astNode = new Tree("numeral");
	            astNode.setAttribute(node.getAttribute());
	            if (stack.size() > 0) {
	 
	            	while (!stack.isEmpty() && stack.peek().getType().equals("neg")) {
	        			astNode = stack.pop().addChild(astNode);
	            	}
	            }
	            stack.push(astNode);
	            //System.err.println("stack size: " + stack.size());
	        });

	        down("identifier", node -> {
	            Tree astNode = new Tree("identifier");
	            astNode.setAttribute(node.getAttribute());
	            stack.push(astNode);
	        });

	        up("OUT", node -> {
	            Tree astNode = new Tree("output");
	            Tree astText = stack.pop();
	            astNode.addChild(astText);
	            stack.push(astNode);
	            //System.err.println("stack size: " + stack.size());
	        });

	        up("ASSIGNMENT", node -> {
	            Tree astNode = new Tree("assignment");
	            astNode.addChild(stack.pop());
	            astNode.add1stChild(stack.pop());
	            stack.push(astNode);
	        });

	        up("PROGRAM", node -> {
	            Tree astNode = new Tree("program");
	            int size = stack.size();
	            for (int i = 0; i < size; i++) {
	                astNode.add1stChild(stack.pop());
	            }
	            stack.push(astNode);
	        });
	        
	        // linksseitiges binden von expressions
	        down("sub", node -> {
	        	if (stack.size() > 0) {
		        	Tree child = stack.pop();
			        	if ((child.getType().equals("output")) || (child.getType().equals("assignment")) || (!child.getType().equals("identifier") && !child.getType().equals("numeral") && child.getChild(1) == null)) {
		        			stack.push(child);
			        		stack.push(new Tree("neg"));
		        			return;
		        		}
			        	Tree subNode = new Tree("sub");
			        	subNode.addChild(child);
			        	stack.push(subNode);
	        	}
	        	else {
	        		stack.push(new Tree("neg"));
	        	}
	        });
	        
	        down("add", node -> {
	        	if (stack.size() > 0) {
		        	Tree child = stack.pop();
		        		//if (!child.getType().equals("identifier") && !child.getType().equals("numeral") && child.getChild(1) == null) {
		        		if ((child.getType().equals("neg") && child.getChild(0) == null)||(!child.getType().equals("identifier") && !child.getType().equals("numeral") && !child.getType().equals("neg") && child.getChild(1) == null)) {
		        			stack.push(child);
		        			//System.err.println("pushed, Stack size: " + stack.size());
		        			return;
		        		}
			        	Tree subNode = new Tree("add");
			        	subNode.addChild(child);
			        	stack.push(subNode);
			        	//System.err.println("added add, Stack size: " + stack.size());
	        	}
	        });
	        
	        down("EXP2", node -> {
	        	
	        	if (stack.size() > 1) {
		        	Tree child = stack.pop();
		        	Tree possibleIdentifier = stack.pop();
		        	if (possibleIdentifier.getType().equals("add") || possibleIdentifier.getType().equals("sub")) {
		        	//no identifier or assignment -> has to have an add or sub.
		        		possibleIdentifier.addChild(child);
		        		stack.push(possibleIdentifier);
		        	}
		        	else {
		        		//restore stack
		        		stack.push(possibleIdentifier);
		        		stack.push(child);
		        	}
		        	//System.err.println("stack size: " + stack.size());
	        	}
	        	
	        });
	        
	        //linksseitiges binden von termen
	        
	        down("mult", node -> {
	        	downTerm("mult");
	        });
	        
	        down("div", node -> {
	        	downTerm("div");
	        });
	        
	        down("mod", node -> {
	        	downTerm("mod");
	        });

	        up("TERM2/mult", node -> {
	        	upTerm();
	        });
	        
	        up("TERM2/div", node -> {
	        	upTerm();
	        });
	        
	        up("TERM2/mod", node -> {
	        	upTerm();
	        });
	        
	        
	        // rechtsseitiges binden von potenzen
	        
	        down("pot", node -> {
	        	Tree child = stack.pop();
	        	Tree subNode = new Tree("pot");
	        	subNode.addChild(child);
	        	stack.push(subNode);
	        });
	        
	        up("POTENZ^POTENZ2", node -> {
	        	
	        	if (stack.size() > 1) {
		        	Tree child = stack.pop();
		        	Tree possibleIdentifier = stack.pop();
		        	if (possibleIdentifier.getType().equals("pot")) {
		        		possibleIdentifier.addChild(child);
		        		stack.push(possibleIdentifier);
		        	}
		        	else {
		        		//restore stack
		        		stack.push(possibleIdentifier);
		        		stack.push(child);
		        	}
	        	}
	        });
	       
	    }
	
	
	private void downTerm(String type) {
		Tree child = stack.pop();
    	Tree subNode = new Tree(type);
    	
    	if (stack.size() > 0) {
    		Tree possibleEqualTree = stack.pop();
    		if (possibleEqualTree.getType().equals("mult") || possibleEqualTree.getType().equals("div") || possibleEqualTree.getType().equals("mod")) {
    			possibleEqualTree.addChild(child);
    			subNode.addChild(possibleEqualTree);
    			stack.push(subNode);
    			return;
    		}
    		else {
    			stack.push(possibleEqualTree);
    		}
    	}
    	subNode.addChild(child);
    	stack.push(subNode);
	}
	
	private void upTerm() {
    	if (stack.size() > 0) {
    		Tree astNode = stack.pop();
    		
    		if (astNode.getType().equals("mult") || astNode.getType().equals("mod") || astNode.getType().equals("div")) {
    			stack.push(astNode);
    			//System.err.println("name oberster: " + astNode.getType() + "Stack size: " + stack.size());
    		}
    		else {
    			Tree astNode2 = stack.pop();
    			//System.err.println("name oberster: " + astNode.getType() + ", name zweiter: " + astNode2.getType());
    			astNode2.addChild(astNode);
    			stack.push(astNode2);
    		}
    	}
	}
}
