
public class Token {
     public enum TokenType {
         SYMBOL,
         KEYWORD,
         IDENTIFIER,
         CONSTANT,
         EOF;
     }
     private String lexeme;
     private Integer lineNumber;
     private TokenType type;
     private KeywordType keyWord;

     public Token(){
         lexeme = "";

     }

     public void setNewLexeme(){
         lexeme = "";
     }

     public void setLexeme(String lexemeRead){
         lexeme += lexemeRead;
     }

     public void setLineNumber(Integer lineNumberCount){
         lineNumber = lineNumberCount;
     }

     public void setTokenType(TokenType tokenRead){
         type = tokenRead;
     }

     public void setKeywordType(KeywordType classification){
         keyWord = classification;
     }

     public String getLexeme(){
        return lexeme;
     }
     public Integer getLineNumber(){
         return lineNumber;
     }
     public TokenType getTokenType(){
        return type;
     }
     public KeywordType getKeywordType(){
         return keyWord;
     }

     public enum KeywordType{
        CLASS,
        CONSTRUCTOR,
        METHOD,
        FUNCTION,
        INT,
        BOOLEAN,
        CHAR,
        VOID,
        VAR,
        STATIC,
        FIELD,
        LET,
        DO,
        IF,
        ELSE,
        WHILE,
        RETURN,
        TRUE,
        FALSE,
        NULL,
        THIS;
     }
}
