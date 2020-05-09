import MyToken.TokenType;

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

        System.out.println("Error at or near " + t.getLexeme() + ", " + message);

        System.exit(1);
    }

    public void OK(MyToken t) {
        System.out.println(t.getLexeme() + ":  OK ");
    }

    public void jackProg() {

        MyToken t = lexer.PeekNextToken();
        while (t.getTokenType() != MyToken.TokenType.EOF) {
            stmt();
            t = lexer.PeekNextToken();
        }
    }

    // public void class(){
    // MyToken t = lexer.PeekNextToken();
    // if (t.getLexeme() = "class"){
    // OK();
    // t=lexer.
    // }
    // }

    public

    public void statement() {
        MyToken t = lexer.PeekNextToken();

        if (t.getLexeme() == "var")
            varDeclarStatement();
        else if (t.getLexeme() == "let")
            letStatement();
        else if (t.getLexeme() == "if")
            ifStatement();
        else if (t.getLexeme() == "while")
            repeatStatement();
        else if (t.getLexeme() == "do")
            doStatement();
        else if (t.getLexeme() == "return")
            returnStatement();
        else
            Error(t, "unknown keyword");
    }

    // public void stmt() {
    // MyToken t = lexer.PeekNextToken();

    // if (t.getLexeme() == "var")
    // varDeclar();
    // else if (t.getLexeme() == "let")
    // letStmt();
    // else if (t.getLexeme() == "do")
    // printStmt();
    // // doStmt();
    // else if (t.getLexeme() == "if")
    // assignStmt();
    // // ifStmt();
    // else if (t.getLexeme() == "while")
    // repeatStmt();
    // // elseStmt();
    // else if (t.getLexeme() == "")
    // banStmt();
    // // whileStmt();
    // else if (t.getLexeme() == "return")
    // returnStmt();
    // else
    // Error(t, "unknown keyword");
    // }

    public void varDeclarStatement() {
        MyToken t = lexer.GetNextToken();
        if (t.getLexeme() == "var")
            OK(t);
        else
            t = lexer.GetNextToken();
        if (t.getTokenType() == MyToken.TokenType.IDENTIFIER) {
            OK(t);
            t = lexer.GetNextToken();
            if (t.getTokenType() == MyToken.TokenType.IDENTIFIER)
                OK(t);
            else
                Error(t, "an identifier is expected");
        } else
            Error(t, "an identifier is expected");
    }

    public void letStatement() {
        MyToken t = lexer.GetNextToken();
        if (t.getLexeme() == "let") {
            OK(t);
            t = lexer.GetNextToken();
            if (t.getTokenType() == MyToken.TokenType.IDENTIFIER) {
                OK(t);
                t = lexer.GetNextToken();
                if (t.getLexeme() == "[") {
                    OK(t);
                    t = lexer.GetNextToken();
                    expression();// TODO
                    t = lexer.GetNextToken();
                    if (t.getLexeme() == "]") {
                        OK(t);
                        t = lexer.GetNextToken();
                        if (t.getLexeme() == "=") {
                            OK(t);
                            t = lexer.GetNextToken();
                            expression();
                            t = lexer.GetNextToken();
                            if (t.getLexeme() == ";")
                                OK(t);
                            else
                                Error(t, "a ';' is expected");
                        } else
                            Error(t, "a '=' is expected");
                    } else
                        Error(t, "a ']' is expected");
                } else if (t.getLexeme() == "=") {
                    OK(t);
                    t = lexer.GetNextToken();
                    expression();
                    t = lexer.GetNextToken();
                    if (t.getLexeme() == ";")
                        OK(t);
                    else
                        Error(t, "a ';' is expected");
                } else
                    Error(t, "a '=' of '[' is expected");
            } else
                Error(t, "an identifier is expected");
        } else

            Error(t, "an identifier is expected: 'let");
    }

    public void ifStatement() {
        MyToken t = lexer.GetNextToken();
        if (t.getLexeme() == "if") {
            OK(t);
            t = lexer.GetNextToken();
            if (t.getLexeme() == "(") {
                OK(t);
                t = lexer.GetNextToken();
                expression();
                t = lexer.GetNextToken();
                if (t.getLexeme() == ")") {
                    OK(t);
                    t = lexer.GetNextToken();
                    if (t.getLexeme() == "{") {
                        OK(t);
                        t = lexer.GetNextToken();
                        statement();
                        t = lexer.GetNextToken();
                        if (t.getLexeme() == "}") {
                            OK(t);
                            MyToken lookAhead = lexer.PeekNextToken();
                            if (lookAhead.getLexeme() == "else") {
                                t = lexer.GetNextToken();
                                OK(t);
                                t = lexer.GetNextToken();
                                if (t.getLexeme() == "{") {
                                    OK(t);
                                    t = lexer.GetNextToken();
                                    statement();
                                    t = lexer.GetNextToken();
                                    if (t.getLexeme() == "}") {
                                        OK(t);
                                    } else
                                        Error(t, "a '}' is expected");
                                } else
                                    Error(t, "a '{' is expected");
                            } else
                                Error(t, "an 'else' is expected");
                        } else
                            Error(t, "a '}' is expected");
                    } else
                        Error(t, "a '{' is expected");
                } else
                    Error(t, "a ')' is expected");
            } else
                Error(t, "a '(' is expected");
        } else
            Error(t, "an identifier is expected: 'if");
    }

    public void whileStatement() {
        MyToken t = lexer.GetNextToken();
        if (t.getLexeme() == "while") {
            if (t.getLexeme() == "(") {
                OK(t);
                t = lexer.GetNextToken();
                expression();
                t = lexer.GetNextToken();
                if (t.getLexeme() == ")") {
                    OK(t);
                    t = lexer.GetNextToken();
                    if (t.getLexeme() == "{") {
                        OK(t);
                        t = lexer.GetNextToken();
                        statement();
                        t=lexer.GetNextToken();
                        if (t.getLexeme() == "}") {
                            OK(t);
                        } else
                            Error(t, "a '}' is expected");
                    } else
                        Error(t, "a '{' is expected");
                } else
                    Error(t, "a ')' is expected");
            } else
                Error(t, "a '(' is expected");
        } else
            Error(t, "an identifier is expected: 'while'");
    }

    public void deStatement(){
        MyToken t = lexer.GetNextToken();
        if (t.getLexeme() == "do") {
            OK(t);
            t = lexer.GetNextToken();
            subRoutineCall();
        } else
            Error(t, "an identifier is expected: 'do'");
    }









    

    public void assignStmt() {
        MyToken t = lexer.GetNextToken();
        if (t.getLexeme() == "assign")
            OK(t);
        else
            Error(t, "\'assign\' expected");
        expr();
        t = lexer.GetNextToken();
        if (t.getLexeme() == "to")
            OK(t);
        else
            Error(t, "'to' expected");
        t = lexer.GetNextToken();
        if (t.getTokenType() == Token.TokenType.IDENTIFIER)
            OK(t);
        else
            Error(t, "an identifier is expected");
    }

    void printStmt() {
        Token t = lexer.GetNextToken();
        if (t.getLexeme() == "print")
            OK(t);

        else
            Error(t, "'print' expected");
        expr();
    }

    void repeatStmt() {
        Token t = lexer.GetNextToken();
        if (t.Lexeme == "repeat")
            OK(t);
        else
            Error(t, "'repeat' expected");
        expr();
        t = lexer.GetNextToken();
        if (t.Lexeme == "times")
            OK(t);
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
            OK(t);
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

    // edit this
    void expression() {
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
            OK(t);
    }

  }

    void jackProgram(){
        Token token;
        token = 
    }
}
