import java.util.*;

public class Token {
     public enum TokenType {
         SYMBOL,
         KEYWORD,
         IDENTIFIERS,
         CONSTANTS,
         EOF;
     }
     public String lexeme;
     public Integer lineNumber;
     public TokenType type;

     public Token{
         lexeme = "";
     }
}

