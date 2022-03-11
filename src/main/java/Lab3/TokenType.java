package Lab3;

public enum TokenType {
    // Single-character tokens
    left_parentheses, right_parentheses, left_bracket, right_bracket,
    comma, dot, minus, plus, semicolon, slash, star,

    // One or two character tokens
    not, not_equal,
    assign, equal,
    greater, greater_equal,
    less, less_equal,

    // Literals
    identifier, string, integer, decimal,

    // Keywords
    VOID, INT, DECIMAL, BOOLEAN, STRING, RETURN,
    AND, ELSE, FALSE, IF, TRUE
}
