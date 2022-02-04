package Lab1;


public class TransitionRule {
    private final char from;
    private final String to;

    TransitionRule(char from, String to) {
        this.from = from;
        this.to = to;
    }

    public static TransitionRule parse(String input) {
        char from = input.charAt(0);
        String to = input.substring(3);

        return new TransitionRule(from, to);
    }

    public char getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }
}
