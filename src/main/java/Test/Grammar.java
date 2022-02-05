package Test;

import Lab1.TransitionRule;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Grammar {

    private final List<Lab1.TransitionRule> rules;

    Grammar(List<Lab1.TransitionRule> rules) {
        this.rules = rules;
    }

    public static Grammar parseFile(String inputPath) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(inputPath));

        List<Lab1.TransitionRule> rules = new ArrayList<>();
        for (String line : lines) {
            Lab1.TransitionRule rule = Lab1.TransitionRule.parse(line);
            rules.add(rule);
        }
        return new Grammar(rules);
    }

    public List<TransitionRule> getRules() { return rules; }
}
