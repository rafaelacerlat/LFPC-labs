package Lab2_1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class FiniteAutomata {
    static List<String> states = new LinkedList<>();
    static List<String> alphabet = new LinkedList<>();
    static String initialState = "q0";
    static List<String> finalStates = new LinkedList<>();
    static Map<String, Map<String, Set<String>>> NFA = new LinkedHashMap<>();
    static Map<Set<String>, Map<String, Set<String>>> DFA = new LinkedHashMap<>();


    public void buildFromInput(String inputPath) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(inputPath));

        for (int i = 0 ; i < lines.size() ; i++){
            String line = lines.get(i);
            if (i == 0){
                states = Arrays.asList(line.trim().split(" "));
            }else if (i == 1){
                alphabet = Arrays.asList(line.trim().split(" "));
            } else if (i == 2){
                finalStates = Arrays.asList(line.trim().split(" "));
            }
            else {
                String currentState = line.substring(1,3);
                String using = line.substring(4,5);
                String nextState = line.substring(7);

                Map<String, Set<String>> transitions = new HashMap<>();
                Set<String> nextStates = new HashSet<>();
                nextStates.add(nextState);

                // Completing the states map
                if (NFA.containsKey(currentState)) {
                    if (NFA.get(currentState).containsKey(using)){
                         NFA.get(currentState).get(using).add(nextState);
                    }else {
                        NFA.get(currentState).put(using, nextStates);
                    }
                } else {
                    transitions.put(using, nextStates);
                    NFA.put(currentState, transitions);
                }
            }
        }

    }

    public void print() {
        for (Map.Entry<String, Map<String, Set<String>>> stateRules : NFA.entrySet()) {
            for (Map.Entry<String, Set<String>> rule : stateRules.getValue().entrySet()) {
                System.out.printf("(%s, %s) = %s\n", stateRules.getKey(), rule.getKey(), rule.getValue());
            }
        }
        System.out.println();

    }


    public void convertToDFA() {
        Set<String> currentStates = new HashSet<>();
        currentStates.add(initialState);
        getNewStates(DFA, currentStates);
        printDFA(DFA);
    }

    public static void getNewStates(Map<Set<String>, Map<String, Set<String>>> DFA, Set<String> currentStates){
        if (!DFA.containsKey(currentStates)) {
            Map<String, Set<String>> newRules = new HashMap<>();
            for (String word : alphabet) {
                Set<String> newStates = new LinkedHashSet<>();
                for (String state : currentStates) {
                    if(NFA.containsKey(state) && NFA.get(state).containsKey(word)){
                        for (String value : NFA.get(state).get(word)) {
                            if (value != null)
                                newStates.add(value);
                        }
                    }
                }

                if (!newStates.isEmpty()) {
                    newRules.put(word, newStates);
                    DFA.put(currentStates, newRules);
                    getNewStates(DFA, newStates);
                }
            }
        }
    }

    public void printDFA(Map<Set<String>, Map<String, Set<String>>> DFA) {

        for (Map.Entry<Set<String>, Map<String, Set<String>>> stateRules : DFA.entrySet()) {
            for (Map.Entry<String, Set<String>> rule : stateRules.getValue().entrySet()) {
                if (stateRules.getKey().contains(initialState))
                    System.out.printf("-> (%s, %s) = %s\n", stateRules.getKey(), rule.getKey(), rule.getValue());
                else if (!Collections.disjoint(stateRules.getKey(), finalStates))
                    System.out.printf("* (%s, %s) = %s\n", stateRules.getKey(), rule.getKey(), rule.getValue());
                else
                    System.out.printf("  (%s, %s) = %s\n", stateRules.getKey(), rule.getKey(), rule.getValue());
            }

        }
    }

}
