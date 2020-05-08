import java.io.*;
import java.util.*;

public class MyLexer {
    public PushbackReader pushbackReader = null;

    private int linNum;
    int c;

    public ArrayList<MyToken> allTokens;
    public List<String> uncommentedCode;

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
            removeComments(inputFile)
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

    public MyToken GetNextToken() throws IOException {

        // EOF detection
        if (c == -1) {
            token.setTokenType(MyToken.TokenType.EOF);
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

            token.setTokenType(MyToken.TokenType.IDENTIFIER);
            // System.out.println(token);
            return token;
        }

        if (Character.isDigit((char) c)) {
            // token.setNewLexeme();
            while ((c != -1) && Character.isDigit((char) c)) {
                token.setLexeme(String.valueOf((char) c));
                c = pushbackReader.read();
                // pushbackReader.unread(c);
            }
            token.setTokenType(MyToken.TokenType.CONSTANT);
            // System.out.println(token);
            return token;
        }

        // token.setNewLexeme();
        token.setLexeme(String.valueOf((char) c));
        token.setTokenType(MyToken.TokenType.SYMBOL);
        // System.out.println(token);
        return token;

    }

    public MyToken PeekNextToken() {
    }

    public static List<String> removeComments(File inputFile) throws IOException {
        FileReader fread = new FileReader(inputFile);

        String content = null;
        StringBuilder stringBuild = new StringBuilder();
        String separateLine = System.getProperty("line.separator"); // Java API
        LineNumberReader lineRead = new LineNumberReader(fread);
        content = lineRead.readLine()
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

        lineRead.close();
        fread.close();
        
        
        return conversionStrList;
    }

    public void Tokenize(List<String> inputString) {


        //split into array of individual chars
        
        for (int count = 0; count < inputString.size(); count++) {          //iterate through lines
                String[] particles = inputString.get(count).split("(?!^)");
                for (int i = 0; i < particles.length; i++) {
                    
                    
                    if(Character.isDigit(particles[i].charAt(0)){
                        MyToken newToken = new MyToken(parseInt(particles[i]), MyToken.TokenType.CONSTANT, count);
                        allTokens.add(newToken);
                    }
                    for (Keywords kwd : Keywords.values()) {
                        if (particles[i].equals(kwd.getKeywordName())) {
                            MyToken newToken = new MyToken(particles[i], MyToken.TokenType.KEYWORD, count);
                            allTokens.add(newToken);
                        }
                        
                    }
                }
        }
    }

    public static void main(String[] args) {
        MyLexer trialLexer = new MyLexer(args[0]);
        Integer count = 0;
        // try {
        // while (!trialLexer.PeekNextToken().equals(MyToken.TokenType.EOF)) {
        // MyToken dispToken = trialLexer.GetNextToken();
        // count++;
        // System.out.println(dispToken);
        // }

        // } catch (IOException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }

    }
}