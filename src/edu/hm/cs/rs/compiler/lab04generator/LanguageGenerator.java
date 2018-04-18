/* (C) 2017, R. Schiedermeier, rs@cs.hm.edu
 * Java 1.8.0_121, Linux x86_64 4.8.15
 * bluna (Intel Core i7-5600U CPU/2.60GHz, 4 cores, 2000 MHz, 16000 MByte RAM)
 **/
package edu.hm.cs.rs.compiler.lab04generator;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/** Interface fuer Klassen, die Sprachen von Typ-1-Grammatiken generieren.
 * Grammatiken sind als String ohne Whitespace geschrieben:
 * Das erste Zeichen trennt linke und rechte Seite einer Produktion, das zweite Produktionen.
 * Grossbuchstaben sind Nichtterminale.
 * Alle anderen druckenden ASCII-Zeichen (Codes 33-126) sind Terminale.
 * Ein Beispiel: Grammatik: =,S=Sa,S=x (Seitentrenner =, Produktionentrenner ,).
 * Woerter bis zur Laenge 4: x, xa, xaa, xaaa.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version 2017-04-07
 */
public interface LanguageGenerator {
    /** Generiert alle Woerter der Sprache der Grammatik bis zur gegebenen Laenge.
     * Start ist die linke Seite der ersten Produktion.
     * @param grammar Typ-1-Grammatik.
     * @param uptoLength Maximale Laenge der Woerter der Sprache der Grammatik.
     * @return Alle Woerter der Sprache bis (einschliesslich) zur Laenge uptoLength,
     * sortiert nach steigender Laenge und bei gleicher Laenge alphabetisch.
     */
    Stream<String> generate(Stream<String[]> grammar, int uptoLength);

    /** Zerlegt einen String mit einer Typ-1-Grammatik.
     * @param grammarString String mit einer Grammatik.
     * @return Produktionen der Grammatik.
     * Jede Produktion ist ein Array mit 2 Elementen, der linken und der rechten Seite.
     */
    Stream<String[]> parseGrammar(String grammarString);

    /** Gibt die Grammatikproduktionen zeilenweise in lesbarer Form aus.
     * @param grammar Ein Grammatik.
     * @return Die Grammatik grammar.
     */
    static Stream<String[]> prettyprint(Stream<String[]> grammar) {
        return System.getProperty("prettyprint") == null
              ? grammar
              : grammar.peek(production -> System.err.println(production[1]
                              .chars()
                              .mapToObj(chr -> Character.toString((char)chr))
                              .collect(Collectors.joining(" ", production[0] + " -> ", ""))));
    }

}