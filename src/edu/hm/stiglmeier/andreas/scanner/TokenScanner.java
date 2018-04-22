package edu.hm.stiglmeier.andreas.scanner;

import edu.hm.cs.rs.compiler.toys.base.BaseScanner;

/**
 * Class for creating a DFA to create a Stream of Tokens.
 * @author Stigi
 *
 */
public class TokenScanner extends  BaseScanner {


		private static final String	SEMICOLON = ";";
		private static final String OPEN = "(";
		private static final String CLOSE = ")";
		private static final String ADD = "+";
		private static final String SUB = "-";
		private static final String MUL = "*";
		private static final String DIV = "/";
		private static final String MOD = "%";
		private static final String DPOINT = ":";
		private static final String ASSIGN = "=";
		private static final String NUMERAL = "0123456789";
		private static final String LETTER = "abcdefghijklmnopqrstuvwxyz0123456789";
		private static final String LETTER_LETTER = "abcdefghijklmnopqrstuvwxyz";
		private static final String PRINT_P = "p";
		private static final String PRINT_R = "r";
		private static final String PRINT_I = "i";
		private static final String PRINT_N = "n";
		private static final String PRINT_T = "t";
		private static final String EMPTY = "";
		private static final String IGNORE = " \n\r";
		
		
		private static final String STATE_INIT = "init";

		private static final String STATE_P = "print_p";
		private static final String STATE_PR = "print_pr";
		private static final String STATE_PRI = "print_pri";
		private static final String STATE_PRIN = "print_prin";
		private static final String STATE_PRINT = "print";
		private static final String STATE_ASSIGN_1 = "assign_:";
		private static final String STATE_ASSIGN_2 = "assign";
		private static final String STATE_NUMERAL = "numeral";
		private static final String STATE_ADD = "add";
		private static final String STATE_SUB = "sub";
		private static final String STATE_MUL = "mult";
		private static final String STATE_DIV = "div";
		private static final String STATE_MOD = "mod";
		private static final String STATE_OPEN = "open";
		private static final String STATE_CLOSE = "close";
		private static final String STATE_SEMICOLON = "semicolon";
		private static final String STATE_LETTER = "identifier";
		private static final String STATE_IGNORE = "ignore";
	
	
	/**
	 * Konstruktor for the Scanner.
	 */
		public TokenScanner() {
			start(STATE_INIT);
			
			accept(STATE_PRINT);
			accept(STATE_ASSIGN_2);
			accept(STATE_LETTER, true); // identifier
			accept(STATE_NUMERAL, true);
			accept(STATE_ADD);
			accept(STATE_SUB);
			accept(STATE_MUL);
			accept(STATE_DIV);
			accept(STATE_MOD);
			accept(STATE_OPEN);
			accept(STATE_CLOSE);
			accept(STATE_SEMICOLON);
			acceptAndIgnore(STATE_IGNORE);
			
			accept(STATE_P, STATE_LETTER);
			accept(STATE_PR, STATE_LETTER);
			accept(STATE_PRI, STATE_LETTER);
			accept(STATE_PRIN, STATE_LETTER);
			
			
			transition(STATE_INIT, IGNORE, STATE_IGNORE);
			
			transition(STATE_INIT, SUB, STATE_SUB);
			
			transition(STATE_INIT, DIV, STATE_DIV);
			
			transition(STATE_INIT, MOD, STATE_MOD);
			
			transition(STATE_INIT, SEMICOLON, STATE_SEMICOLON);
			
			transition(STATE_INIT, DPOINT, STATE_ASSIGN_1);
			transition(STATE_ASSIGN_1, ASSIGN, STATE_ASSIGN_2);
			
			transition(STATE_INIT, NUMERAL, STATE_NUMERAL);
			transition(STATE_NUMERAL, NUMERAL, STATE_NUMERAL);
			
			transition(STATE_INIT, LETTER_LETTER.replace(PRINT_P, EMPTY), STATE_LETTER);
			transition(STATE_LETTER, LETTER, STATE_LETTER);
			
			transition(STATE_INIT, ADD, STATE_ADD);
			transition(STATE_INIT, MUL, STATE_MUL);
			transition(STATE_INIT, OPEN, STATE_OPEN);
			transition(STATE_INIT, CLOSE, STATE_CLOSE);
			
			transition(STATE_INIT, PRINT_P, STATE_P);
			transition(STATE_P, PRINT_R, STATE_PR);
			transition(STATE_PR, PRINT_I, STATE_PRI);
			transition(STATE_PRI, PRINT_N, STATE_PRIN);
			transition(STATE_PRIN, PRINT_T, STATE_PRINT);
			
			transition(STATE_P, LETTER.replace(PRINT_R, EMPTY), STATE_LETTER);
			transition(STATE_PR, LETTER.replace(PRINT_I, EMPTY), STATE_LETTER);
			transition(STATE_PRI, LETTER.replace(PRINT_N, EMPTY), STATE_LETTER);
			transition(STATE_PRIN, LETTER.replace(PRINT_T, EMPTY), STATE_LETTER);
			transition(STATE_PRINT, LETTER, STATE_LETTER);
			
			
		}
}
