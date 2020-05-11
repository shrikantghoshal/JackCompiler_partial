import java.io.*;
import java.util.*;

public class MyLexer {

    private int linNum;

    public List<String> charCode = new ArrayList();
    public List<MyToken> tokenList;

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
        String noComments = rmvMultilineCom.replaceAll("/\\*(?:.|[\\n\\r])*?\\*/", " ");
        
        charCode.addAll(Arrays.asList(noComments.split("(?!^)")));
        allTokens();

        lineRead.close();
        fread.close();

    }

    public char consumeChar() throws EndOfFileException {
        if (charCode.isEmpty())
            throw new EndOfFileException("List is empty");
        char temp = charCode.get(0).charAt(0);
        charCode.remove(0);
        return temp;
    }

    public char peekChar() throws EndOfFileException {
        if (charCode.isEmpty())
            throw new EndOfFileException("List is empty");
        return charCode.get(0).charAt(0);
    }

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

    public MyToken.TokenType classifyToken(char input) throws EndOfFileException {
        if (Character.isDigit(input))
            return MyToken.TokenType.INT_LITERAL;
        if (Character.isLetter(input))
            return MyToken.TokenType.IDENTIFIER;
        if (input == '\"') {
            return MyToken.TokenType.STRING_LITERAL;
        } else
            return MyToken.TokenType.SYMBOL;
    }

    public MyToken consumeIntegerToken() throws EndOfFileException {
        StringBuilder stringBuild = new StringBuilder();

        while (Character.isDigit(peekChar())) {
            stringBuild.append(consumeChar());
        }
        return new MyToken(stringBuild.toString(), MyToken.TokenType.INT_LITERAL);

    }

    public MyToken consumeStringToken() throws EndOfFileException {
        StringBuilder stringBuild = new StringBuilder();

        while (peekChar() != '\"') {

            stringBuild.append(consumeChar());
        }

        stringBuild.append(consumeChar());// to consume the ending '"'

        return new MyToken(stringBuild.toString(), MyToken.TokenType.STRING_LITERAL);

    }

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

    public MyToken consumeSymbolToken() throws EndOfFileException {
        String symbolLexeme = String.valueOf(consumeChar());

        return new MyToken(symbolLexeme, MyToken.TokenType.SYMBOL);
    }

    public MyToken GetNextToken() {
        MyToken temp = tokenList.get(0);
        tokenList.remove(0);
        return temp;
    }

    public MyToken PeekNextToken() {
        return tokenList.get(0);
    }

    public static void main(String[] args) {
        MyLexer trialLexer = new MyLexer("Main.jack");

        while (trialLexer.PeekNextToken().getTokenType() != MyToken.TokenType.EOF) {
            // System.out.println(trialLexer.getNextToken().toString());
            System.out.println(trialLexer.GetNextToken());
        }

    }
}