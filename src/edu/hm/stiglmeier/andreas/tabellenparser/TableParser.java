package edu.hm.stiglmeier.andreas.tabellenparser;

import edu.hm.cs.rs.compiler.toys.base.LL1TableParser;;

public class TableParser extends LL1TableParser {

	// non terminals
	private static final String PROGRAMM = "program";
	private static final String STATEMENT = "statement";
	private static final String OUTPUT = "output";
	private static final String ASSIGNMENT = "assignment";
	private static final String EMPTY = "empty";
	private static final String OUT = "OUT";
	private static final String TERM = "term";
	private static final String TERM2 = "TERM2";
	private static final String EXPRESSION = "expression";
	private static final String EXP2 = "EXP";
	private static final String POTENZ = "potenz";
	private static final String POTENZ2 = "potenz2";
	private static final String FACTOR = "factor";
	
	// terminals
	private static final String SEMICOLON = "semicolon";
	private static final String PRINT = "print";
	private static final String IDENTIFIER = "identifier";
	private static final String ASSIGN = "assign";
	private static final String ADD = "add";
	private static final String SUB = "sub";
	private static final String MUL = "mult";
	private static final String DIV = "div";
	private static final String MOD = "mod";
	private static final String OPEN = "open";
	private static final String CLOSE = "close";
	private static final String POT = "pot";
	private static final String NUMERAL = "numeral";
	
	
	public TableParser() {
		
		
		//matrix("lookahead", "stacksymbol", "rechte seite der produktion") ;
		
		
		
	}
}
