package edu.hm.stiglmeier.andreas.tabellenparser;

import edu.hm.cs.rs.compiler.toys.base.LL1TableParser;;

public class TableParser extends LL1TableParser {

	// non terminals
	private static final String PROGRAMM = "PROGRAM";
	private static final String STATEMENT = "STATEMENT";
	private static final String OUTPUT = "OUTPUT";
	private static final String ASSIGNMENT = "ASSIGNMENT";
	private static final String EMPTY = "EMPTY";
	private static final String OUT = "OUT";
	private static final String TERM = "TERM";
	private static final String TERM2 = "TERM2";
	private static final String EXPRESSION = "EXPRESSION";
	private static final String EXP2 = "EXP2";
	private static final String POTENZ = "POTENZ";
	private static final String POTENZ2 = "POTENZ2";
	private static final String FACTOR = "FACTOR";
	
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
	private static final String END = "$";
	
	
	/**
	 * Constructor for the tableparser.
	 * 
	 * If errors occure, use "verbose(true);" for detailed information, where the error occured.
	 * 
	 * Also Note, that any Non-Terminal Node has to be in Upper Cases, otherwise the courseware will fail the tests.
	 * Also, where ever there is an epsilon expression, use the follow mengen as lookahead!
	 * 
	 * In Addition, please use the "tabellenparser_ueberlegungen.xlsx" for detailed information on how the matrix was created.
	 */
	public TableParser() {
		//verbose(true);
		
		//matrix("lookahead", "stacksymbol", "rechte seite der produktion");
		
		
		//program start productions
		matrix(PRINT, PROGRAMM, OUT + " " + STATEMENT);
		matrix(IDENTIFIER, PROGRAMM, ASSIGNMENT + " " + STATEMENT);
		matrix(SEMICOLON, PROGRAMM, EMPTY + " " + STATEMENT);
		
		// -> follow mengen noetig
		matrix(END, PROGRAMM, "");
		
		//statement productions
		matrix(PRINT, STATEMENT, OUT + " " + STATEMENT);
		matrix(IDENTIFIER, STATEMENT, ASSIGNMENT + " " + STATEMENT);
		matrix(SEMICOLON, STATEMENT, EMPTY + " " + STATEMENT);
		
		// -> follow mengen noetig
		matrix(END, STATEMENT, "");
		
		//output assignment and empty productions
		matrix(PRINT, OUT, PRINT + " " + OUTPUT);
		matrix(SEMICOLON, OUTPUT, SEMICOLON);
		
		matrix(OPEN, OUTPUT, EXPRESSION + " " + SEMICOLON);
		matrix(ADD, OUTPUT, EXPRESSION + " " + SEMICOLON);
		matrix(SUB, OUTPUT, EXPRESSION + " " + SEMICOLON);
		matrix(IDENTIFIER, OUTPUT, EXPRESSION + " " + SEMICOLON);
		matrix(NUMERAL, OUTPUT, EXPRESSION + " " + SEMICOLON);
		
		matrix(IDENTIFIER, ASSIGNMENT, IDENTIFIER + " " + ASSIGN + " " + EXPRESSION + " " + SEMICOLON);
		matrix(SEMICOLON, EMPTY, SEMICOLON);
		
		// expression productions
		matrix(OPEN, EXPRESSION, TERM + " " + EXP2);
		matrix(ADD, EXPRESSION, TERM + " " + EXP2);
		matrix(SUB, EXPRESSION, TERM + " " + EXP2);
		matrix(IDENTIFIER, EXPRESSION, TERM + " " + EXP2);
		matrix(NUMERAL, EXPRESSION, TERM + " " + EXP2);
		
		matrix(ADD, EXP2, ADD + " " + EXPRESSION);
		matrix(SUB, EXP2, SUB + " " + EXPRESSION);
		
		// -> follow mengen noetig
		matrix(CLOSE, EXP2, "");
		matrix(SEMICOLON, EXP2, "");
		
		// Term productions
		matrix(OPEN, TERM, POTENZ + " " + TERM2);
		matrix(ADD, TERM, POTENZ + " " + TERM2);
		matrix(SUB, TERM, POTENZ + " " + TERM2);
		matrix(IDENTIFIER, TERM, POTENZ + " " + TERM2);
		matrix(NUMERAL, TERM, POTENZ + " " + TERM2);
		
		matrix(MUL, TERM2, MUL + " " + TERM);
		matrix(DIV, TERM2, DIV + " " + TERM);
		matrix(MOD, TERM2, MOD + " " + TERM);
		
		// -> follow mengen noetig
		matrix(ADD, TERM2, "");
		matrix(SUB, TERM2, "");
		matrix(CLOSE, TERM2, "");
		matrix(SEMICOLON, TERM2, "");
		
		//Potenz productions
		matrix(OPEN, POTENZ, FACTOR + " " + POTENZ2);
		matrix(ADD, POTENZ, FACTOR + " " + POTENZ2);
		matrix(SUB, POTENZ, FACTOR + " " + POTENZ2);
		matrix(IDENTIFIER, POTENZ, FACTOR + " " + POTENZ2);
		matrix(NUMERAL, POTENZ, FACTOR + " " + POTENZ2);
		
		matrix(POT, POTENZ2, POT + " " + POTENZ);
		
		// -> follow mengen noetig
		matrix(ADD, POTENZ2, "");
		matrix(SUB, POTENZ2, "");
		matrix(CLOSE, POTENZ2, "");
		matrix(SEMICOLON, POTENZ2, "");
		matrix(MUL, POTENZ2, "");
		matrix(DIV, POTENZ2, "");
		matrix(MOD, POTENZ2, "");
		
		//factor productions
		
		matrix(OPEN, FACTOR, OPEN + " " + EXPRESSION + " " + CLOSE);
		matrix(ADD, FACTOR, ADD + " " + FACTOR);
		matrix(SUB, FACTOR, SUB + " " + FACTOR);
		matrix(IDENTIFIER, FACTOR, IDENTIFIER);
		matrix(NUMERAL, FACTOR, NUMERAL);
		
	}
}
