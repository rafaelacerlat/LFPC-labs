package Lab1;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.util.*;
import java.util.stream.Collectors;

public class FiniteAutomata {

    private static final char STATE_START = 'S';
    private static final char STATE_FINISH = '-';
    private static Map<Character, State> states = new LinkedHashMap<>();
    private static int finalStates = 0;

    private final State start;

    private FiniteAutomata(State start) {
        this.start = start;
    }

    public static FiniteAutomata buildFromGrammar(Grammar grammar) {

        // Creating states hierarchy, where getFrom is key for states Map
        grammar.getRules().stream()
                .map(TransitionRule::getFrom)
                .map(State::new)
                .forEach(state -> states.put(state.getName(), state));

        for (TransitionRule rule : grammar.getRules()) {
            State currentState = states.get(rule.getFrom());
            String transition = rule.getTo();

            // Separating transition string into using and nextState

            char using = transition.charAt(0);

            State nextState;
            if (transition.length() == 1) {
                nextState = State.FINAL;
                finalStates += 1;
            } else nextState = states.get(transition.charAt(1));

            currentState.addTransition(using, nextState);
        }

        State startState = states.get(STATE_START);
        return new FiniteAutomata(startState);
    }

    public static void printMatrix() {
        int position = 0;
        int positionWhenDestinationNull = 0; // for final states index/position in matrix
        List<Character> nonTerminalVariables = new ArrayList<>();
        Map<Character, Integer> charactersPositions = new HashMap<>(); // nonTerminalVariables position in matrix

        for (Map.Entry<Character, State> entry : states.entrySet()) {
            Character key = entry.getKey();
            charactersPositions.put(key, position);
            nonTerminalVariables.add(key);
            positionWhenDestinationNull = charactersPositions.size();
            position += 1;
        }

        char[][] matrix = new char[nonTerminalVariables.size() + finalStates][nonTerminalVariables.size() + finalStates];

        for (Map.Entry<Character, State> entry : states.entrySet()) {
            Character key = entry.getKey(); // vertex1
            State value = entry.getValue();

            // iterating through all transitions
            for (Map.Entry<Character, State> transitionsEntry : value.transitions.entries()) {
                char transition = transitionsEntry.getKey(); // edge weight
                char destination = transitionsEntry.getValue().name; // vertex2
                if (key.equals(destination)) {
                    matrix[charactersPositions.get(key)][charactersPositions.get(key)] = transition;
                } else if (!(destination == STATE_FINISH)) {
                    charactersPositions.get(key);
                    matrix[charactersPositions.get(key)][charactersPositions.get(destination)] = transition;
                } else {
                    matrix[charactersPositions.get(key)][positionWhenDestinationNull] = transition;
                    positionWhenDestinationNull += 1;
                }
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

    public boolean test(String input) {
        State currentState = start;
        int index = 0;

        while (index < input.length()) {
            boolean lastSymbol = index == input.length() - 1; // if we reached the last symbol in given word
            Optional<Collection<State>> stateOptional =
                    currentState.getNextStateFor(input.charAt(index), lastSymbol);
            if (stateOptional.get().isEmpty()) {
                break;
            }

            currentState = stateOptional.get().iterator().next(); //stateOptional becomes currentState
            index++;
        }

        return currentState.equals(State.FINAL) && index == input.length();
    }

    static class State {

        private final char name;
        private final Multimap<Character, State> transitions; // Map that allows multiple value for the same key

        State(char name) {
            this.name = name;
            this.transitions = ArrayListMultimap.create();
        }

        void addTransition(char using, State next) {
            transitions.put(using, next);
        }

        static final State FINAL = new State(STATE_FINISH);

        public char getName() {
            return name;
        }

        Optional<Collection<State>> getNextStateFor(char ch, boolean lastSymbol) {
            return lastSymbol ? Optional.ofNullable( // if lastSymbol, it's taken as a finalState
                    transitions.get(ch).stream().filter(f -> f.name == '-').collect(Collectors.toList())) :
                    Optional.ofNullable(transitions.get(ch)); // get next state from given symbol/terminalVariable
        }
    }
}