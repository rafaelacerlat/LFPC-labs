package Lab3;

public class Token {
    private TokenType type;
    private Object value;

    Token(TokenType type, Object value){
        this.type = type;
        this.value = value;
    }

    public TokenType getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }

    public String print() {
        return "Value: " + value + "  Type: " + type;
    }
}
