
public class Token {
    public enum TokenType {
        SYMBOL, KEYWORD, IDENTIFIER, LITERAL, CONSTANT, EOF;
    }

    private String lexeme;
    private Integer lineNumber;
    private TokenType type;


    public Token() {
        lexeme = "";

    }

    public void setNewLexeme() {
        lexeme = "";
    }

    public void setLexeme(String lexemeRead) {
        lexeme += lexemeRead;
    }

    public void setLineNumber(Integer lineNumberCount) {
        lineNumber = lineNumberCount;
    }

    public void setTokenType(TokenType tokenRead) {
        type = tokenRead;
    }

    public String getLexeme() {
        return lexeme;
    }

    public Integer getLineNumber() {
        return lineNumber;
    }

    public TokenType getTokenType() {
        return type;
    }

}
