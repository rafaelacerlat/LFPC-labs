package Lab5;

import com.google.common.collect.*;
import java.io.*;
import java.util.*;

public class LL1 {
    private CFG grammar;
    Map<String, Set<Character>> first = new LinkedHashMap<>();
    Map<String, Set<Character>> follow = new LinkedHashMap<>();
    Table<String, Character, String> predictiveMatrix = HashBasedTable.create();

    LL1 (CFG grammar) throws IOException {
        this.grammar = grammar;
        algorithm();
    }

    void algorithm() throws IOException {
        removeLeftRecursion();
        System.out.println();
        grammar.print();
        getFirstAndFollow();
        getPMatrix();
        parsing();

    }

    void removeLeftRecursion() {
        Map<String, List<String>> newGrammar = new LinkedHashMap<>();
        String newState;

        for (Map.Entry<String, List<String>> p : grammar.getProductions().entrySet()) {
            Collections.sort(p.getValue());
            String prefix = p.getValue().get(0);
            for (int i = 1; i < p.getValue().size(); i++) {
                String transition = p.getValue().get(i);
                if (transition.startsWith(prefix)) {
                    newState = p.getKey() + "1";
                    grammar.nonTerminal.add(newState);
                    if (newGrammar.containsKey(newState)) {
                        newGrammar.get(newState).add(transition.replace(prefix, ""));
                    }else {
                        List<String> transitions = new ArrayList<String>(){{add(transition.replace(prefix, "")); add("0");}};
                        newGrammar.put(newState, transitions);
                    }
                    grammar.getProductions().replace(p.getKey(), Collections.singletonList( prefix + newState));
                }
            }
        }
        grammar.getProductions().putAll(newGrammar);
    }

    void getFirstAndFollow(){
        System.out.format("\n%10s%20s%20s\n", "states", "first", "follow");

        for(String state: grammar.nonTerminal){
            getFirst(state);
        }

        for(String state: grammar.nonTerminal) {
            if (state.equals("S")) follow.put(state, Collections.singleton('$'));
            else getFollow(state);

            System.out.format("%10s%20s%20s\n", state, first.get(state), follow.get(state));
        }
    }

    void getFirst(String state){
        if (!first.containsKey(state)) first.put(state, new LinkedHashSet<>());

        for(String transition: grammar.getProductions().get(state)) {
            transition = processTransition(state, transition);
            if (transition.equals("")){
                first.get(state).add('0');
            }
        }
    }

    String processTransition(String state, String transition){
        //transition starts with terminal or empty
        if (grammar.terminal.contains(transition.charAt(0)) || transition.equals("0")) {
                first.get(state).add(transition.charAt(0));
                return transition;
        }

        if (transition.length() >= 2){
            if (transition.charAt(1) == '1'){ //for cases like non-terminal state B1
               getFirst(transition.substring(0,1));
               first.get(state).addAll(first.get(transition.substring(0,1)));
               if (first.get(transition.substring(0,1)).contains('0')) {
                   first.get(state).remove('0');
                   return processTransition(state, transition.substring(2));
               }
            } else {
                getFirst(String.valueOf(transition.charAt(0)));
                first.get(state).addAll(first.get(String.valueOf(transition.charAt(0))));
                if (first.get(String.valueOf(transition.charAt(0))).contains('0')) {
                    first.get(state).remove('0');
                    return processTransition(state, transition.substring(1));
                }
            }
        }
        return transition;

    }

    void getFollow(String state){
        if (!follow.containsKey(state)) follow.put(state, new LinkedHashSet<>());

        String suffix;
        for (Map.Entry<String, List<String>> p : grammar.getProductions().entrySet()) {
            for (String transition : grammar.getProductions().get(p.getKey())) {
                if(transition.contains(state) ) {
                    suffix = transition.substring(transition.lastIndexOf(state) + state.length());
                    suffix = processSuffix(suffix, state);
                    if (suffix.equals("")){
                        if (!follow.containsKey(p.getKey())) getFollow(p.getKey());
                        follow.get(state).addAll(follow.get(p.getKey()));
                    }
                }

            }
        }
    }

