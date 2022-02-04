package Lab1;

import java.io.IOException;

public class Main {

    private static final String inputPath = "C:\\Users\\rafae\\Desktop\\FAF\\FAF-sem4\\LFPC\\lfpc-labs\\src\\main\\java\\Lab1\\grammar.txt";

    public static void main(String[] args) throws IOException {
        FiniteAutomata.printMatrix(FiniteAutomata.parseFile(inputPath));
    }
}
