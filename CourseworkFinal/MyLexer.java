import java.io.*;
import java.util.*;

public class MyLexer {

    private int linNum;

    public List<String> charCode = new ArrayList();         //list of characters in the program
    public List<MyToken> tokenList;                         //list of tokens to be generated

    //enum of all keywords
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

    //lexer constructor
    public MyLexer(String userInput) {
        Boolean initiate = init(userInput);

        if (initiate == false)
            System.exit(0);

    }

    //checks if the input file exists, cna be opened, and is a 'fileName.jack' format
    public Boolean init(String inputFilename) {
        File inputFile = new File(inputFilename);
        if (!inputFile.exists()) {
            System.out.println("Specified file " + inputFilename + " doesn't exist");
            System.exit(0);
            return false;
        }

        try {
            removeComments(inputFilename);
            return true;
        } catch (IOException FileError) {
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

    //removeComments-->remove all comments (both inline and block comments) and split file by each individual character
    //add individual characters to the class member list and pass to allTokens
    public void removeComments(String inputFile) throws IOException {
        FileReader fread = new FileReader(inputFile);

        
        StringBuilder stringBuild = new StringBuilder();
        String separateLine = System.getProperty("line.separator"); // Java API
        LineNumberReader lineRead = new LineNumberReader(fread);
        String content = lineRead.readLine();
        while (content != (null)) {
            int inLineCom = content.indexOf("//");
            if (inLineCom != -1)
                content = content.substring(0, inLineCom); // Delete the inline coment from the starting double-slash

            stringBuild.append(content);
            stringBuild.append(separateLine);
            content = lineRead.readLine();
        }
        String rmvMultilineCom = stringBuild.toString();
        String noComments = rmvMultilineCom.replaceAll("/\\*(?:.|[\\n\\r])*?\\*/", " ");//java regex
        
        charCode.addAll(Arrays.asList(noComments.split("(?!^)")));//java regex
        allTokens();

        lineRead.close();
        fread.close();

    }

    //allTokens-->iterate through the list of characters and until list is empty 
    //Then call consumeToken and add token to list
    //when list of characters is empty, add EOF token manually to the end of the list of tokens created
    public List<MyToken> allTokens() {
        List<MyToken> tokens = new ArrayList<MyToken>();

        try {
            while (!charCode.isEmpty()) {
                tokens.add(consumeToken());
            }

        } catch (EndOfFileException e) {
            MyToken endOf = new MyToken("-1", MyToken.TokenType.EOF);
            tokens.add(endOf);
        }

        tokenList = tokens;
        return tokens;

    }

    //consumeToken--> consume whitespace characters and pass remaining tokens to classifyToken
    public MyToken consumeToken() throws EndOfFileException {
        while (Character.isWhitespace(peekChar())) {
            consumeChar();
        }
        char c = peekChar();

        switch (classifyToken(c)) {
            case INT_LITERAL:
                return consumeIntegerToken();
            case IDENTIFIER:
                return consumeWordToken();
            case STRING_LITERAL:
                return consumeStringToken();
            case SYMBOL:
                return consumeSymbolToken();
        }
        throw new RuntimeException("Unexpected state");
    }

    
    //classifyToken-->check the character input and return the type of token
    public MyToken.TokenType classifyToken(char input) throws EndOfFileException {
        if (Character.isDigit(input))
            return MyToken.TokenType.INT_LITERAL;
        if (Character.isLetter(input))
            return MyToken.TokenType.IDENTIFIER;
        if (input == '"') {
            return MyToken.TokenType.STRING_LITERAL;
        } else
            return MyToken.TokenType.SYMBOL;
    }

    //consumeIntegerToken-->consume all characters that are part of the integer token and returns the final token
    public MyToken consumeIntegerToken() throws EndOfFileException {
        StringBuilder stringBuild = new StringBuilder();

        while (Character.isDigit(peekChar())) {
            stringBuild.append(consumeChar());
        }
        return new MyToken(stringBuild.toString(), MyToken.TokenType.INT_LITERAL);

    }

    //consumeStringToken-->consume all characters that are part of the string token and returns the final token
    public MyToken consumeStringToken() throws EndOfFileException {
        StringBuilder stringBuild = new StringBuilder();
        stringBuild.append(consumeChar());

        while (peekChar() != '"') {

            stringBuild.append(consumeChar());
        }

        stringBuild.append(consumeChar());// to consume the ending '"'

        return new MyToken(stringBuild.toString(), MyToken.TokenType.STRING_LITERAL);

    }

    //consumeWordToken-->consume all characters that are part of the word token, determines the actual type (keyword or identifier) and returns the final token
    public MyToken consumeWordToken() throws EndOfFileException {
        StringBuilder stringBuild = new StringBuilder();

        while (Character.isLetterOrDigit(peekChar())) {
            stringBuild.append(consumeChar());
        }

        for (Keywords kwd : Keywords.values()) {
            if (stringBuild.toString().equals(kwd.getKeywordName())) {
                return new MyToken(stringBuild.toString(), MyToken.TokenType.KEYWORD);

            }
        }

        return new MyToken(stringBuild.toString(), MyToken.TokenType.IDENTIFIER);
    }

    //consumeSymbolToken-->consume all characters that are part of the symbol token and returns the final token
    public MyToken consumeSymbolToken() throws EndOfFileException {
        String symbolLexeme = String.valueOf(consumeChar());

        return new MyToken(symbolLexeme, MyToken.TokenType.SYMBOL);
    }


    //consumeChar-->returns the first element in the List of characters and removes that character from the list
    public char consumeChar() throws EndOfFileException {
        if (charCode.isEmpty())
            throw new EndOfFileException("List is empty");
        char temp = charCode.get(0).charAt(0);
        charCode.remove(0);
        return temp;
    }

    //peekChar-->returns the first element in the list of Characters
    public char peekChar() throws EndOfFileException {
        if (charCode.isEmpty())
            throw new EndOfFileException("List is empty");
        return charCode.get(0).charAt(0);
    }

    //GetNextToken-->returns the first token in the list of tokens and removes that token from the list
    public MyToken GetNextToken() {
        MyToken temp = tokenList.get(0);
        tokenList.remove(0);
        return temp;
    }

    //PeekNextToken-->returns the first token in the list of tokens
    public MyToken PeekNextToken() {
        return tokenList.get(0);
    }

    // testing the lexer -> convert the name from 'lexerTest' to 'main'
    public static void lexerTest(String[] args) {
        MyLexer trialLexer = new MyLexer(args[0]);

        while (trialLexer.PeekNextToken().getTokenType() != MyToken.TokenType.EOF) {
            System.out.println(trialLexer.GetNextToken());
        }

    }
}