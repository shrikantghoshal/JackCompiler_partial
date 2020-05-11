public class MyParser {

    private MyLexer lexer;

    public MyParser(String userInput) {
        init(userInput);
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
    
    public void jackClass() {
        MyToken t = lexer.GetNextToken();
        if (!t.getLexeme().equals("class"))
            Error(t, "a 'class' is expected");
        OK(t);
        t = lexer.GetNextToken();
        if (t.getTokenType() != MyToken.TokenType.IDENTIFIER)
            Error(t, "an identifier is expected");
        OK(t);
        t = lexer.GetNextToken();
        if (!t.getLexeme().equals("{"))
            Error(t, "a '{' is expected");
        OK(t);
        t = lexer.PeekNextToken();
        while (!t.getLexeme().equals("}")) {
            memberDeclar();
            t = lexer.PeekNextToken();
        }
        t = lexer.GetNextToken();
        if (!t.getLexeme().equals("}"))
            Error(t, "a '}' is expected");
        OK(t);
    }

    public void memberDeclar() {
        MyToken t = lexer.PeekNextToken();
        if (t.getLexeme().equals("static") || t.getLexeme().equals("field"))
            classVarDeclar();
        else if (t.getLexeme().equals("constructor") || t.getLexeme().equals("function") || t.getLexeme().equals("method"))
            subRoutineDeclar();
        else
            Error(t, "unknown member");
    }

    public void classVarDeclar() {
        MyToken t = lexer.GetNextToken();
        if (!t.getLexeme().equals("static") && !t.getLexeme().equals("field"))
            Error(t, "'static' or 'field' expected");
        OK(t);
        t = lexer.GetNextToken();
        type();
        t = lexer.GetNextToken();
        if (t.getTokenType() != MyToken.TokenType.IDENTIFIER)
            Error(t, "an identifier is expected");
        OK(t);
        t = lexer.PeekNextToken();
        while (!t.getLexeme().equals(";")) {
            t = lexer.GetNextToken();
            OK(t);
            t = lexer.GetNextToken();
            if (!t.getLexeme().equals(","))
                Error(t, "a ',' is expected");
            OK(t);
            t = lexer.GetNextToken();
            if (t.getTokenType() != MyToken.TokenType.IDENTIFIER)
                Error(t, "an identifier is expected");
            OK(t);
            t = lexer.PeekNextToken();
        }
        if (!t.getLexeme().equals(";"))
            Error(t, "a ';' is expected");
        OK(t);

    }

    public void type() {
        MyToken t = lexer.GetNextToken();
        if (!t.getLexeme().equals("int") && !t.getLexeme().equals("char") && !t.getLexeme().equals("boolean")
                && t.getTokenType() != MyToken.TokenType.IDENTIFIER)
            Error(t, "unknown type");
        OK(t);
    }

    public void subRoutineDeclar() {
        MyToken t = lexer.GetNextToken();
        if (!t.getLexeme().equals("constructor") && !t.getLexeme().equals("function") && !t.getLexeme().equals("method"))
            Error(t, "'constructor' , 'function' or 'method' is expected");
        OK(t);
        t = lexer.PeekNextToken();
        if ((!t.getLexeme().equals("int") && !t.getLexeme().equals("char") && !t.getLexeme().equals("boolean")
                && t.getTokenType() != MyToken.TokenType.IDENTIFIER) && !t.getLexeme().equals("void")) {
            Error(t, "a type or void is expected");
            type();
            if (t.getLexeme().equals("void"))
                t = lexer.GetNextToken();
            OK(t);
            t = lexer.GetNextToken();
            if (t.getTokenType() != MyToken.TokenType.IDENTIFIER)
                Error(t, "an identifier is expected");
            OK(t);
            t = lexer.GetNextToken();
            if (t.getLexeme().equals("(")) {
                t = lexer.GetNextToken();
                OK(t);
                paramList();
                t = lexer.GetNextToken();
                if (!t.getLexeme().equals(")"))
                    Error(t, "a ')' is expected");
            } else
                Error(t, "a '(' is expected");
            statementBlock();
        }
    }

    public void paramList() {
        MyToken t = lexer.PeekNextToken();
        if (!t.getLexeme().equals(")")) {
            type();
            t = lexer.GetNextToken();
            if (t.getTokenType() != MyToken.TokenType.IDENTIFIER)
                Error(t, "an identifier is expected");
            OK(t);
            t = lexer.PeekNextToken();
            while (!t.getLexeme().equals(")")) {
                t = lexer.GetNextToken();
                if (!t.getLexeme().equals(","))
                    Error(t, "a ',' or ')' is expected)");
                OK(t);
                type();
                t = lexer.GetNextToken();
                if (t.getTokenType() != MyToken.TokenType.IDENTIFIER)
                    Error(t, "an identifier is expected");
                OK(t);
                t = lexer.PeekNextToken();
            }
        }
    }

    public void statement() {
        MyToken t = lexer.PeekNextToken();

        if (t.getLexeme().equals("var"))
            varDeclarStatement();
        else if (t.getLexeme().equals("let"))
            letStatement();
        else if (t.getLexeme().equals("if"))
            ifStatement();
        else if (t.getLexeme().equals("while"))
            whileStatement();
        else if (t.getLexeme().equals("do"))
            doStatement();
        else if (t.getLexeme().equals("return"))
            returnStatement();
        else
            Error(t, "unknown keyword");
    }

    public void varDeclarStatement() {
        MyToken t = lexer.GetNextToken();
        if (!t.getLexeme().equals("var"))
            Error(t, "an keyword is expected: 'var'");
        OK(t);
        type();
        t = lexer.GetNextToken();
        if (t.getTokenType() != MyToken.TokenType.IDENTIFIER)
            Error(t, "an identifier is expected");
        OK(t);
        t = lexer.PeekNextToken();
        while (!t.getLexeme().equals(";")) {
            if (!t.getLexeme().equals(","))
                Error(t, "a ',' or ';' is expected");
            t = lexer.GetNextToken();
            OK(t);
            t = lexer.GetNextToken();
            if (t.getTokenType() != MyToken.TokenType.IDENTIFIER)
                Error(t, "an identifier is expected");
            OK(t);
            t = lexer.PeekNextToken();
        }
        t = lexer.GetNextToken();
        OK(t);
    }

    public void letStatement() {
        MyToken t = lexer.GetNextToken();
        if (!t.getLexeme().equals("let"))
            Error(t, "a keyword is expected: 'let");
        OK(t);
        t = lexer.GetNextToken();
        if (t.getTokenType() != MyToken.TokenType.IDENTIFIER)
            Error(t, "an identifier is expected");
        OK(t);
        t = lexer.GetNextToken();
        if (!t.getLexeme().equals("[") || !t.getLexeme().equals("="))
            Error(t, "a '=' of '[' is expected"); // let identifier [expression] = expression; |or| let identifier =
                                                  // expression;
        if (t.getLexeme().equals("[")) {
            OK(t);
            t = lexer.PeekNextToken();
            while (!t.getLexeme().equals("]")) {
                expression();
                t = lexer.PeekNextToken();
            }
            t = lexer.GetNextToken();
            if (!t.getLexeme().equals("]"))
                Error(t, "a ']' is expected");
            OK(t);
            t = lexer.GetNextToken();
            if (!t.getLexeme().equals("="))
                Error(t, "a '=' is expected");
            OK(t);
            expression();
            t = lexer.GetNextToken();
            if (!t.getLexeme().equals(";"))
                Error(t, "a ';' is expected");
            OK(t);

        } else if (t.getLexeme().equals("=")) {
            OK(t);
            expression();
            t = lexer.GetNextToken();
            if (!t.getLexeme().equals(";"))
                Error(t, "a ';' is expected");
            OK(t);

        }
    }

    public void ifStatement() {
        MyToken t = lexer.GetNextToken();
        if (!t.getLexeme().equals("if"))
            Error(t, "an identifier is expected: 'if");
        OK(t);
        t = lexer.GetNextToken();
        if (!t.getLexeme().equals("("))
            Error(t, "a '(' is expected");
        OK(t);
        expression();
        t = lexer.GetNextToken();
        if (!t.getLexeme().equals(")"))
            Error(t, "a ')' is expected");
        OK(t);
        statementBlock();
        t = lexer.PeekNextToken();
        if (t.getLexeme().equals("else")) {
            t = lexer.GetNextToken();
            OK(t);
            statementBlock();
        }

    }

    public void statementBlock() {
        MyToken t = lexer.GetNextToken();
        if (!t.getLexeme().equals("{"))
            Error(t, "a '{' is expected");
        OK(t);
        t = lexer.PeekNextToken();
        while (!t.getLexeme().equals("}")) {
            statement();
            t = lexer.PeekNextToken();
        }
        t = lexer.GetNextToken();
        if (!t.getLexeme().equals("}"))
            Error(t, "a '}' is expected");
        OK(t);
    }

    public void whileStatement() {
        MyToken t = lexer.GetNextToken();
        if (!t.getLexeme().equals("while"))
            Error(t, "an identifier is expected: 'while'");

        OK(t);
        t = lexer.GetNextToken();
        if (!t.getLexeme().equals("("))
            Error(t, "a '(' is expected");

        OK(t);
        expression();
        t = lexer.GetNextToken();
        if (!t.getLexeme().equals(")"))
            Error(t, "a ')' is expected");

        OK(t);
        statementBlock();
    }

    public void doStatement() {
        MyToken t = lexer.GetNextToken();
        if (!t.getLexeme().equals("do"))
            Error(t, "an identifier is expected: 'do'");
        OK(t);
        subRoutineCall();
        t = lexer.GetNextToken();
        if (!t.getLexeme().equals(";"))
            Error(t, "a ';' is expected");
        OK(t);
    }

    public void returnStatement() {
        MyToken t = lexer.PeekNextToken();
        while (!t.getLexeme().equals(";")) {
            expression();
            t = lexer.PeekNextToken();
        }
        t = lexer.GetNextToken();
        if (!t.getLexeme().equals(";"))
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
        if (t.getLexeme().equals("(") || t.getLexeme().equals(".")) {
            OK(t);
            if (t.getLexeme().equals(".")) {
                t = lexer.GetNextToken();
                if (t.getTokenType() != MyToken.TokenType.IDENTIFIER)
                    Error(t, "an identifier is expected");
                OK(t);
            }
            t = lexer.GetNextToken();
            if (t.getLexeme().equals("(")) {
                expressionList();
                t = lexer.GetNextToken();
                if (!t.getLexeme().equals(")"))
                    Error(t, "a ')' is expected");
            } else
                Error(t, "a '(' is expected");
        } else
            Error(t, "a '.' or '(' is expected");
        OK(t);
    }

    public void expressionList() {
        MyToken t = lexer.PeekNextToken();
        if (!t.getLexeme().equals(")")) {
            expression();
            t = lexer.PeekNextToken();
            while (!t.getLexeme().equals(")")) {
                t = lexer.GetNextToken();
                if (!t.getLexeme().equals(","))
                    Error(t, "a ',' or ')' is expected)");
                OK(t);
                expression();
                t = lexer.PeekNextToken();
            }
        }
        OK(t);
    }

    public void expression() {
        MyToken t = lexer.GetNextToken();
        relationalExpression();
        t = lexer.PeekNextToken();
        while (t.getLexeme().equals("&") || t.getLexeme().equals("|")) {
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
        while (t.getLexeme().equals("=") || t.getLexeme().equals(">") || t.getLexeme().equals("<")) {
            t = lexer.GetNextToken();
            arithmeticExpression();
            t = lexer.PeekNextToken();
        }
    }

    public void arithmeticExpression() {
        MyToken t = lexer.GetNextToken();
        term();
        t = lexer.PeekNextToken();
        while (t.getLexeme().equals("+") || t.getLexeme().equals("-")) {
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
        while (t.getLexeme().equals("*") || t.getLexeme().equals("/")) {
            lexer.GetNextToken(); // consume the * or /
            factor();
            t = lexer.PeekNextToken();
        }
    }

    // factor -> int | id | ( expr )
    public void factor() {
        MyToken t = lexer.GetNextToken();
        if (t.getLexeme().equals("-") || t.getLexeme().equals("~") || t.getLexeme().equals(""))
            operand();

        OK(t);
    }

    public void operand() {
        MyToken t = lexer.GetNextToken();
        if (t.getTokenType() == MyToken.TokenType.INT_LITERAL)
            OK(t);
        else if (t.getTokenType() == MyToken.TokenType.IDENTIFIER) {
            t = lexer.PeekNextToken();
            if (t.getLexeme().equals(".")) {
                t = lexer.GetNextToken();
                OK(t);
                t = lexer.GetNextToken();
                if (t.getTokenType() != MyToken.TokenType.IDENTIFIER)
                    Error(t, "an identifier expected");
                OK(t);
                t = lexer.PeekNextToken();
                if (t.getLexeme().equals("[") || t.getLexeme().equals("(")) {
                    t = lexer.GetNextToken();
                    OK(t);
                    if (t.getLexeme().equals("[")) {
                        t = lexer.GetNextToken();
                        expression();
                        t = lexer.GetNextToken();
                        if (!t.getLexeme().equals("]"))
                            Error(t, "a ']' is expected");
                        OK(t);
                    } else if (t.getLexeme().equals("(")) {
                        t = lexer.GetNextToken();
                        expressionList();
                        t = lexer.GetNextToken();
                        if (!t.getLexeme().equals(")"))
                            Error(t, "a ')' is expected");
                        OK(t);
                    }
                }
            }
        } else if (t.getLexeme().equals("(")) {
            OK(t);
            t = lexer.GetNextToken();
            expression();
            t = lexer.GetNextToken();
            if (!t.getLexeme().equals(")"))
                Error(t, "a ')' is expected");
        } else if (t.getTokenType() == MyToken.TokenType.STRING_LITERAL)
            OK(t);
        else if (t.getLexeme().equals("true"))
            OK(t);
        else if (t.getLexeme().equals("false"))
            OK(t);
        else if (t.getLexeme().equals("null"))
            OK(t);
        else if (t.getLexeme().equals("this"))
            OK(t);
        else
            Error(t, "unknown operand");

    }

    public void jackProgram(){
        MyToken t = lexer.PeekNextToken();
        while(t.getTokenType()!=MyToken.TokenType.EOF){
            jackClass();
            t=lexer.PeekNextToken();
        }
    }

    public static void main(String[] args) {
        MyParser trialParser = new MyParser("Main.jack");

        trialParser.jackProgram();

    }

}
