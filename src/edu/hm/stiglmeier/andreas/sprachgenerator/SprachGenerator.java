package edu.hm.stiglmeier.andreas.sprachgenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import edu.hm.cs.rs.compiler.lab04generator.LanguageGenerator;

public class SprachGenerator implements LanguageGenerator {

	private static final int MAXLENGTH = 9;
	
	public static void main(String... args) {
		System.setProperty("prettyprint", "blaBLABLAJDFKALNKL");
		LanguageGenerator generator = new SprachGenerator();
		//Stream grammar = generator.parseGrammar("=;S=();S=(S);S=()S;S=(S)S"); // 4
		//Stream grammar = generator.parseGrammar(":,P:,P:Q,Q:a,Q:b,Q:aa,Q:bb,Q:aQa,Q:bQb"); // 4
		//Stream grammar = generator.parseGrammar(">/S>aSBc/S>aBc/cB>Bc/bB>Bb/aB>ab"); // 6
		Stream grammar = generator.parseGrammar("-,S-X,S-aB,S-Ba,S-BaB,S-XaB,S-BaX,X-a,X-c,X-aX,X-cX,B-ab,B-BB,B-XB,B-BX,B-aXb,B-aBb,B-ba,B-bXa,B-bBa,B-BXB,B-XBX"); // 9
		grammar = LanguageGenerator.prettyprint(grammar);
		
		Stream sätze = generator.generate(grammar, MAXLENGTH);
		printStringStream(sätze);

		
		grammar.close();
		sätze.close();
	}
	private static void printStringStream(Stream<String> stream) {
		
		for(Object s : stream.toArray()) {
			System.out.println(s);
		}
	}
	
	@Override
	public Stream<String> generate(Stream<String[]> grammar, int uptoLength) {
		
		int lastElementSize = 0;
		List<String[]> grammatik = grammar.collect(Collectors.toList());
		
		List<String> ausdruecke = initStartProduction(grammatik.iterator());
		List<String> newAusdruecke;
		List<String> saetze = new ArrayList<String>();
		
		while(lastElementSize <= uptoLength) {
			newAusdruecke = new ArrayList<String> (ausdruecke);
			for (String ausdruck : ausdruecke) {
				if(ausdruck.equals(ausdruck.toLowerCase())) {
					lastElementSize = ausdruck.length();
					//check if already past limit
					if(lastElementSize > uptoLength) {
						break;
					}
					if(!saetze.contains(ausdruck)) {
						saetze.add(ausdruck);
					}
				}
				else {
					doProductions(ausdruck, newAusdruecke, grammatik);
				}
				newAusdruecke.remove(ausdruck);
			}
			
			ausdruecke = newAusdruecke;
		}
		
		
		saetze.sort(new Comparator<String>() {
			public int compare(String s1, String s2) {
				if(s1.length() != s2.length()) {
					return s1.length() - s2.length();
				}
				else {
					return s1.compareTo(s2);
				}
			}
			
		});
		return saetze.stream();
	}
	
	private void doProductions(String ausdruck, List<String> ausdruecke, List<String[]> grammatik) {
		String newAusdruck = ausdruck;
		for(String[] s : grammatik) {
			newAusdruck = ausdruck.replaceAll(s[0], s[1]);
			
			if(!newAusdruck.equals(ausdruck)) {
				ausdruecke.add(newAusdruck);
			}
		}
		
	}
	
	private List<String> initStartProduction(Iterator<String[]> grammatik) {
		List<String> list = new ArrayList<String>();
		String startVariable = "";
		String[] rule;
		
		while(grammatik.hasNext()) {
			rule = grammatik.next();
			
			if(startVariable.equals("")) {
				startVariable = rule[0];
			}
			
			if(rule[0].equals(startVariable)) {
					list.add(rule[1]);
				
			}
			
			
		}
		
		return list;
	}
	
	
	


	@Override
	public Stream<String[]> parseGrammar(String grammarString) {
		
		/*
		 * the example for parseGrammar in the course created a String Array for a rule to a empty statement ( P -> ), that has the length of 1.
		 * For That case, the code in the lambda statement was modified to also create a 2 length array in that scenario
		 * 
		 */
		String sideSplitter = grammarString.substring(0, 1);
		String productionSplitter = grammarString.substring(1, 2);
		return Stream.of(grammarString.split(productionSplitter)).skip(1).map(productionString -> {
			if(productionString.indexOf(sideSplitter) == productionString.length()-1) {
				return new String[] {productionString.substring(0, productionString.length()-1), ""};
			}
			else {
				return productionString.split(sideSplitter);
			}
		});
	}

}
