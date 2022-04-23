package Lab5;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class CFG {
    private final Map<String, List<String>> productions;
    Set<Character> terminal;
    Set<String> nonTerminal;

    CFG(String input) throws IOException {
        this.productions = new LinkedHashMap<>();
        this.terminal = new LinkedHashSet<>();
        this.nonTerminal = new LinkedHashSet<>();
        buildFromInput(input);
        print();
    }

    void buildFromInput(String input) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(input));

        for (String line : lines) {
            String currentState = String.valueOf(line.charAt(0));
            if (Character.isUpperCase(currentState.charAt(0))) nonTerminal.add(currentState);

            for (int i = 3; i < line.length(); i++) {
                if (Character.isLowerCase(line.charAt(i))) terminal.add(line.charAt(i));
            }

            String transition = line.substring(3);

            if (productions.containsKey(currentState)) {
                productions.get(currentState).add(transition);
            }else {
                List<String> transitions = new ArrayList<String>(){{add(transition);}};
                productions.put(currentState, transitions);
            }
        }
    }

    void print(){
        for(Map.Entry<String, List<String>> production: productions.entrySet()){
            System.out.print(production.getKey() + " -> ");
            for(String transition: production.getValue()){
                System.out.print(transition + "  ");
            }
            System.out.println();
        }
    }

    public Map<String, List<String>> getProductions(){
        return productions;
    }
}