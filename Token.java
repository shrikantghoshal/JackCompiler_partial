import java.util.*;

public class Token {
     public enum TokenType {
         SYMBOL,
         KEYWORD,
         IDENTIFIER,
         CONSTANT,
         EOF;
     }
     public String lexeme;
     public Integer lineNumber;
     public TokenType type;

     public Token{
         lexeme = "";
     }
}

