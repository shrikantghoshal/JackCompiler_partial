import java.io.*;
import java.util.ArrayList;

public class Lexer {
    public PushbackReader pushbackReader = null;

    private int linNum;
    int c;

    private ArrayList<String> keywords;
    private ArrayList<Character> initCharArray;

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

        String[] fileID = inputFilename.split("[.]");

        if (fileID[1].equals("jack")) {
            linNum = 1;
            return true;
        } else{
            System.out.println("Not a .jack file - Please enter the filepath of a valid .jack file onlto the terminal");
            System.exit(1);
            return false;}            

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
        

        // peekNextLexeme(c);

        // Loop to consume any leading whitespaces
        while (Character.isWhitespace((char) c) && (c != -1)) {
            if (c == '\r')
                linNum++;
            pushbackReader.read();
            
            // c = this.pushbackReader.read();
            // this.pushbackReader.unread(c);
        }

        // // Loop to consume any leading comments
        // Integer comType = 0;
        // do {
        // if ((char) c == '/' && (c != -1)) {
        // peekNextLexeme(c);
        // if ((char) c == '/') {
        // comType = 1;
        // if (c == '\r') {
        // linNum++;
        // pushbackReader.read();
        // peekNextLexeme(c);
        // comType = 0;
        // }

        // } else if ((char) c == '*') {
        // comType = 2;
        // pushbackReader.read();
        // peekNextLexeme(c);
        // if ((char) c == '*') {
        // peekNextLexeme(c);
        // if ((char) c == '/') {
        // linNum++;
        // pushbackReader.read();
        // peekNextLexeme(c);
        // comType = 0;
        // } else
        // peekNextLexeme(c);
        // } else if ((char) c == '/') {
        // linNum++;
        // pushbackReader.read();
        // peekNextLexeme(c);
        // comType = 0;
        // } else
        // peekNextLexeme(c);
        // }
        // }
        // } while ((comType != 0) || (comType == 1 && (char) c != '\r'));

        // EOF detection
        if (c == -1) {
            token.setTokenType(Token.TokenType.EOF);
            token.setLineNumber(linNum);
            return token;
        }

        // // Check for keywords or identifiers
        // if (Character.isLetter((char) c)) {
        // token.setNewLexeme();
        // while ((c != -1) && Character.isLetterOrDigit((char) c) || c == '_') {
        // token.setLexeme(String.valueOf((char) c));
        // pushbackReader.read();
        // peekNextLexeme(c);
        // }

        // token.setLineNumber(linNum);
        // token.setTokenType(Token.TokenType.IDENTIFIER);
        // return token;
        // }

        // if (Character.isDigit((char) c)) {
        // token.setNewLexeme();
        // while ((c != -1) && Character.isDigit((char) c)) {
        // token.setLexeme(String.valueOf((char) c));
        // pushbackReader.read();
        // peekNextLexeme(c);
        // }
        // token.setTokenType(Token.TokenType.CONSTANT);
        // return token;
        // }

        // token.setNewLexeme();
        // token.setLexeme(String.valueOf((char) c));
        // token.setTokenType(Token.TokenType.SYMBOL);
        // return token;
        // }

        // public static void main(String[] args){
        // Lexer trialLexer = new Lexer(args[0]);
        // }

    }
}