package Test1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class FiniteAutomataTest {

    public static void printMatrix(List<TransitionRuleTest> parsedFile) {
        List<Character> nonTerminalVariables = new LinkedList<>();
        List<Character> terminalVariables = new LinkedList<>();
        List<Character> destinations = new LinkedList<>();
        int finalStates = 0;

        for (TransitionRuleTest rule : parsedFile) {
            char source = rule.getFrom();
            char weight = rule.getTo().charAt(0);
            char destination;
            nonTerminalVariables.add(source);
            terminalVariables.add(weight);
            if(rule.getTo().length() == 1){
                destination = '-';
                finalStates += 1;
            } else {
                destination = rule.getTo().charAt(1);
            }
            destinations.add(destination);
        }

        List<Character> listOfStates = new ArrayList<>(new LinkedHashSet<>(nonTerminalVariables));
        int positionWhenDestinationNull = listOfStates.size();

        char[][] matrix = new char[listOfStates.size() + finalStates][listOfStates.size() + finalStates];

        for(int i = 0; i < nonTerminalVariables.size(); i++) {
            if (nonTerminalVariables.get(i).equals(destinations.get(i))) {
                int position = listOfStates.indexOf(nonTerminalVariables.get(i));
                matrix[position][position] = terminalVariables.get(i);
            } else if (destinations.get(i) != '-'){
                int positionFirst = listOfStates.indexOf(nonTerminalVariables.get(i));
                int positionSecond = listOfStates.indexOf(destinations.get(i));
                matrix[positionFirst][positionSecond] = terminalVariables.get(i);
            } else {
                int position = listOfStates.indexOf(nonTerminalVariables.get(i));
                matrix[position][positionWhenDestinationNull] = terminalVariables.get(i);
                positionWhenDestinationNull += 1;
            }
        }

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] == 0) {
                    matrix[i][j] = '-';
                }
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println("\n");
        }
    }

    public static List<TransitionRuleTest> parseFile(String inputPath) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(inputPath));

        List<TransitionRuleTest> rules = new ArrayList<>();
        for (String line : lines) {
            TransitionRuleTest rule = TransitionRuleTest.parse(line);
            rules.add(rule);
        }
        return rules;
    }
}
