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

	//private static final String GRAMMAR = "=,E=(EOE),E=F,O=+,O=-,F=n,F=-E";
	private static final String IMPORT_UTIL = "import java.util.*;\n";
	private static final String CLASS_BODY = "public class RDParser";
	
	/**
	 * Main Method.
	 * @param args : args.
	 */
	public static void main(final String... args) {
	    System.out.println(new ParserGenerator().generate(args[0]));
	}

	@Override
	public String generate(String grammar) {
		char startChar = grammar.charAt(2);
		
		Map<Character, Map<String, List<Character>>> firstMenge = getFirstmenge(grammar);

		String parserCode = addImportStuff();
		parserCode += addClassUpperBody(startChar);
		parserCode += "private " + this.getSyntaxErrorExceptionSourcecode() + "\n";
		parserCode += "private " + this.getNodeSourcecode() + "\n";
		parserCode += addParserMain(startChar) + "\n";
		parserCode += addDefaultConstructor(startChar) + "\n";
		parserCode += addGenerateMethodUpperBody() + "\n";
		parserCode += addGenerateMethodContent(startChar);
		parserCode += addGenerateMethodLowerBody() + "\n";
		
		parserCode += addChildsMethodUpperBody() + "\n";
		parserCode += generateRekursiveChilds(firstMenge);
		parserCode += addChildsMethodLowerBody() + "\n";
		
		
		parserCode += addClassLowerBody();
		StringBuffer s = new StringBuffer(parserCode);
		
		return parserCode.replaceAll("\\u0000", "");
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
	
	private String addChildsMethodUpperBody() {
		return "private String createChilds(Node node, String input) throws SyntaxErrorException {";
	}
	
	private String addChildsMethodLowerBody() {
		return "}\n";
	}
	
	private String addParserMain(char startSymbol) {
		
		return "public static void main(String... args) throws SyntaxErrorException {\n"
				+ "\tNode parseTree = new RDParser" + startSymbol + "().parse(args[0]);\n" 
				+ "\tSystem.out.println(parseTree);  // Parsebaum in einer Zeile\n"
				+ "\tparseTree.prettyPrint();        // Parsebaum gekippt, mehrzeilig\n"
				+ "}\n";
	}
	
//********************************************************************
// from here on it is the generating of the generate logic
//********************************************************************
	
	private String addGenerateMethodContent(char startChar) {
		
		
		String content = "\tNode startNode = new Node(\"" + startChar + "\");\n";
		content += "\tString newInput = createChilds(startNode, input);\n";
		content += "\tif(newInput.length() > 0) {\n";
		content += "\t\tthrow new SyntaxErrorException(\"Expected nothing, but was \" + newInput);\n";
		content += "\t}\n";
		content += "\treturn startNode;\n";
		
		return content;
		
	}
	
	private String generateRekursiveChilds(Map<Character, Map<String, List<Character>>> firstMenge) {
		
		String content = "\tif(node.name.toLowerCase().equals(node.name)) {\n";
		content += "\t\tif(input.startsWith(node.name)) {\n";
		content += "\t\t\treturn input.substring(1);\n";	
		content += "\t\t}\n";		
		content += "\t\telse {\n";	
		content += "\t\t\tthrow new SyntaxErrorException(\"Expected (one of) \" + node.name + \", but was \" + input.substring(0, 1));\n";	
		content += "\t\t}\n";		
		content += "\t}\n\n";
		
		content += generateSourceSymbolLogik(firstMenge);
		
		content += "\tString newInput = input;\n";
		content += "\tfor(Node n : node) {\n";
		content += "\t\tnewInput = createChilds(n, newInput);\n";
		content += "\t}\n";	
		content += "\treturn newInput;\n";
		return content;
	}
	
	private String generateSourceSymbolLogik(Map<Character, Map<String, List<Character>>> firstMenge) {
		
		String content = "";
		
		String ifClauseNONT = "if";
		boolean isIFClauseNONT = false;
		
		for (char c : firstMenge.keySet()) {
			content += "\t" + ifClauseNONT + "(node.name.equals(\"" + c + "\")) {\n";
			content += generateProjectionLogic(firstMenge.get(c));
			content += "\t}\n";
			
			if (!isIFClauseNONT) {
				ifClauseNONT = "else if";
				isIFClauseNONT = true;
			}
		}
		return content + "\n";
	}
	
	private String generateProjectionLogic(Map<String, List<Character>> projections) {
		String content = "";
		String ifClause = "if";
		boolean isIFClause = false;
		
		for (String s : projections.keySet()) {
			content += "\t\t" + ifClause + "(" + getStartsWithFirstSymbols(projections.get(s)) + ") {\n";
			for (char c : s.toCharArray()) {
				content += "\t\t\tnode.add(new Node(\"" + c + "\"));\n";
			}
			content += "\t\t}\n";
			if (!isIFClause) {
				ifClause = "else if";
				isIFClause = true;
			}
		}
		content += "\t\telse { throw new SyntaxErrorException(\"Expected (one of) \"" + listToString(projections) + " + \" but was \" + input.substring(0, 1));}\n";
		
		return content;
	}
	
	private String getStartsWithFirstSymbols(List<Character> chars) {
		String content = "";
		String addOr = "";
		boolean isOr = false;
		
		for (char c : chars) {
			content += addOr + "input.startsWith(\"" + c + "\")";
			
			if (!isOr) {
				addOr = " || ";
				isOr = true;
			}
		}
		return content;
	}
	
	private String listToString(Map<String, List<Character>> projections) {
		String content = "";
		
		for (String s : projections.keySet()) {
			for (char c : projections.get(s)) {
				content += " + \"" + c + ",\""; 
			}
		}
		return content;
	}
}
