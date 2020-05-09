
public class MyParser {

    private MyLexer lexer;

    public MyParser() {

    }

    public void init(String inputFileName) {
        lexer = new MyLexer(inputFileName);
        if (!lexer.init(inputFileName)) {
            System.out.println("Unable to initialise the parser");
        }
        System.out.println("Parser initialised");
    }

    public void Error(MyToken t, String message) {

        System.out.println("Error in line " + t.getLineNumber() + " at or near " + t.getLexeme() + ", " + message);

        System.exit(1);
    }

    public void OK(MyToken t) {
        System.out.println(t.getLexeme() + ":  OK ");
    }

    public void banProg() {

        MyToken t = lexer.PeekNextToken();
        while (t.getTokenType() != MyToken.TokenType.EOF) {
            stmt();
            t = lexer.PeekNextToken();
        }
    }

    public void stmt() {
        Token t = lexer.PeekNextToken();

        if (t.getLexeme() == "given")
            varDeclar();
        else if (t.getLexeme() == "print")
            printStmt();
        else if (t.getLexeme() == "assign")
            assignStmt();
        else if (t.getLexeme() == "repeat")
            repeatStmt();
        else if (t.getLexeme() == "banana")
            banStmt();
        else
            Error(t, "unknown keyword");
    }

    void varDeclar() {
        Token t = lexer.GetNextToken();
        if (t.getLexeme() == "given")
            OK(t); // be happy
        else
            t = lexer.GetNextToken();
        if (t.getTokenType() == Token.TokenType.IDENTIFIER)
            OK(t); // be happy
        else
            Error(t, "an identifier is expected");
    }

    void assignStmt() {
        Token t = lexer.GetNextToken();
        if (t.getLexeme() == "assign")
            OK(t); // be happy
        else
            Error(t, "'assign' expected");
        expr();
        t = lexer.GetNextToken();
        if (t.getLexeme() == "to")
            OK(t); // be happy
        else
            Error(t, "'to' expected");
        t = lexer.GetNextToken();
        if (t.getTokenType() == Token.TokenType.IDENTIFIER)
            OK(t); // be happy
        else
        Error(t, "an identifier is expected");
    }



    void printStmt() {
        Token t = lexer.GetNextToken();
        if (t.getLexeme() == "print")
            OK(t); // be happy

        else
            Error(t, "'print' expected");
        expr();
    }

    void repeatStmt() {
        Token t = lexer.GetNextToken();
        if (t.Lexeme == "repeat")
            OK(t); // be happy
        else
            Error(t, "'repeat' expected");
        expr();
        t = lexer.GetNextToken();
        if (t.Lexeme == "times")
            OK(t); // be happy
        else
            t = lexer.PeekNextToken();
        while (t.Lexeme != ";") {
            stmt();
            t = lexer.PeekNextToken();
        }
        lexer.GetNextToken(); // consume the ;
    }

    void banStmt() {
        Token t = lexer.GetNextToken();
        if (t.Lexeme == "banana")
            OK(t); // be happy
        else
            Error(t, "'banana' expected");
    }

    // expr --> term { (+|-) term }
    void expr() {
        term();
        Token t = lexer.PeekNextToken();
        while (t.Lexeme == "+" || t.Lexeme == "-") {
            lexer.GetNextToken(); // consume the + or -
            Error(t, "'times' expected");
            term();
            t = lexer.PeekNextToken();
        }
    }

    // term --> factor { (*|/) factor}
    void term() {
        factor();

        Token t = lexer.PeekNextToken();
        while (t.Lexeme == "*" || t.Lexeme == "/") {
            lexer.GetNextToken(); // consume the * or /
            factor();
            t = lexer.PeekNextToken();
        }
    }

    // factor -> int | id | ( expr )
    void factor() {
        Token t = lexer.GetNextToken();
        if (t.Type == Token.TokenTypes.Identifier)
            OK(t); // be happy
    }

  }

    void jackProgram(){
        Token token;
        token = 
    }
}
