package Lab1;

import Test1.TransitionRuleTest;
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

        grammar.getRules().stream()
                .map(TransitionRuleTest::getFrom)
                .map(State::new)
                .forEach(state -> states.put(state.getName(), state));

        for (TransitionRuleTest rule : grammar.getRules()) {
            State currentState = states.get(rule.getFrom());
            String transition = rule.getTo();

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
        int positionWhenDestinationNull = 0;
        List<Character> listOfNonTerminalVariables = new ArrayList<>();
        Map<Character, Integer> charactersPositions = new HashMap<>();

        for (Map.Entry<Character, State> entry : states.entrySet()) {
            Character key = entry.getKey();
            charactersPositions.put(key, position);
            listOfNonTerminalVariables.add(key);
            positionWhenDestinationNull = charactersPositions.size();
            position += 1;
        }

        char[][] matrix = new char[listOfNonTerminalVariables.size() + finalStates][listOfNonTerminalVariables.size() + finalStates];

        for (Map.Entry<Character, State> entry : states.entrySet()) {
            Character key = entry.getKey();
            State value = entry.getValue();
            for (Map.Entry<Character, State> transitionsEntry : value.transitions.entries()) {
                char transition = transitionsEntry.getKey();
                char destination = transitionsEntry.getValue().name;
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
        int idx = 0;

        while (idx < input.length()) {
            boolean lastSymbol = input.charAt(idx) == input.charAt(input.length() - 1);
            Optional<Collection<State>> stateOptional =
                    currentState.getNextStateFor(input.charAt(idx), lastSymbol);
            if (stateOptional.get().isEmpty()) {
                break;
            }

            currentState = stateOptional.get().iterator().next();
            idx++;
        }

        return currentState.equals(State.FINAL) && idx == input.length();
    }

    static class State {

        private final char name;
        private final Multimap<Character, State> transitions;

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
            return lastSymbol ? Optional.ofNullable(
                    transitions.get(ch).stream().filter(f -> f.name == '-').collect(Collectors.toList())) :
                    Optional.ofNullable(transitions.get(ch));
        }
    }
}