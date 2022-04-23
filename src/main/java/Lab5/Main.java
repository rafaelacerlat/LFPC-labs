package Lab5;


import java.io.IOException;

public class Main {
    private static final String inputPath = "src/main/java/Lab5/grammar.txt";
    public static void main(String[] args) throws IOException {
        CFG grammar = new CFG(inputPath);
        new LL1(grammar);
    }
}
