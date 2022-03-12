package Lab3;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;


public class Main {

    static final String inputPath = "src/main/java/Lab3/sourceCode.txt";

    public static void main(String[] args) throws IOException {
        String str = String.join("\n", Files.readAllLines(Paths.get(inputPath)));
        Lexer lexer = new Lexer(str);
        List<Token> tokens = lexer.tokenize();
        lexer.checkOrder(tokens);
        for (Token token : tokens) {
            System.out.println(token.print());
        }
    }
}
