package Lab1;

import java.io.IOException;

public class Main {

    private static final String inputPath = "C:\\Users\\rafae\\Desktop\\FAF\\FAF-sem4\\LFPC\\lfpc-labs\\src\\main\\java\\Lab1\\grammarTest.txt";

    public static void main(String[] args) throws IOException {
        Grammar grammar = Grammar.parseFile(inputPath);
        FiniteAutomata automata = FiniteAutomata.buildFromGrammar(grammar);

        FiniteAutomata.printMatrix();

        final String inputString = "cfbnnnn";
        boolean isValid = automata.test(inputString);

        System.out.println("Is valid = " + isValid);


    }
}
