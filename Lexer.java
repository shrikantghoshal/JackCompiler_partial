import java.io.*;
import java.util.ArrayList;

public class Lexer {
    public PushbackReader pushbackReader = null;

    private int linNum;
    int c;

    private ArrayList<String> keywords;
    private char[] initCharArray;

    public Lexer(String userInput) {
        init(userInput);
        setKeywordArray();

    }

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

        // Checking if the file is jack format
        // String[] fileID = inputFilename.split("[.]");

        // if (fileID[1].equals("jack")) {
        // linNum = 1;
        // return true;
        // } else {
        // System.out.println("Not a .jack file - Please enter the filepath of a valid
        // .jack file onlto the terminal");
        // System.exit(1);
        // return false;
        // }

    }

    public void setKeywordArray() {
        keywords = new ArrayList<String>(21);

        // Program components
        keywords.add("class");
        keywords.add("constructor");
        keywords.add("method");
        keywords.add("function");

        // Primitive types
        keywords.add("int");
        keywords.add("boolean");
        keywords.add("char");
        keywords.add("void");

        // Variab;e declarations
        keywords.add("var");
        keywords.add("static");
        keywords.add("field");

        // Statements
        keywords.add("let");
        keywords.add("do");
        keywords.add("if");
        keywords.add("else");
        keywords.add("while");
        keywords.add("return");

        // Constant values
        keywords.add("true");
        keywords.add("false");
        keywords.add("null");

        // Object reference
        keywords.add("this");
    }

    public int getLinNum() {
        return linNum;
    }

    public Token getNextToken() throws IOException {
        Token token = new Token();

        // Loop to consume any leading whitespaces
        while (Character.isWhitespace((char) c) && (c != -1)) {
            if (c == '\r')
                linNum++;
            c = pushbackReader.read();
            pushbackReader.unread(c);
        }

        // Loop to consume any leading comments
        Integer comType = 0;
        do {
            if ((char) c == '/' && (c != -1)) {
                c = pushbackReader.read();
                pushbackReader.unread(c);
                if ((char) c == '/') { // Check for "//" Single line comment
                    comType = 1;
                    if (c == '\r') {
                        linNum++;
                        c = pushbackReader.read();
                        pushbackReader.unread(c);
                        comType = 0;
                    }

                } else if ((char) c == '*') { // Check for "/*" Multi-line comment start
                    comType = 2;
                    c = pushbackReader.read();
                    pushbackReader.unread(c);
                    if ((char) c == '*') { // Check for "/**" API Documentation comment
                        c = pushbackReader.read();
                        pushbackReader.unread(c);
                        if ((char) c == '/') {
                            linNum++;
                            c = pushbackReader.read();
                            pushbackReader.unread(c);
                            comType = 0;
                        } else {
                            c = pushbackReader.read();
                            pushbackReader.unread(c);
                        }
                    } else if ((char) c == '/') {
                        linNum++;
                        c = pushbackReader.read();
                        pushbackReader.unread(c);
                        comType = 0;
                    } else {
                        c = pushbackReader.read();
                        pushbackReader.unread(c);
                    }
                }
            }
        } while ((comType != 0) || (comType == 1 && (char) c != '\r'));

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
                c = pushbackReader.read();
                pushbackReader.unread(c);
            }

            token.setLineNumber(linNum);
            for (int i = 0; i < 21; i++) {
                if (token.getLexeme().equals(keywords.get(i))) {
                    token.setTokenType(Token.TokenType.KEYWORD);
                    return token;
                }
            }
            token.setTokenType(Token.TokenType.IDENTIFIER);
            return token;
        }

        if (Character.isDigit((char) c)) {
            token.setNewLexeme();
            while ((c != -1) && Character.isDigit((char) c)) {
                token.setLexeme(String.valueOf((char) c));
                c = pushbackReader.read();
                pushbackReader.unread(c);
            }
            token.setTokenType(Token.TokenType.CONSTANT);
            return token;
        }

        token.setNewLexeme();
        token.setLexeme(String.valueOf((char) c));
        token.setTokenType(Token.TokenType.SYMBOL);
        return token;

    }

    // public static void main(String[] args){
    // Lexer trialLexer = new Lexer(args[0]);

    // }
}