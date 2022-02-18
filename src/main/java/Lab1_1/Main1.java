package Lab1_1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;


public class Main1 {



    static final String inputPath = "src/main/java/Lab1/grammar.txt";
    static Map<Character, Multimap<Character, Character>> states = new LinkedHashMap<>();
    static char STATE_FINAL = '-';
    static char STATE_START = 'S';


    public static void main(String[] args) throws IOException {

        List<String> lines = Files.readAllLines(Paths.get(inputPath));

        for (String line : lines) {
            char currentState = line.charAt(0);
            String transition = line.substring(3);

            // Separating transition string into using and nextState
            char using = transition.charAt(0);
            char nextState;

            if (transition.length() == 1) {
                nextState = STATE_FINAL;
            } else nextState = transition.charAt(1);

            Multimap<Character, Character> transitions = ArrayListMultimap.create();

            // Completing the states map
            if (states.containsKey(currentState)) {
                states.get(currentState).put(using, nextState);
            } else {
                transitions.put(using, nextState);
                states.put(currentState, transitions);
            }
        }

        printEdges();

        System.out.print("Input a word to be analysed: ");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String inputString = reader.readLine();
        if (test(inputString)) {
            System.out.println("The string '" + inputString + "' is accepted!");
        } else {
            System.out.println("The string '" + inputString + "' is not valid!");
        }
    }

    public static void printEdges(){
        int position = 0;
        int positionWhenStateFinal = 0;
        Map<Character, Integer> statesPositions = new HashMap<>();
        for (Map.Entry<Character, Multimap<Character, Character>> entry : states.entrySet()) {
            Character state = entry.getKey();
            statesPositions.put(state, position);
            positionWhenStateFinal = statesPositions.size();
            position += 1;
        }

        for (Map.Entry<Character, Multimap<Character, Character>> entry : states.entrySet()) {
            Character vertex1 = entry.getKey(); // start vertex

            for (Map.Entry<Character, Character> transitionsEntry : entry.getValue().entries()) {
                char edge = transitionsEntry.getKey(); // edge weight
                char vertex2 = transitionsEntry.getValue(); //end vertex
                int i = statesPositions.get(vertex1);

                if (vertex1.equals(vertex2)) {
                    System.out.println("("+ 'q' + i + "," + 'q' + i + ") = " + edge);
                }else if (!(vertex2 == STATE_FINAL)) {
                    int j = statesPositions.get(vertex2);
                    System.out.println("("+ 'q' + i + "," + 'q' + j + ") = " + edge);
                } else {
                    System.out.println("("+ 'q' + i + "," + 'q' + positionWhenStateFinal + ") = " + edge);
                    positionWhenStateFinal += 1;
                }
            }
        }
    }

    public static boolean test(String input) {
        char currentState = STATE_START;
        int i = 0;

        while (i < input.length()) {
            boolean lastChar = i == input.length() - 1;// if we reached the last character in given word

            if (states.get(currentState).containsKey(input.charAt(i))) {
                Iterator<Character> iterator = states.get(currentState).asMap().get(input.charAt(i)).iterator();
                if(lastChar) {
                    while (iterator.hasNext()) {
                        char nextState = iterator.next();
                        if ( nextState == STATE_FINAL) {
                            currentState = nextState;
                        }
                    }
                }else{
                    currentState = iterator.next();
                }
            }else break;
            i++;
        }

        return currentState == STATE_FINAL && i == input.length();
    }

}
