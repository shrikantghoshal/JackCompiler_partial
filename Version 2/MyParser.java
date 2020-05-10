
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
            statement();
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

    public void statement() {
        MyToken t = lexer.PeekNextToken();

        if (t.getLexeme() == "var")
            varDeclarStatement();
        else if (t.getLexeme() == "let")
            letStatement();
        else if (t.getLexeme() == "if")
            ifStatement();
        else if (t.getLexeme() == "while")
            whileStatement();
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
        if (t.getLexeme() != "var")
            Error(t, "an keyword is expected: 'var'");
        OK(t);

        t = lexer.GetNextToken();
        if (t.getTokenType() != MyToken.TokenType.IDENTIFIER)
            Error(t, "an identifier is expected");
        OK(t);
        t = lexer.GetNextToken();

    }

    public void letStatement() {
        MyToken t = lexer.GetNextToken();
        if (t.getLexeme() != "let")
            Error(t, "a keyword is expected: 'let");
        OK(t);
        t = lexer.GetNextToken();
        if (t.getTokenType() != MyToken.TokenType.IDENTIFIER)
            Error(t, "an identifier is expected");
        OK(t);
        t = lexer.GetNextToken();
        if (t.getLexeme() != "[" || t.getLexeme() != "=")
            Error(t, "a '=' of '[' is expected"); // let identifier [expression] = expression; |or| let identifier =
                                                  // expression;
        if (t.getLexeme() == "[") {
            OK(t);
            t = lexer.PeekNextToken();
            while (t.getLexeme() != "]") {
                expression();// TODO
                t = lexer.PeekNextToken();
            }
            t = lexer.GetNextToken();
            if (t.getLexeme() != "]")
                Error(t, "a ']' is expected");
            OK(t);
            t = lexer.GetNextToken();
            if (t.getLexeme() != "=")
                Error(t, "a '=' is expected");
            OK(t);
            t = lexer.GetNextToken();
            expression();
            t = lexer.GetNextToken();
            if (t.getLexeme() != ";")
                Error(t, "a ';' is expected");
            OK(t);

        } else if (t.getLexeme() == "=") {
            OK(t);
            t = lexer.GetNextToken();
            expression();
            t = lexer.GetNextToken();
            if (t.getLexeme() != ";")
                Error(t, "a ';' is expected");
            OK(t);

        }
    }

    public void ifStatement() {
        MyToken t = lexer.GetNextToken();
        if (t.getLexeme() != "if")
            Error(t, "an identifier is expected: 'if");
        OK(t);
        t = lexer.GetNextToken();
        if (t.getLexeme() != "(")
            Error(t, "a '(' is expected");
        OK(t);
        t = lexer.GetNextToken();
        expression();
        t = lexer.GetNextToken();
        if (t.getLexeme() != ")")
            Error(t, "a ')' is expected");
        OK(t);
        statementBlock();
        t = lexer.PeekNextToken();
        if (t.getLexeme() == "else") {
            t = lexer.GetNextToken();
            OK(t);
            statementBlock();
        }

    }

    public void statementBlock() {
        MyToken t = lexer.GetNextToken();
        if (t.getLexeme() != "{")
            Error(t, "a '{' is expected");
        OK(t);
        t = lexer.PeekNextToken();
        while (t.getLexeme() != "}") {
            statement();
            t = lexer.PeekNextToken();
        }
        t = lexer.GetNextToken();
        if (t.getLexeme() != "}")
            Error(t, "a '}' is expected");
        OK(t);
    }

    public void whileStatement() {
        MyToken t = lexer.GetNextToken();
        if (t.getLexeme() != "while")
            Error(t, "an identifier is expected: 'while'");

        OK(t);
        t = lexer.GetNextToken();
        if (t.getLexeme() != "(")
            Error(t, "a '(' is expected");

        OK(t);
        t = lexer.GetNextToken();
        expression();
        t = lexer.GetNextToken();
        if (t.getLexeme() != ")")
            Error(t, "a ')' is expected");

        OK(t);
        statementBlock();
    }

    public void doStatement() {
        MyToken t = lexer.GetNextToken();
        if (t.getLexeme() != "do")
            Error(t, "an identifier is expected: 'do'");
        OK(t);

        t = lexer.GetNextToken();
        subRoutineCall();
        t = lexer.GetNextToken();
        if (t.getLexeme() != ";")
            Error(t, "a ';' is expected");
        OK(t);
    }

    public void returnStatement() {
        MyToken t = lexer.PeekNextToken();
        while (t.getLexeme() != ";") {
            expression();
            t = lexer.PeekNextToken();
        }
        t = lexer.GetNextToken();
        if (t.getLexeme() != ";")
            Error(t, "a ';' is expected");
        OK(t);
    }

    // create subroutine call
    public void subRoutineCall() {
        MyToken t = lexer.GetNextToken();
        if (t.getTokenType() != MyToken.TokenType.IDENTIFIER)
            Error(t, "an identifier is expected");
        OK(t);
        t = lexer.GetNextToken();
        if (t.getLexeme() == "(" || t.getLexeme() == ".") {
            OK(t);
            if (t.getLexeme() == ".") {
                t = lexer.GetNextToken();
                if (t.getTokenType() != MyToken.TokenType.IDENTIFIER)
                    Error(t, "an identifier is expected");
                OK(t);
            }
            t = lexer.PeekNextToken();
            while (t.getLexeme() != ")") {
                expressionList();
                t = lexer.PeekNextToken();
            }
            t = lexer.GetNextToken();
        } else
            Error(t, "a '.' or '(' is expected");
        if (t.getLexeme() != ")")
            Error(t, "a ')' is expected");
        OK(t);
    }

    public void expressionList() {
        MyToken t = lexer.GetNextToken();
        expression();
        t = lexer.PeekNextToken();
        while (t.getLexeme() != "null") {
            if (t.getLexeme() != ",")
                Error(t, "a ',' is expected");
            OK(t);
            t = lexer.GetNextToken();
            expression();
            t = lexer.PeekNextToken();
        }
    }

    public void expression() {
        MyToken t = lexer.GetNextToken();
        relationalExpression();
        t = lexer.PeekNextToken();
        if (t.getLexeme() != "&" && t.getLexeme() != "|")
            Error(t, "a '&' or '|' is expected");
        while (t.getLexeme() == "&" || t.getLexeme() == "|") {
            t = lexer.GetNextToken();
            relationalExpression();
            t = lexer.PeekNextToken();
        }
        t = lexer.GetNextToken();
        OK(t);
    }

    public void relationalExpression() {
        MyToken t = lexer.GetNextToken();
        arithmeticExpression();
        t = lexer.PeekNextToken();
        while (t.getLexeme() == "=" || t.getLexeme() == ">" || t.getLexeme() == "<") {
            t = lexer.GetNextToken();
            arithmeticExpression();
            t = lexer.PeekNextToken();
        }
    }

    public void arithmeticExpression() {
        MyToken t = lexer.GetNextToken();
        term();
        t = lexer.PeekNextToken();
        while (t.getLexeme() == "+" || t.getLexeme() == "-") {
            t = lexer.GetNextToken();
            term();
            t = lexer.PeekNextToken();
        }
    }

    // term --> factor { (*|/) factor}
    public void term() {
        MyToken t = lexer.GetNextToken();
        factor();
        t = lexer.PeekNextToken();
        while (t.getLexeme() == "*" || t.getLexeme() == "/") {
            lexer.GetNextToken(); // consume the * or /
            factor();
            t = lexer.PeekNextToken();
        }
    }

    // factor -> int | id | ( expr )
    public void factor() {
        MyToken t = lexer.GetNextToken();
        if (t.getLexeme() == "-" || t.getLexeme() == "~" || t.getLexeme() == "")
            operand();

        OK(t);
    }

    public void operand(){
        MyToken t = lexer.GetNextToken();
        if(t.getTokenType() == )
    }

    void jackProgram(){
        Token token;
        token = 
    }
}
