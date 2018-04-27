package edu.hm.stiglmeier.andreas.sprachgenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import edu.hm.cs.rs.compiler.lab04generator.LanguageGenerator;

/**
 * languagegenerator.
 * @author Stigi
 *
 */
public class SprachGenerator implements LanguageGenerator {

	private static final int MAXLENGTH = 6;
	
	/**
	 * main.
	 * @param args arguments.
	 */
	public static void main(String... args) {
		double startMillis = System.currentTimeMillis();
		System.setProperty("prettyprint", "blaBLABLAJDFKALNKL");
		LanguageGenerator generator = new SprachGenerator();
		//Stream<String[]> grammar = generator.parseGrammar("=;S=();S=(S);S=()S;S=(S)S"); // 4
		//Stream<String[]> grammar = generator.parseGrammar(":,P:,P:Q,Q:a,Q:b,Q:aa,Q:bb,Q:aQa,Q:bQb"); // 4
		//Stream<String[]> grammar = generator.parseGrammar(">/S>aSBc/S>aBc/cB>Bc/bB>Bb/aB>ab"); // 6
		Stream<String[]> grammar = generator.parseGrammar("-,S-X,S-aB,S-Ba,S-BaB,S-XaB,S-BaX,X-a,X-c,X-aX,X-cX,B-ab,B-BB,B-XB,B-BX,B-aXb,B-aBb,B-ba,B-bXa,B-bBa,B-BXB,B-XBX"); // 9
		//Stream<String[]> grammar = generator.parseGrammar(":,P:,P:Q,Q:SQ,Q:S,S:pX;,S:i=X;,X:X+X,X:-X,X:(X),X:i,X:n");
		//Stream<String[]> grammar = generator.parseGrammar("=;S=abcd;S=aSQ;cQd=Bccdd;dQ=Qd;bB=bb;cB=Bc");
		//Stream<String[]> grammar = generator.parseGrammar("cdxSYcSYxzdSYcsYdxScxYzdYzczydYca");
		grammar = LanguageGenerator.prettyprint(grammar);
		
		Stream<String> saetze = generator.generate(grammar, MAXLENGTH);
		printStringStream(saetze);

		
		grammar.close();
		saetze.close();
		
		
		System.out.println("Time processed: " + ((System.currentTimeMillis() - startMillis) / 1000) + " seconds");
	} 
	private static void printStringStream(Stream<String> stream) {
		
		for (Object s : stream.toArray()) {
			System.out.println(s);
		}
	}
	
	@Override
	public Stream<String> generate(Stream<String[]> grammar, int uptoLength) {
		
		List<String[]> grammatik = grammar.collect(Collectors.toList());
		List<String> ausdruecke = initStartProduction(grammatik);
		List<String> saetze = new ArrayList<String>();
		
		int oldLength = 0;
		int length = ausdruecke.size();
		String ausdruck;
		while (oldLength < length) {
			//System.out.println("next round. old length: " + oldLength + ", new Length: " + length);
			for (int i = oldLength; i < length; i++) {
					ausdruck = ausdruecke.get(i);
					if (ausdruck.equals(ausdruck.toLowerCase())) {
							saetze.add(ausdruck);
					}
					else {
						doProductions(ausdruck, ausdruecke, grammatik, uptoLength);
					}
			}
			

			oldLength = length;
			length = ausdruecke.size();
			
			
			
		}
		sortList(saetze);
		return saetze.stream();
	}
	
	private void sortList(List<String> l) {
		l.sort(new Comparator<String>() {
			public int compare(String s1, String s2) {
				if (s1.length() != s2.length()) {
					return s1.length() - s2.length();
				}
				else {
					return s1.compareTo(s2);
				}
			}
			
		});
	}
	
	private void doProductions(String ausdruck, List<String> ausdruecke, List<String[]> grammatik, int uptoLength) {
		String newAusdruck = ausdruck;
		for (String[] s : grammatik) {
			newAusdruck = ausdruck.replaceFirst(s[0], s[1]);
			if (newAusdruck.length() <= uptoLength && !ausdruecke.contains(newAusdruck)) {
				ausdruecke.add(newAusdruck);
			}
		}
		
	}
	
	private List<String> initStartProduction(List<String[]> grammatik) {
		List<String> list = new ArrayList<String>();
		list.add(grammatik.get(0)[0]);
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
			if (productionString.indexOf(sideSplitter) == productionString.length() - 1) {
				return new String[] {productionString.substring(0, productionString.length() - 1), ""};
			}
			else {
				return productionString.split(sideSplitter);
			}
		});
	}

}
