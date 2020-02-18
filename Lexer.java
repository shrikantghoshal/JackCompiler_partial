import java.beans.PersistenceDelegate;
import java.io.*;

import org.graalvm.compiler.nodes.calc.IntegerBelowNode;

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

    public Lexer(String userInput) {
        init(userInput);
    }

    public void peekNextToken(Integer count) {
        try {
            count = this.pushbackReader.read();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            this.pushbackReader.unread(count);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public Token getNextToken() {
        Token token = new Token();

        peekNextToken(c);

        // Loop to consume any leading whitespaces
        while (Character.isWhitespace((char) c) && (c != -1)) {
            if (c == '\r')
                linNum++;
            pushbackReader.read();
            peekNextToken(c);
            // c = this.pushbackReader.read();
            // this.pushbackReader.unread(c);
        }

        // Loop to consume any leading comments
        Integer comType = 0;
        do {
            if ((char) c == '/' && (c != -1)) {
                peekNextToken(c);
                if ((char) c == '/') {
                    comType = 1;
                    if (c == '\r')
                        linNum++;
                    pushbackReader.read();
                    peekNextToken(c);

                } else if ((char) c == '*') {
                    comType = 2;
                    pushbackReader.read();
                    peekNextToken(c);
                    if ((char) c == '*') {
                        peekNextToken(c);
                        if ((char) c == '/') {
                            //to complete this
                        } else
                            peekNextToken(c);
                    }
                }
            }
        } while ((comType != 0) || (comType == 1 && (char) c != '\r'));
//review this poo (^-^)

        // EOF detection
        if (c == -1) {
            token.setTokenType(Token.TokenType.EOF);
            token.setLineNumber(linNum);
            return token;
        }

        // Check for keywords or identifiers
        if (Character.isLetter((char) c)) {
            token.setNewLexeme();
            while ((c != -1) && Character.isLetterOrDigit((char) c) || c == '_') {
                token.setLexeme(String.valueOf((char) c));
                pushbackReader.read();
                peekNextToken(c);
            }

            token.setLineNumber(linNum);
            token.setTokenType(Token.TokenType.IDENTIFIER);
            return token;
        }

        if (Character.isDigit((char) c)) {
            token.setNewLexeme();
            while ((c != -1) && Character.isDigit((char) c)) {
                token.setLexeme(String.valueOf((char) c));
                pushbackReader.read();
                peekNextToken(c);
            }
            token.setTokenType(Token.TokenType.CONSTANT);
            return token;
        }

        token.setNewLexeme();
        token.setLexeme(String.valueOf((char) c));
        token.setTokenType(Token.TokenType.SYMBOL);
        return token;
    }

}