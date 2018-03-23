package edu.hm.stiglmeier.andreas.praeproc;

import edu.hm.cs.rs.compiler.toys.base.LexicalError;
import edu.hm.cs.rs.compiler.toys.base.Preprocessor;
import edu.hm.cs.rs.compiler.toys.base.Source;

/**
 * dummy javadoc.
 * @author Stigi
 *
 */
public class PraeProzessor implements Preprocessor {
	
	/**
	 * dummy javadoc.
	 * @author Stigi
	 *
	 */
	private enum STATE {
		
		/**
		 * dummy.
		 */
		NORMAL(0),
		/**
		 * dummy.
		 */
		COMM(1),
		/**
		 * dummy.
		 */
		COMMLINE(2),
		/**
		 * dummy.
		 */
		COMMBLOCK(3),
		/**
		 * dummy.
		 */
		COMMBLOCKEND(4);
		
		/**
		 * dummy.
		 */
		private final int state;
		
		/**
		 * dummy javadoc.
		 * @param state dummy.
		 */ 
		STATE(int state) {
			this.state = state;
		}
		
		/**
		 * dummy javadoc.
		 * @return dummy.
		 */
		 private int getState() {
			 return state;
		 }
	}
	
	/**
	 * dummy javadoc.
	 */
	public PraeProzessor() {
		
	}

	/**
	 * dummy javadoc.
	 * @param unprocessed dummy.
	 */
	@Override
	public Source process(Source unprocessed) throws LexicalError {
		
		final Source processed = new Source();
		int state = STATE.NORMAL.getState(); // resetting automat
		char character = 0;
		while (unprocessed.hasMore()) {
			character = unprocessed.getNextChar();
			state = processChar(processed, character, state);
		}
		
		if (state == 1) { // append '/'
			processed.append(character);
		}
		if (state >= 2) {
			throw new LexicalError();
		}
		return processed;
	}
	
	/**
	 * dummy javadoc.
	 * @param character dummy.
	 * @param processed dummy.
	 * @return dummy.
	 */
	private int processChar(Source processed, char character, int state) {
		switch(state) {
		case 0:
			return processNormal(processed, character);
		case 1:
			return processComm(processed, character);
		case 2:
			return processCommLine(processed, character);
		case 3:
			return processCommBlock(processed, character);
		case 4:
			return processCommBlockEnd(processed, character);
		default:
				return processNormal(processed, character);
		}
	}
	
	/**
	 * dummy javadoc.
	 * @param character dummy.
	 * @param processed dummy.
	 * @return dummy.
	 */
	private int processNormal(Source processed, char character) {
		if (character == '/') {
			return STATE.COMM.getState();
		}
		else {
			return proceedNormal(processed, character);
		}
		
	}
	
	/**
	 * dummy javadoc.
	 * @param character dummy.
	 * @param processed dummy.
	 * @return dummy.
	 */
	private int processComm(Source processed, char character) {
		if (character == '/') {
			return STATE.COMMLINE.getState();
		}
		else if (character == '*') {
			return STATE.COMMBLOCK.getState();
		}
		else {
			processed.append('/');
			return proceedNormal(processed, character);
		}
	}
	
	/**
	 * dummy javadoc.
	 * @param character dummy.
	 * @param processed dummy.
	 * @return dummy.
	 */
	private int processCommLine(Source processed, char character) {
		if (character == '\n') {
			processed.append(character);
			return STATE.NORMAL.getState();
		}
		else {
			return STATE.COMMLINE.getState();
		}
	}
	
	/**
	 * dummy javadoc.
	 * @param character dummy.
	 * @param processed dummy.
	 * @return dummy.
	 */
	private int processCommBlock(Source processed, char character) {
		if (character == '*') {
			return STATE.COMMBLOCKEND.getState();
		}
		else if (character == '\n') {
			processed.append(character);
		}
		return STATE.COMMBLOCK.getState();
		
	}
	
	/**
	 * dummy javadoc.
	 * @param character dummy.
	 * @param processed dummy.
	 * @return dummy.
	 */
	private int processCommBlockEnd(Source processed, char character) {
		if (character == '/') {
			processed.append(' ');
			return STATE.NORMAL.getState();
		}
		else if (character == '*') {
			return STATE.COMMBLOCKEND.getState();
		}
		else if (character == '\n') {
			processed.append(character);
		}
		return STATE.COMMBLOCK.getState();

	}
	
	/**
	 * dummy javadoc.
	 * @param character dummy.
	 * @param processed dummy.
	 * @return dummy.
	 */
	private int proceedNormal(Source processed, char character) {
		processed.append(character);
		return STATE.NORMAL.getState();
	}
	

}