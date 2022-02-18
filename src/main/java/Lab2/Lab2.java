package Lab2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Lab2 {
    static final String inputPath = "src/main/java/Lab2/example.txt";
    static List<String> states;
    static List<String> alphabet;
    static String initialState;
    static List<String> finalStates;
    static Map<String, Map<String, String>> NFA = new LinkedHashMap<>();
    static Map<String, Map<String, String>> DFA = new LinkedHashMap<>();

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(inputPath));

        for (int i = 0 ; i < lines.size() ; i++){
            String line = lines.get(i);
            if (i == 0){
                states = Arrays.asList(line.trim().split(" "));
            }else if (i == 1){
                alphabet = Arrays.asList(line.trim().split(" "));
            }else if (i == 2)
                initialState = line;
            else if (i == 3){
                finalStates = Arrays.asList(line.trim().split(" "));
            }
            else {
                String currentState = line.substring(1,3);
                String using = line.substring(4,5);
                String nextState = line.substring(7);

                Map<String, String> transitions = new HashMap<>();

                // Completing the states map
                if (NFA.containsKey(currentState)) {
                    if (NFA.get(currentState).containsKey(using)){
                        String nextStates = NFA.get(currentState).get(using);
                        nextStates += " " + nextState;
                        NFA.get(currentState).put(using, nextStates);
                    }else {
                        NFA.get(currentState).put(using, nextState);
                    }
                } else {
                    transitions.put(using, nextState);
                    NFA.put(currentState, transitions);
                }
            }
        }


        String currentState = initialState;
        nfaToDfa(DFA, currentState);

        for (Map.Entry<String, Map<String, String>> stateRules : DFA.entrySet()) {
            for (Map.Entry<String, String> rule : stateRules.getValue().entrySet()) {
                System.out.printf("([%s], %s) -> [%s]\n", stateRules.getKey(), rule.getKey(), rule.getValue());
            }
        }
    }

    public static void nfaToDfa (Map<String, Map<String, String>> DFA, String currentState){
        if (!DFA.containsKey(currentState)) {
            Map<String, String> newRules = new HashMap<>();
            for (String word : alphabet) {
                StringBuilder endState = new StringBuilder();
                for (String state : currentState.split(" ")) {
                    if(NFA.containsKey(state)){
                    String s = NFA.get(state).get(word);
                    if (s != null && !endState.toString().contains(s))
                        endState.append(" ").append(s);
                    }
                }

                String strState = endState.toString().trim();
                if (!strState.isEmpty() && !strState.equals(" ")) {
                    newRules.put(word, strState);
                    DFA.put(currentState, newRules);

                    nfaToDfa(DFA, strState);
                }
            }
        }
    }
}
