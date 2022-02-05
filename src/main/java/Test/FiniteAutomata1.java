package Test;

import Lab1.TransitionRule;

import java.util.*;

public class FiniteAutomata1 {

    private static final char STATE_START = 'S';
    private static final char STATE_FINISH = '-';
    private static Map<Character, State> states = new LinkedHashMap<>();
    private static int finalStates = 0;

    private final State start;
    private FiniteAutomata1(State start) {
        this.start = start;
    }

    public static FiniteAutomata1 buildFromGrammar(Grammar grammar) {

        grammar.getRules().stream()
                .map(Lab1.TransitionRule::getFrom)
                .map(State::new)
                .forEach(state -> states.put(state.getName(), state));

        for (TransitionRule rule : grammar.getRules()) {
            State currentState = states.get(rule.getFrom());
            String transition = rule.getTo();

            char using = transition.charAt(0);

            State nextState;
            if (transition.length() == 1) {
                nextState = State.FINAL;
                finalStates += 1;
            }
            else nextState = states.get(transition.charAt(1));

            currentState.addTransition(using, nextState);
        }

        State startState = states.get(STATE_START);
        return new FiniteAutomata1(startState);
    }

    public static void printMatrix(){
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
            for (Map.Entry<Character, State> transitionsEntry : value.transitions.entrySet()) {
               char transition = transitionsEntry.getKey();
               char destination = transitionsEntry.getValue().name;
               if (key.equals(destination)){
                   matrix[charactersPositions.get(key)][charactersPositions.get(key)] = transition;
               }  else if (!(destination == STATE_FINISH)){
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
            Optional<State> stateOptional =
                    currentState.getNextStateFor(input.charAt(idx));
            if (!stateOptional.isPresent()) {
                break;
            }

            currentState = stateOptional.get();
            idx++;
        }

        return currentState.equals(State.FINAL) && idx == input.length();
    }

    static class State {

        private final char name;
        private final Map<Character, State> transitions;

        State(char name) {
            this.name = name;
            this.transitions = new HashMap<>();
        }

        void addTransition(char using, State next) {
            transitions.put(using, next);
        }

        static final State FINAL = new State(STATE_FINISH);

        public char getName() {
            return name;
        }

        Optional<State> getNextStateFor(char ch) {
            return Optional.ofNullable(transitions.get(ch));
        }

    }
}