import java.io.*;

public class Lexer {
    protected PushbackReader pushbackReader = null;

    int linNum;
    int c;

    public Lexer(Reader readIn) {
        this.pushbackReader = new PushbackReader(readIn);
    }

    public Token getNextToken() {
        Token token = new Token();

        c = this.pushbackReader.read();
        this.pushbackReader.unread(c);

        //Loop to consume any leading whitespaces
        while (Character.isWhitespace((char) c) && (c != -1)) {
            if (c == '\r')
                linNum++;
            pushbackReader.read();
            c = this.pushbackReader.read();
            this.pushbackReader.unread(c);
        }

        if(c==-1){
            token.type = Token.TokenType.EOF;
            token.lineNumber = linNum;
            return token;
        }

        // if(Character.isLetter((char)c)){
        //     token.lexeme = 
        // }
    }

}