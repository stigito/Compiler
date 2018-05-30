import edu.hm.cs.rs.compiler.toys.base.*;
import edu.hm.cs.rs.compiler.toys.compiler.*;
import java.util.*;

public class XMLASTGenerator extends XMLChecker {
    private final Stack<Tree> stack = new Stack<>();
    
    public XMLASTGenerator() {
        setHtmlOutput(true);
    
        up("Content/-", node -> stack.push(new Tree("element")));
        
        up("Content/text", node -> {
            Tree astElement = stack.pop();
            Tree astText = new Tree("text");
            astText.setAttribute(node.getChild(0).getAttribute());
            astElement.add1stChild(astText);
            stack.push(astElement);
        });
        
        up("Content/Element", node -> {
            Tree astElement = stack.pop();
            Tree astChild = stack.pop();
            astElement.add1stChild(astChild);
            stack.push(astElement);
        });
        
        down("Element/empty", node -> {
            Tree astNode = new Tree("element");
            astNode.setAttribute(stripNonLetters(node.getChild(0).getStringAttribute()));
            stack.push(astNode);
        });
        
        up("Element/open", node -> {
            stack.peek().setAttribute(stripNonLetters(node.getChild(0).getStringAttribute()));
        });
    }
    
    public Tree analyze(Tree root) throws SemanticError {
        walk(root);
        if(stack.size() != 1)
            throw new AssertionError("this compiler is broken!");
        return stack.pop();
    }
}
