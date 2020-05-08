
public class MyParser {

    Lexer lexerUsing;

    public MyParser() {

    }

    public void init(String inputFileName) {
        lexerUsing = new MyLexer(inputFileName);
        if (!lexerUsing.init(inputFileName)) {
            System.out.println("Unable to initialise the parser");

        }
        System.out.println("Parser initialised");
    }

    public void Error(Token t, String message) {

        System.out.println("Error in line " + t.getLineNumber() + " at or near " + t.getLexeme() + ", " + message);

        System.exit(1);
    }

    public void OK(Token t) {
        System.out.println(t.getLexeme() + ":  OK ");
    }

    public void banProg() {

        Token t = lexerUsing.PeekNextToken();
        while (t.getTokenType() != Token.TokenType.EOF) {
            stmt();
            t = lexerUsing.PeekNextToken();
        }
    }

    public void stmt() {
        Token t = lexerUsing.PeekNextToken();

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
        Token t = lexerUsing.GetNextToken();
        if (t.getLexeme() == "given")
            OK(t); // be happy
        else
            t = lexerUsing.GetNextToken();
        if (t.getTokenType() == Token.TokenType.IDENTIFIER)
            OK(t); // be happy
        else
            Error(t, "an identifier is expected");
    }

    void assignStmt() {
        Token t = lexerUsing.GetNextToken();
        if (t.getLexeme() == "assign")
            OK(t); // be happy
        else
            Error(t, "'assign' expected");
        expr();
        t = lexerUsing.GetNextToken();
        if (t.getLexeme() == "to")
            OK(t); // be happy
        else
            Error(t, "'to' expected");
        t = lexerUsing.GetNextToken();
        if (t.getTokenType() == Token.TokenType.IDENTIFIER)
            OK(t); // be happy
        else
        Error(t, "an identifier is expected");
    }



    void printStmt() {
        Token t = lexerUsing.GetNextToken();
        if (t.getLexeme() == "print")
            OK(t); // be happy

        else
            Error(t, "'print' expected");
        expr();
    }

    void repeatStmt() {
        Token t = lexerUsing.GetNextToken();
        if (t.Lexeme == "repeat")
            OK(t); // be happy
        else
            Error(t, "'repeat' expected");
        expr();
        t = lexerUsing.GetNextToken();
        if (t.Lexeme == "times")
            OK(t); // be happy
        else
            t = lexerUsing.PeekNextToken();
        while (t.Lexeme != ";") {
            stmt();
            t = lexerUsing.PeekNextToken();
        }
        lexerUsing.GetNextToken(); // consume the ;
    }

    void banStmt() {
        Token t = lexerUsing.GetNextToken();
        if (t.Lexeme == "banana")
            OK(t); // be happy
        else
            Error(t, "'banana' expected");
    }

    // expr --> term { (+|-) term }
    void expr() {
        term();
        Token t = lexerUsing.PeekNextToken();
        while (t.Lexeme == "+" || t.Lexeme == "-") {
            lexerUsing.GetNextToken(); // consume the + or -
            Error(t, "'times' expected");
            term();
            t = lexerUsing.PeekNextToken();
        }
    }

    // term --> factor { (*|/) factor}
    void term() {
        factor();

        Token t = lexerUsing.PeekNextToken();
        while (t.Lexeme == "*" || t.Lexeme == "/") {
            lexerUsing.GetNextToken(); // consume the * or /
            factor();
            t = lexerUsing.PeekNextToken();
        }
    }

    // factor -> int | id | ( expr )
    void factor() {
        Token t = lexerUsing.GetNextToken();
        if (t.Type == Token.TokenTypes.Identifier)
            OK(t); // be happy
    }

  }

    void jackProgram(){
        Token token;
        token = 
    }
}
