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
		private int state;
		
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
	 * dummy.
	 */
	private String buffer = "";
	
	/**
	 * dummy.
	 */
	private int state;
	
	/**
	 * dummy javadoc.
	 */
	public PraeProzessor() {
		
	}

	/**
	 * dummy javadoc.
	 */
	@Override
	public Source process(Source unprocessed) throws LexicalError {
		
		final Source processed = new Source();
		state = STATE.NORMAL.getState(); // resetting automat
		buffer = "";
		while(unprocessed.hasMore()) {
			processed.append(processChar(unprocessed.getNextChar()));
		}
		
		if(state >= 2) {
			throw new LexicalError();
		}
		processed.append(buffer);
		return processed;
	}
	
	/**
	 * dummy javadoc.
	 * @param character dummy.
	 * @return dummy.
	 */
	private String processChar(char character) {
		switch(state) {
		case 0:
			return processNormal(character);
		case 1: 
			return processComm(character);
		case 2:
			return processCommLine(character);
		case 3:
			return processCommBlock(character);
		case 4:
			return processCommBlockEnd(character);
		default:
				return processNormal(character);
		}
	}
	
	/**
	 * dummy javadoc.
	 * @param character dummy.
	 * @return dummy.
	 */
	private String processNormal(char character) {
		if (character == '/') {
			buffer += character;
			state = STATE.COMM.getState();
			return "";
		}
		else {
			return proceedNormal(character);
		}
		
	}
	
	/**
	 * dummy javadoc.
	 * @param character dummy.
	 * @return dummy.
	 */
	private String processComm(char character) {
		if (character == '/') {
			state = STATE.COMMLINE.getState();
		}
		else if(character == '*') {
			state = STATE.COMMBLOCK.getState();
		}
		else {
			state = STATE.NORMAL.getState();
			return proceedNormal(character);
		}
		buffer = "";
		return "";
	}
	
	/**
	 * dummy javadoc.
	 * @param character dummy.
	 * @return dummy.
	 */
	private String processCommLine(char character) {
		if (character == '\n') {
			state = STATE.NORMAL.getState();
			return proceedNormal(character);
		}
		else {
			return "";
		}
	}
	
	/**
	 * dummy javadoc.
	 * @param character dummy.
	 * @return dummy.
	 */
	private String processCommBlock(char character) {
		if (character == '*') {
			state = STATE.COMMBLOCKEND.getState();
			return "";
		}
		else if (character == '\n') {
			return proceedNormal(character);
		}
		else {
			return "";
		}
	}
	
	/**
	 * dummy javadoc.
	 * @param character dummy.
	 * @return dummy.
	 */
	private String processCommBlockEnd(char character) {
		if (character == '/') {
			state = STATE.NORMAL.getState();
			buffer = "";
			return " ";
		}
		else if (character == '\n') {
			state = STATE.COMMBLOCK.getState();
			return proceedNormal(character);
		}
		else if(character == '*') {
			state = STATE.COMMBLOCKEND.getState();
			return "";
		}
		else {
			state = STATE.COMMBLOCK.getState();
			return "";
		}
	}
	
	/**
	 * dummy javadoc.
	 * @param character dummy.
	 * @return dummy.
	 */
	private String proceedNormal(char character) {
		final String s = new String(buffer);
		buffer = "";
		return s + character;
	}
	

}