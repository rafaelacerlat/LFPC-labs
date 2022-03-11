package Lab3;

import java.util.*;
import static Lab3.TokenType.*;

public class Lexer {
    private final String input;
    private int start = 0;
    private int current = 0;
    private final List<Token> tokens = new ArrayList<>();
    private static final Map<String, TokenType> keywords = new HashMap<>();
    static {
        keywords.put("void", VOID);
        keywords.put("int", INT);
        keywords.put("decimal", DECIMAL);
        keywords.put("string", STRING);
        keywords.put("boolean", BOOLEAN);
        keywords.put("return", RETURN);
        keywords.put("if", IF);
        keywords.put("else", ELSE);
        keywords.put("and", AND);
        keywords.put("true", TRUE);
        keywords.put("false", FALSE);
    }

    Lexer(String sourceCode) {
        this.input = sourceCode;
    }

    List<Token> tokenize() {
        while (!isEndOfFile()) {
            // at the beginning of the next lexeme
            start = current;
            getToken();
        }

        return tokens;
    }

    private void getToken() {
        char c = input.charAt(current++);
        switch (c) {
            case '(': addToken(left_parentheses); break;
            case ')': addToken(right_parentheses); break;
            case '{': addToken(left_bracket); break;
            case '}': addToken(right_bracket); break;
            case ',': addToken(comma); break;
            case '.': addToken(dot); break;
            case '-': addToken(minus); break;
            case '+': addToken(plus); break;
            case ';': addToken(semicolon); break;
            case '*': addToken(star); break;
            // slash
            case '/':
                if (match('/')) {
                    // A comment goes until the end of the line
                    while (getCurrentChar() != '\n' && !isEndOfFile()) current++;
                } else {
                    addToken(slash);
                }
                break;
            // two-char-tokens
            case '=':
                addToken(match('=') ? equal : assign);
                break;
            case '!':
                addToken(match('=') ? not_equal : not);
                break;
            case '<':
                addToken(match('=') ? less_equal : less);
                break;
            case '>':
                addToken(match('=') ? greater_equal : greater);
                break;

            // whitespace
            case ' ':
            case '\n':
                // Ignore whitespace.
                break;

            // string-start
            case '"': string(); break;

            default:
                // digit-start
                if (isDigit(c)) {
                    number();
                // identifier-start
                } else if (isLetter(c)) {
                    identifier();
                } else {
                    System.out.println( "Unexpected character: " + c);  // error
                }
                break;
        }
    }

    // identifier method
    private void identifier() {
        while (isAlphaNumeric(getCurrentChar())) current++;

    // keyword-type
        String value = input.substring(start, current);
        TokenType type = keywords.get(value);
        if (type == null) type = identifier;
        addToken(type);
    }

    // number method
    private void number() {
        while (isDigit(getCurrentChar())) current++;

        // Look for a fractional part.
        if (getCurrentChar() == '.' && isDigit(getNextChar())) {
            // skip over the "."
            current++;

            while (isDigit(getCurrentChar())) current++;
            addToken(decimal);
        }
        else addToken(integer);
    }

    // string method
    private void string() {
        while (getCurrentChar() != '"' && !isEndOfFile()) {
            current++;
        }

        if (isEndOfFile()) {
            System.out.println( "Unterminated string");
        }

        current++;
        addToken(string);
    }

    // isEndOfFile method
    private boolean isEndOfFile() { return current >= input.length();}


    // match method
    private boolean match(char expected) {
        if (getCurrentChar() != expected) return false;

        current++;
        return true;
    }

    // getCurrentChar method
    private char getCurrentChar() {
        if (isEndOfFile()) return '~';
        return input.charAt(current);
    }

    // getNextChar method
    private char getNextChar() {
        if (current + 1 >= input.length()) return '~';
        return input.charAt(current + 1);
    }

    // isLetter method
    private boolean isLetter(char c) {
        return (c >= 'a' && c <= 'z') ||
                (c >= 'A' && c <= 'Z');
    }

    // a character that is either a letter or a number
    private boolean isAlphaNumeric(char c) {
        return isLetter(c) || isDigit(c);
    }

    // isDigit method
    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }


    private void addToken(TokenType type) {
        String value;
        if (type == string) {
            // trim the surrounding quotes.
            value = input.substring(start + 1, current - 1);
        }
        else { value = input.substring(start, current); }
        tokens.add(new Token(type, value));
    }
}