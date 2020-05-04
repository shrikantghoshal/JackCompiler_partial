public class MyToken {
    public String lexeme;

    public enum TokenType {
        SYMBOL, KEYWORD, IDENTIFIER, LITERAL, CONSTANT, EOF;

    }

    public TokenType type;


    public void setLexeme(String lexemeRead) {
        lexeme += lexemeRead;
    }

    public void setTokenType(TokenType tokenRead) {
        type = tokenRead;
    }

    public String getLexeme() {
        return lexeme;
    }

    public TokenType getTokenType() {
        return type;
    }
}