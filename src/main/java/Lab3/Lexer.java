package Lab3;

import java.util.*;
import java.lang.*;
import static Lab3.TokenType.*;
import static java.lang.Character.*;

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
                if (isAlphabetic(c)) {
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
        // a character that is either a letter or a number
        while (isAlphabetic(getCurrentChar()) || isDigit(getCurrentChar())) current++;

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

    private void addToken(TokenType type) {
        String value;
        if (type == string) {
            // trim the surrounding quotes.
            value = input.substring(start + 1, current - 1);
        }
        else { value = input.substring(start, current); }
        tokens.add(new Token(type, value));
    }

    void checkOrder(List<Token> tokens){
        for (int i = 0; i < tokens.size() - 1; i++) {
           if( tokens.get(i).getType() == tokens.get(i+1).getType()){
               System.out.println("There are 2 or more adjacent tokens of the same type in the source code!");
           }
        }
    }
}