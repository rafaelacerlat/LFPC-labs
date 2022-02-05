package Lab1;

import Test1.TransitionRuleTest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Grammar {

    private final List<TransitionRuleTest> rules;

    Grammar(List<TransitionRuleTest> rules) {
        this.rules = rules;
    }

    public static Grammar parseFile(String inputPath) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(inputPath));

        List<TransitionRuleTest> rules = new ArrayList<>();
        for (String line : lines) {
            TransitionRuleTest rule = TransitionRuleTest.parse(line);
            rules.add(rule);
        }
        return new Grammar(rules);
    }

    public List<TransitionRuleTest> getRules() { return rules; }
}
