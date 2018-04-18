package edu.hm.stiglmeier.andreas.sprachgenerator;

import java.util.stream.Stream;

import edu.hm.cs.rs.compiler.lab04generator.LanguageGenerator;

public class SprachGenerator implements LanguageGenerator {

	@Override
	public Stream<String> generate(Stream<String[]> grammar, int uptoLength) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Stream<String[]> parseGrammar(String grammarString) {
		String sideSplitter = grammarString.substring(0, 1);
		String productionSplitter = grammarString.substring(1, 2);
		return Stream.of(grammarString.split(productionSplitter)).skip(1).map(productionString -> productionString.split(sideSplitter));
	}

}
