package edu.hm.stiglmeier.andreas.scanner;

import edu.hm.cs.rs.compiler.toys.base.BaseScanner;

public class TokenScanner extends  BaseScanner{


		private static final String eins = "abc-";
		private static final String sub = "-";
	
	
		public TokenScanner() {
			start("init");
			
			accept("sub");
			accept("bla");
			
			
			transition("init", sub, "sub");
			transition("init", eins, "bla");
			
		}
}
