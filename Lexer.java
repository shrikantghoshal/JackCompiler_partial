import java.io.*;

public class Lexer {
    protected PushbackReader pushbackReader = null;

    int linNum;
    int c;

    public Boolean init(String inputFilename) {
        File inputFile = new File(inputFilename);
        if (!inputFile.exists()) {
            System.out.println("Specified file " + inputFilename + " doesn't exist");
            return false;
        }

        try {
            pushbackReader = new PushbackReader(new FileReader(inputFilename));
        } catch (Exception FileError) {
            System.out.println("Unable to open the specified file " + inputFilename);
        }
        linNum = 1;
        return true;
    }

    public Lexer{

    }

    public Token getNextToken() {
        Token token = new Token();

        c = this.pushbackReader.read();
        this.pushbackReader.unread(c);

        // Loop to consume any leading whitespaces
        while (Character.isWhitespace((char) c) && (c != -1)) {
            if (c == '\r')
                linNum++;
            pushbackReader.read();
            c = this.pushbackReader.read();
            this.pushbackReader.unread(c);
        }

        //EOF detection
        if (c == -1) {
            token.type = Token.TokenType.EOF;
            token.lineNumber = linNum;
            return token;
        }

        //Check for keywords or identifiers
        if (Character.isLetter((char) c)) {
            token.lexeme = "";
            while ((c!=-1)&&Character.isLetter((char)c)||Character.isDigit((char)c)||c=='_'){
                token.lexeme += (char)c;
                pushbackReader.read();
                c=pushbackReader.read();
                pushbackReader.unread(c);
            }
        token.lineNumber = linNum;
        token.type = Token.TokenType.IDENTIFIER;
        return token;
        }

        if(Character.isDigit((char)c)){
            token.lexeme = "";
            while((c!=-1)&&Character.isDigit((char)c)){
                token.lexeme += (char)c;
                pushbackReader.read();
                c = pushbackReader.read();
                pushbackReader.unread(c);
            }
            token.type = Token.TokenType.CONSTANT;
            return token;
        }

        token.lexeme = "";
        token.lexeme += (char)c;
        token.type = Token.TokenType.SYMBOL;
        return token;
    }

}