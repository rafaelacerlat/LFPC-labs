package Lab2_1;

import java.io.IOException;

public class Main {

    static final String inputPath = "src/main/java/Lab2/variant6.txt";

    public static void main(String[] args) throws IOException {
        FiniteAutomata NFA = new FiniteAutomata();
        NFA.buildFromInput(inputPath);
        NFA.print();
        NFA.convertToDFA();
    }
}
