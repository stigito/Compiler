package edu.hm.stiglmeier.andreas.praeproc;

import edu.hm.cs.rs.compiler.toys.base.LexicalError;
import edu.hm.cs.rs.compiler.toys.base.Preprocessor;
import edu.hm.cs.rs.compiler.toys.base.Source;

public class PraeProzessor implements Preprocessor {

	@Override
	public Source process(Source unprocessed) throws LexicalError {
		
		Source processed = new Source();
		char character;
		
		while(unprocessed.hasMore()) {
			character = unprocessed.getNextChar();
			if(character == ' ') {
				processed.append("x");
			}
			else {
				processed.append(character);

			}
		}
		
		return processed;
	}

}
