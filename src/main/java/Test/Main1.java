package Test;

import java.io.IOException;

public class Main1 {

    private static final String inputPath = "C:\\Users\\rafae\\Desktop\\FAF\\FAF-sem4\\LFPC\\lfpc-labs\\src\\main\\java\\Lab1\\grammar.txt";

    public static void main(String[] args) throws IOException {
        Grammar grammar = Grammar.parseFile(inputPath);
        FiniteAutomata1 automata = FiniteAutomata1.buildFromGrammar(grammar);

        FiniteAutomata1.printMatrix();

        final String inputString = "cfbnnnn";
        boolean isValid = automata.test(inputString);

        System.out.println("Is valid = " + isValid);


    }
}
