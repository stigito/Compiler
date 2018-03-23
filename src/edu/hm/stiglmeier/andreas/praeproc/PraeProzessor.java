package edu.hm.stiglmeier.andreas.praeproc;

import edu.hm.cs.rs.compiler.toys.base.LexicalError;
import edu.hm.cs.rs.compiler.toys.base.Preprocessor;
import edu.hm.cs.rs.compiler.toys.base.Source;

/**
 * This Class Represents a Praeprocesser in a Compiler.
 * Use the Method process(Source unprocessed).
 * @author Andreas Stiglmeier
 *
 */
public class PraeProzessor implements Preprocessor {
	
	/**
	 * States used in the implementation of the Praeprocessor.
	 * @author Andreas Stiglmeier
	 *
	 */
	private enum STATE {
		
		/**
		 * This State represents the normal processing of code with no comment.
		 */
		NORMAL(0),
		/**
		 * This State represents the possibility of currently reading a comment symbolised by the first occurence of '/'.
		 */
		COMM(1),
		/**
		 * This State represents the currently reading of a Comment Line : // ...
		 */
		COMMLINE(2),
		/**
		 * This State represents the currently reading of a Comment Block : /* ...
		 */
		COMMBLOCK(3),
		/**
		 * This State represents the currently reading of a Comment Block with the possibility of ending that block : /* ... *.
		 */
		COMMBLOCKEND(4);
		
		/**
		 * the integer value representing the state.
		 */
		private final int state;
		
		/**
		 * Constructor.
		 * @param state the integer value representing the state.
		 */ 
		STATE(int state) {
			this.state = state;
		}
		
		/**
		 * Getter for the integer value representing the state.
		 * @return state as int.
		 */
		 private int getState() {
			 return state;
		 }
	}
	
	/**
	 * default constructor.
	 */
	public PraeProzessor() {
		
	}

	/**
	 * Method for processing the pipeline in a compiler.
	 * @param unprocessed Source with sourcecode.
	 * @return Source without comments.
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
	 * This Method processes the currently read character and selects the handling method depending on the current state of the automat.
	 * @param character currently read character.
	 * @param processed Source from where the character was read.
	 * @param state current state of the automat.
	 * @return new state of the automat.
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
	 * This Method handles the "normal" processing state of the automat.
	 * When a '/' occures, no character is processed and the state of the automat changes to STATE.COMM,
	 * otherwise the character is appended to the Output Source and the state of the automat changes to STATE.NORMAL
	 * @param character currently read character.
	 * @param processed Output Source.
	 * @return new state of the automat.
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
	 * This Method handles the STATE.COMM processing state of the automat.
	 * When a '/' occures, no character is processed and the state of the automat changes to STATE.COMMLINE
	 * When a '*' occures, no character is processed and the state of the automat changes to STATE.COMMBLOCK
	 * otherwise the character is appended to the Output Source and the state of the automat changes to STATE.NORMAL  
	 * @param character currently read character.
	 * @param processed Output Source.
	 * @return new state of the automat.
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
	 * This Method handles the STATE.COMMLINE  processing state of the automat.
	 * When a '\n' occures, the character is appended to the Output Source and the state of the automat changes to STATE.NORMAL 
	 * otherwise no character is processed and the state of the automat changes to STATE.COMMLINE
	 * @param character currently read character.
	 * @param processed Output Source.
	 * @return new state of the automat.
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
	 * This Method handles the STATE.COMMBLOCK processing state of the automat.
	 * When a '*' occures, no character is processed and the state of the automat changes to STATE.COMMBLOCKEND
	 * When a '\n' occures, the character is appended to the Output Soruce and the state of the automat changes to STATE.COMMBLOCK,
	 * otherwise no character is processede and the state of the automat changes to STATE.COMMBLOCK 
	 * @param character currently read character.
	 * @param processed Output Source.
	 * @return new state of the automat.
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
	 * This Method handles the STATE.COMMBLOCEND processing state of the automat.
	 * When a '/' occures, a whitespace character is appended to the Output Source and the state of the automat changes to STATE.NORMAL,
	 * When a '*' occures, no character is processed and the state of the automat changes to STATE.COMMBLOCKEND,
	 * When a '\n' occures, the character is appended to the Output Soruce and the state of the automat changes to STATE.COMMBLOCK,
	 * otherwise no character is processed and the state of the automat changes to STATE.COMMBLOCK 
	 * @param character currently read character.
	 * @param processed Output Source.
	 * @return new state of the automat.
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
	 * Method to box appending a character and changing the state of the automat to STATE.NORMAL.
	 * @param character currently read character.
	 * @param processed Output Source.
	 * @return new state of the automat.
	 */
	private int proceedNormal(Source processed, char character) {
		processed.append(character);
		return STATE.NORMAL.getState();
	}
	

}