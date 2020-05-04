import java.io.*;
import java.util.ArrayList;

public class MyLexer {
    public PushbackReader pushbackReader = null;

    private int linNum;
    int c;

    public ArrayList<MyToken> allTokens;

    public enum Keywords {
        // Program components
        CLASS("class"), CONSTRUCTOR("constructor"), METHOD("method"), FUNCTION("function"),

        // Primitive types
        INT("int"), BOOL("boolean"), CHAR("char"), VOID("void"),

        // Variab;e declarations
        VAR("var"), STATIC("static"), FIELD("field"),

        // Statements
        LET("let"), DO("do"), IF("if"), ELSE("else"), WHILE("while"), RETURN("return"),

        // Constant values
        TRUE("true"), FALSE("false"), NULL("null"),

        // Object reference
        THIS("this");

        private String keywordName;

        Keywords(String keywordClass) {
            this.keywordName = keywordClass;
        }

        public String getKeywordName() {
            return keywordName;
        }
    }

    public MyLexer(String userInput) {
        Boolean initiate = init(userInput);

        if (initiate == false)
            System.exit(0);

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
            System.exit(0);
        }

        // Checking if the file is jack format
        String[] fileID = inputFilename.split("[.]");

        if (fileID[1].equals("jack")) {
            linNum = 1;
            return true;
        } else {
            System.out.println("Not a .jack file - Please enter the filepath of a valid .jack file onlto the terminal");
            return false;
        }

    }

    public int getLinNum() {
        return linNum;
    }

    public MyToken GetNextToken() throws IOException {
        MyToken token = new MyToken();

        // Loop to consume any leading whitespaces
        while (Character.isWhitespace((char) c) && (c != -1)) {
            if (c == '\r')
                linNum++;
            c = pushbackReader.read();
        }

        // Loop to consume any leading comments
        Integer comType = 0;
        do {
            if ((char) c == '/' && (c != -1)) {
                c = pushbackReader.read();
                if ((char) c == '/') { // Check for "//" Single line comment
                    comType = 1;
                    if (c == '\r') {
                        linNum++;
                        c = pushbackReader.read();

                        comType = 0;
                    }

                } else if ((char) c == '*') { // Check for "/*" Multi-line comment start
                    comType = 2;
                    c = pushbackReader.read();

                    if ((char) c == '*') { // Check for "/**" API Documentation comment
                        c = pushbackReader.read();

                        if ((char) c == '/') {
                            linNum++;
                            c = pushbackReader.read();
                            comType = 0;
                        } else {
                            c = pushbackReader.read();
                        }
                    } else if ((char) c == '/') {
                        linNum++;
                        c = pushbackReader.read();
                        comType = 0;
                    } else {
                        c = pushbackReader.read();
                    }
                }
            }
        } while ((comType != 0) || (comType == 1 && (char) c != '\r'));

        // EOF detection
        if (c == -1) {
            token.setTokenType(Token.TokenType.EOF);
            return token;
        }

        // Check for keywords or identifiers
        if (Character.isLetter((char) c)) {
            while ((c != -1) && Character.isLetterOrDigit((char) c) || c == '_') {
                token.setLexeme(String.valueOf((char) c));
                c = pushbackReader.read();
                // pushbackReader.unread(c);
            }

            for (Keywords kwd : Keywords.values()) {
                if (token.getLexeme().equals(kwd.getKeywordName())) {
                    token.setTokenType(MyToken.TokenType.KEYWORD);
                    // System.out.println(token);
                    return token;
                }
            }

            token.setTokenType(Token.TokenType.IDENTIFIER);
            // System.out.println(token);
            return token;
        }

        if (Character.isDigit((char) c)) {
            token.setNewLexeme();
            while ((c != -1) && Character.isDigit((char) c)) {
                token.setLexeme(String.valueOf((char) c));
                c = pushbackReader.read();
                // pushbackReader.unread(c);
            }
            token.setTokenType(Token.TokenType.CONSTANT);
            // System.out.println(token);
            return token;
        }

        token.setNewLexeme();
        token.setLexeme(String.valueOf((char) c));
        token.setTokenType(Token.TokenType.SYMBOL);
        // System.out.println(token);
        return token;

    }

    public MyToken PeekNextToken() throws IOException {
        MyToken token = new MyToken();

        // Loop to consume any leading whitespaces
        while (Character.isWhitespace((char) c) && (c != -1)) {
            if (c == '\r')
                linNum++;
            c = pushbackReader.read();
            // pushbackReader.unread(c);
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
                        // pushbackReader.unread(c);
                        comType = 0;
                    }

                } else if ((char) c == '*') { // Check for "/*" Multi-line comment start
                    comType = 2;
                    c = pushbackReader.read();
                    // pushbackReader.unread(c);
                    if ((char) c == '*') { // Check for "/**" API Documentation comment
                        c = pushbackReader.read();
                        // pushbackReader.unread(c);
                        if ((char) c == '/') {
                            linNum++;
                            c = pushbackReader.read();
                            // pushbackReader.unread(c);
                            comType = 0;
                        } else {
                            c = pushbackReader.read();
                            // pushbackReader.unread(c);
                        }
                    } else if ((char) c == '/') {
                        linNum++;
                        c = pushbackReader.read();
                        // pushbackReader.unread(c);
                        comType = 0;
                    } else {
                        c = pushbackReader.read();
                        // pushbackReader.unread(c);
                    }
                }
            }
        } while ((comType != 0) || (comType == 1 && (char) c != '\r'));

        // EOF detection
        if (c == -1) {
            token.setTokenType(Token.TokenType.EOF);
            token.setLineNumber(linNum);
            // System.out.println(token);
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
                    token.setTokenType(MyToken.TokenType.KEYWORD);
                    System.out.println("token");
                    return token;
                }
            }
            token.setTokenType(Token.TokenType.IDENTIFIER);
            // System.out.println(token);
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
            // System.out.println(token);
            return token;
        }

        token.setNewLexeme();
        token.setLexeme(String.valueOf((char) c));
        token.setTokenType(Token.TokenType.SYMBOL);
        // System.out.println(token);
        return token;

    }

    public static void main(String[] args) {
        Lexer trialLexer = new Lexer(args[0]);
        Integer count = 0;
        try {
            while (!trialLexer.PeekNextToken().equals(Token.TokenType.EOF)) {
                Token dispToken = trialLexer.GetNextToken();
                count++;
                System.out.println(dispToken);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}