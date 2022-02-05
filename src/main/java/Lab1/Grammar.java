package Lab1;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Grammar {

    private final List<TransitionRule> rules;

    Grammar(List<TransitionRule> rules) {
        this.rules = rules;
    }

    public static Grammar parseFile(String inputPath) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(inputPath));

        List<TransitionRule> rules = new ArrayList<>();
        for (String line : lines) {
            TransitionRule rule = TransitionRule.parse(line);
            rules.add(rule);
        }
        return new Grammar(rules);
    }

    public List<TransitionRule> getRules() { return rules; }
}
