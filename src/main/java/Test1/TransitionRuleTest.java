package Test1;


public class TransitionRuleTest {
    private final char from;
    private final String to;

    TransitionRuleTest(char from, String to) {
        this.from = from;
        this.to = to;
    }

    public static TransitionRuleTest parse(String input) {
        char from = input.charAt(0);
        String to = input.substring(3);

        return new TransitionRuleTest(from, to);
    }

    public char getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }
}
