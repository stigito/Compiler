package edu.hm.stiglmeier.andreas.parsergenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import edu.hm.cs.rs.compiler.lab06rdparsergenerator.RDParserGenerator;
/**
 * Generator Klasse eines Parser-Sourcecodes für die gegebene Grammatik. 
 * @author Stigi
 *
 */
public class ParserGenerator implements RDParserGenerator {

	private static final String GRAMMAR = "=,E=(EOE),E=F,O=+,O=-,F=n,F=-E";
	private static final String IMPORT_UTIL = "import java.util.*;\n";
	private static final String CLASS_BODY = "public class RDParser";
	
	/**
	 * Main Method.
	 * @param args : args.
	 */
	public static void main(final String... args) {
	    System.out.println(new ParserGenerator().generate(GRAMMAR));
	}

	@Override
	public String generate(String grammar) {
		char startChar = grammar.charAt(2);
		
		Map<Character, Map<String, List<Character>>> firstMenge = getFirstmenge(grammar);

		String parserCode = addImportStuff();
		parserCode += addClassUpperBody(startChar);
		parserCode += "private " + this.getSyntaxErrorExceptionSourcecode() + "\n";
		parserCode += "private " + this.getNodeSourcecode() + "\n";
		parserCode += addParserMain() + "\n";
		parserCode += addDefaultConstructor(startChar) + "\n";
		parserCode += addGenerateMethodUpperBody() + "\n";
		
		//here is the magic
		parserCode += addGenerateMethodContent(firstMenge);
		//magic end
		
		parserCode += addGenerateMethodLowerBody() + "\n";
		parserCode += addClassLowerBody();
		return parserCode;
	}
	
	
	private Map<Character, Map<String, List<Character>>> getFirstmenge(String grammar) {
		
		char separatorSide = grammar.charAt(0);
		char separatorProd = grammar.charAt(1);	
		
		Map<Character, Map<String, List<Character>>> map = Arrays.stream(grammar.substring(2).split(Character.toString(separatorProd))).
		map(x -> x.split(Character.toString(separatorSide), 2)).
		collect(Collectors.groupingBy(x -> x[0].charAt(0), Collectors.mapping(x -> x[1], Collectors.toMap(x -> x, x -> new ArrayList<Character>()))));		

		while (map.values().stream().
		anyMatch(e -> e.keySet().stream().
				filter(key -> !e.get(key).isEmpty() ? false :  !map.containsKey(key.charAt(0)) ? e.get(key).add(key.charAt(0)) : map.get(key.charAt(0)).values().stream().filter(l -> !l.isEmpty()).
						peek(l -> e.get(key).addAll(l)).count() != 0)
				.count() != 0)) { }
		
		
		return map;
	}
	
	
	private String addImportStuff() {
		return IMPORT_UTIL + "\n";
	}
	
	private String addClassUpperBody(char startSymbol) {
		return CLASS_BODY + startSymbol + " {\n\n";
	}
	
	private String addClassLowerBody() {
		return "}";
	}
	
	private String addDefaultConstructor(char startSymbol) {
		return "public RDParser" + startSymbol + "() {}\n";
	}
	
	private String addGenerateMethodUpperBody() {
		return "public Node parse(String input) throws SyntaxErrorException {\n";
	}
	
	private String addGenerateMethodLowerBody() {
		return "}\n";
	}
	
	private String addParserMain() {
		
		return "public static void main(String... args) throws SyntaxErrorException {\n"
				+ "Node parseTree = new RDParserS().parse(args[0]);\n" 
				+ "System.out.println(parseTree);  // Parsebaum in einer Zeile\n"
				+ "parseTree.prettyPrint();        // Parsebaum gekippt, mehrzeilig\n"
				+ "}\n";
	}
	
//********************************************************************
// from here on it is the generating of the generate logic
//********************************************************************
	
	private String addGenerateMethodContent(Map<Character, Map<String, List<Character>>> firstMenge) {
		
		return "";
		
	}
}
