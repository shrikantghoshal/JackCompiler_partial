public class MyToken {
    public String lexeme;

    public enum TokenType {
        SYMBOL, KEYWORD, IDENTIFIER, INT_LITERAL, STRING_LITERAL, EOF;

    }

    //default constructor
    public MyToken(){
        lexeme = "";
    }

    //lexer construction didn't allow for line numbers so this is an ideal constructor that is not meant to be used
    public MyToken(String inputLexeme, TokenType inputType, int inputLine){
        setLexeme(inputLexeme);
        setTokenType(inputType);
        setLineNumber(inputLine);
    }

    //constructor that only allows for lexeme and tokenType
    public MyToken(String inputLexeme, TokenType inputType){
        setLexeme(inputLexeme);
        setTokenType(inputType);
    }
    public TokenType type;
    private int lineNumber;


    public void setLexeme(String lexemeRead) {
        lexeme = lexemeRead;
        // System.out.println(lexemeRead);
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

    public void setLineNumber(int inputLineNumber){
        lineNumber = inputLineNumber;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    @Override
    public String toString(){
        return String.format("< %s , %s >\n", getLexeme(), getTokenType());
        
    }
}