    //get first of follow
    String processSuffix(String suffix, String state){
        if (!suffix.equals("") && !suffix.equals("1")) {
            if (grammar.nonTerminal.contains(String.valueOf(suffix.charAt(0)))) {
                if (grammar.nonTerminal.contains(suffix.charAt(0) + "1")) { //B1
                    follow.get(state).addAll(first.get(suffix.charAt(0) + "1"));
                    if (first.get(suffix.charAt(0) + "1").contains('0')) follow.get(state).remove('0');
                    return processSuffix(suffix.substring(2), state);
                } else {
                    follow.get(state).addAll(first.get(String.valueOf(suffix.charAt(0))));
                    if (first.get(String.valueOf(suffix.charAt(0))).contains('0')) follow.get(state).remove('0');
                    return processSuffix(suffix.substring(1), state);
                }
            }else if (grammar.terminal.contains(suffix.charAt(0))) {
                follow.get(state).add(suffix.charAt(0));
            }
        }
        return suffix;
    }

    //Predictive Matrix
    void getPMatrix(){

        System.out.format("\n\n%10s", "states");
        for (Character terminal: grammar.terminal){
            System.out.format("%10s", terminal);
        }
        System.out.format("%10s\n", "$");

        for(String state: grammar.nonTerminal){
            for(String transition: grammar.getProductions().get(state)){
                if (grammar.terminal.contains(transition.charAt(0))) {
                    predictiveMatrix.put(state, transition.charAt(0), transition);
                } else if (transition.equals("0")) {
                    for (Character f: follow.get(state)) {
                        predictiveMatrix.put(state, f, "0");
                    }
                } else {
                    for(Character c: first.get(String.valueOf(transition.charAt(0)))){
                        if(c.equals('0')) {
                            for (Character f: follow.get(state)) {
                                predictiveMatrix.put(state, f,"0");
                            }
                        }else{
                            predictiveMatrix.put(state, c, transition);
                        }
                    }
                }
            }
            System.out.format("%10s", state);


            for(Character terminal: grammar.terminal){
                if(predictiveMatrix.contains(state, terminal)) System.out.format("%10s", predictiveMatrix.get(state, terminal));
                    else System.out.format("%10s", "-");
            }
            if(predictiveMatrix.contains(state, '$')) System.out.format("%10s", predictiveMatrix.get(state, '$'));
                else System.out.format("%10s", "-");
            System.out.println();
        }
    }

    void parsing() throws IOException {
        System.out.print("\n Enter a word for parsing:  ");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String word = reader.readLine() + "$";
        String stack = "S$";
        String action;
        boolean valid = true;

        System.out.format("\n\n\n%10s%20s%20s\n", "stack", "input", "action");
        System.out.format("\n%10s%20s", stack, word);

        while(valid) {
            valid = false;
            outerloop:
            for (String state : grammar.nonTerminal) {
                for (Character terminal: predictiveMatrix.columnKeySet()) {
                    if (!stack.startsWith(state + "1") && stack.startsWith(state) && word.startsWith(terminal + "")) {
                        if (predictiveMatrix.get(state,terminal).equals("0")) {
                            action = "";
                            System.out.format("%20s\n", "0");
                        }
                        else {
                            action = predictiveMatrix.get(state,terminal);
                            System.out.format("%20s\n", action);
                        }
                        stack = stack.replaceFirst(state, action);
                        System.out.format("\n%10s%20s", stack, word);
                        valid = true;
                    } else if (stack.charAt(0) == word.charAt(0)) {
                        if (word.equals("$")) break outerloop;
                        String c = String.valueOf(stack.charAt(0));
                        stack = stack.replaceFirst(c, "");
                        word = word.replaceFirst(c, "");
                        action = "-";
                        System.out.format("%20s\n", action);
                        System.out.format("\n%10s%20s", stack, word);
                        valid = true;
                    }
                }
            }
        }

        System.out.println(!word.equals("$") ? "\n\n\n Invalid word!" : "\n\n\n Valid word!");
    }
}
