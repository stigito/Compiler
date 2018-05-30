import edu.hm.cs.rs.compiler.toys.base.*;
import edu.hm.cs.rs.compiler.toys.compiler.*;

public class XMLChecker extends TreeWalker implements ASTGenerator {
    public XMLChecker() {
        up("Element/open", node -> {
            String openTagName = stripNonLetters(node.getChild(0).getStringAttribute());
            String closeTagName = stripNonLetters(node.getChild(2).getStringAttribute());
            if(!openTagName.equals(closeTagName))
                throw new SemanticError("tag name mismatch: " + openTagName + " vs. " + closeTagName);
        });
    }
    
    public Tree analyze(Tree root) throws SemanticError {
        walk(root);
        return root;
    }
    
    static String stripNonLetters(String string) {
        return string.replaceAll("\\W", "");
    }
}
