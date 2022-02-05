package Test1;

import java.io.IOException;

public class MainTest {

    private static final String inputPath = "C:\\Users\\rafae\\Desktop\\FAF\\FAF-sem4\\LFPC\\lfpc-labs\\src\\main\\java\\Lab1\\grammarTest.txt";

    public static void main(String[] args) throws IOException {
        FiniteAutomataTest.printMatrix(FiniteAutomataTest.parseFile(inputPath));
    }
}
