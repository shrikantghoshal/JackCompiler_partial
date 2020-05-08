import java.io.*;
import java.util.*;


public class MyLexer {

    private int linNum;
  

    public List<String> charCode;

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

    public List<String> removeComments(String inputFile) throws IOException {
        FileReader fread = new FileReader(inputFile);

        String content = null;
        StringBuilder stringBuild = new StringBuilder();
        String separateLine = System.getProperty("line.separator"); // Java API
        LineNumberReader lineRead = new LineNumberReader(fread);
        content = lineRead.readLine();
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

        List<String> conversionStrList = new ArrayList<String>(Arrays.asList(noComments.split("\n")));

        for (int count = 0; count < conversionStrList.size(); count++) { // iterate through lines
            charCode = (Arrays.asList(conversionStrList.get(count).split("(?!^)")));
        }

        lineRead.close();
        fread.close();

        return conversionStrList;
    }

    public char peekChar() {
        char temp = charCode.get(0).charAt(0);
        charCode.remove(0);
        return temp;
    }

    public char consumeChar() {
        return charCode.get(0).charAt(0);
    }

    public MyToken consumeToken() {
        while (Character.isWhitespace(peekChar())) {
            consumeChar();
        }
        char c = peekChar();
        MyToken.TokenType typeC = classifyToken(c);
        switch (typeC) {
            case INT_LITERAL:
                consumeIntegerToken();
                break;
            case IDENTIFIER:
                consumeWordToken();
                break;
            case STRING_LITERAL:
                consumeStringToken();
                break;
            case SYMBOL:
                consumeSymbolToken();
                break;
        }
        return new MyToken(String.valueOf(c),typeC);
    }

    public List<MyToken> allTokens(){
        List<MyToken> tokens = new ArrayList<MyToken>();

        while(!charCode.isEmpty()){
            tokens.add(consumeToken());
        }
        MyToken endOf = new MyToken("-1",MyToken.TokenType.EOF);
        tokens.add(endOf);

        return tokens;

    }

    public MyToken.TokenType classifyToken(char input) {
        if (Character.isDigit(input))
            return MyToken.TokenType.INT_LITERAL;
        if (Character.isLetter(input))
            return MyToken.TokenType.IDENTIFIER;
        if (input == '\"')
            return MyToken.TokenType.STRING_LITERAL;
        else
            return MyToken.TokenType.SYMBOL;
    }

    public MyToken consumeIntegerToken() {
        StringBuilder stringBuild = new StringBuilder();

        while (Character.isDigit(peekChar())) {
            stringBuild.append(consumeChar());
        }
        return new MyToken(stringBuild.toString(), MyToken.TokenType.INT_LITERAL);

    }

    public MyToken consumeStringToken() {
        StringBuilder stringBuild = new StringBuilder();

        while (peekChar() != '\"') {
            stringBuild.append(consumeChar());
        }
        consumeChar(); // to consume the ending '"'

        
        return new MyToken(stringBuild.toString(), MyToken.TokenType.STRING_LITERAL);

    }

    public MyToken consumeWordToken() {
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

    public MyToken consumeSymbolToken() {
        String symbolLexeme = String.valueOf(consumeChar());

        return new MyToken(symbolLexeme, MyToken.TokenType.SYMBOL);
    }

    public static void main(String[] args) {
        MyLexer trialLexer = new MyLexer(args[0]);
        
    }
}