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

    //output to terminal "Error at or near '(following lexeme)', (an errorMessage is expected)" and exit the program
    public void Error(MyToken t, String message) {

        System.out.println("Error at or near '" + t.getLexeme() + "', " + message);

        System.exit(1);
    }

    // output to terminal "lexeme:  OK"
    public void OK(MyToken t) {
        System.out.println(t.getLexeme() + ":  OK ");
    }

     //jackProgram-->{jackClass}       loop till EOF
     public void jackProgram() {
        MyToken t = lexer.PeekNextToken();
        while (t.getTokenType() != MyToken.TokenType.EOF) {
            jackClass();
            t = lexer.PeekNextToken();
        }
    }

    //jackClass-->class identifier->{memberDeclar}
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

    //memberDeclar-->classVarDeclar | subRoutineDeclar
    public void memberDeclar() {
        MyToken t = lexer.PeekNextToken();
        if (t.getLexeme().equals("static") || t.getLexeme().equals("field"))
            classVarDeclar();
        else if (t.getLexeme().equals("constructor") || t.getLexeme().equals("function")
                || t.getLexeme().equals("method"))
            subRoutineDeclar();
        else
            Error(t, "unknown member");
    }

    //classVarDeclar-->(static|field)->type identifier->{,identifier} ;
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

    // type-->int | char | boolean | identifier
    public void type() {
        MyToken t = lexer.GetNextToken();
        if (!t.getLexeme().equals("int") && !t.getLexeme().equals("char") && !t.getLexeme().equals("boolean")
                && t.getTokenType() != MyToken.TokenType.IDENTIFIER)
            Error(t, "unknown type");
        OK(t);
    }

    //subRoutineDeclar-->(constructor|function|method)->(type|void)->identifier (paramList) statementBlock
    public void subRoutineDeclar() {
        MyToken t = lexer.GetNextToken();
        if (!t.getLexeme().equals("constructor") && !t.getLexeme().equals("function")
                && !t.getLexeme().equals("method"))
            Error(t, "'constructor' , 'function' or 'method' is expected");
        OK(t);
        t = lexer.PeekNextToken();
        if (t.getLexeme().equals("void")) {
            t = lexer.GetNextToken();
            OK(t);
        } else
            type();
        t = lexer.GetNextToken();
        if (t.getTokenType() != MyToken.TokenType.IDENTIFIER)
            Error(t, "an identifier is expected");
        OK(t);
        t = lexer.PeekNextToken();
        if (t.getLexeme().equals("(")) {
            t = lexer.GetNextToken();
            OK(t);
            paramList();
            t = lexer.GetNextToken();
            if (!t.getLexeme().equals(")"))
                Error(t, "a ')' is expected");
            OK(t);
        } else
            Error(t, "a '(' is expected");
        statementBlock();

    }

    //paramList-->type identifier->{, type identifier} | empty
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

    //statement
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

    //varDeclarStatement-->var type->identifier->{,identifier} ;
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

    //letStatement-->let identifier->[expression] = expression ;
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
        if (!t.getLexeme().equals("[") && !t.getLexeme().equals("="))
            Error(t, "a '=' or '[' is expected"); // let identifier [expression] = expression; |or| let identifier =
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

    //ifStatement-->if(expression) statementBlock->[else statementBlock]
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

    // statementBlock-->{statement} 
    // (this is a recursive statement method) -> named subroutineBody in the jack grammar description)
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

    // whileStatement-->while (expression){statementBlock}
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

    // doStatement-->do subRoutineCall;
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

    // returnStatement-->return[expression];
    public void returnStatement() {
        MyToken t = lexer.PeekNextToken();
        if (!t.getLexeme().equals("return"))
            Error(t, "an identifier is expected: 'return'");
        MyToken returnToken = lexer.GetNextToken();
        OK(returnToken);
        t = lexer.PeekNextToken();
        if (!t.getLexeme().equals(";")) {
            expression();
            t = lexer.PeekNextToken();
        }
        MyToken semicolon1 = lexer.GetNextToken();
        OK(semicolon1);

    }

    // subRoutineCall-->identifier[.identifier](expressionList)
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

    // expressionList-->expression {,expression} | empty
    public void expressionList() {
        MyToken t = lexer.PeekNextToken();
        if (!t.getLexeme().equals(")")) {
            expression();
            t = lexer.PeekNextToken();
            while (!t.getLexeme().equals(")")) {
                MyToken commaExpList = lexer.GetNextToken();
                if (!commaExpList.getLexeme().equals(","))
                    Error(t, "a ',' or ')' is expected)");
                OK(commaExpList);
                expression();
                t = lexer.PeekNextToken();
            }
        }
        OK(t);
    }

    // expression-->relationalExpression->((&| "|" )relationalExpression)
    public void expression() {
        MyToken t = lexer.PeekNextToken();
        relationalExpression();
        t = lexer.PeekNextToken();
        while (t.getLexeme().equals("&") || t.getLexeme().equals("|")) {
            MyToken expressionSymbol = lexer.GetNextToken();
            OK(expressionSymbol);
            relationalExpression();
            t = lexer.PeekNextToken();
        }
    }

    // relationalExpression-->arithmeticExpression->((=|<|>)arithmeticExpression)
    public void relationalExpression() {
        MyToken t = lexer.PeekNextToken();
        arithmeticExpression();
        t = lexer.PeekNextToken();
        while (t.getLexeme().equals("=") || t.getLexeme().equals(">") || t.getLexeme().equals("<")) {
            MyToken relationSymbol = lexer.GetNextToken();
            OK(relationSymbol);
            arithmeticExpression();
            t = lexer.PeekNextToken();
        }
    }

    // arithmeticExpression-->term->((+|-)term)
    public void arithmeticExpression() {
        MyToken t = lexer.PeekNextToken();
        term();
        t = lexer.PeekNextToken();
        while (t.getLexeme().equals("+") || t.getLexeme().equals("-")) {
            MyToken arithmeticSymbol = lexer.GetNextToken();
            OK(arithmeticSymbol);
            term();
            t = lexer.PeekNextToken();
        }
    }

    // term-->factor->loop((*|/)factor)
    public void term() {
        MyToken t = lexer.PeekNextToken();
        factor();
        t = lexer.PeekNextToken();
        while (t.getLexeme().equals("*") || t.getLexeme().equals("/")) {
            MyToken termSymbol = lexer.GetNextToken(); // consume the * or /
            OK(termSymbol);
            factor();
            t = lexer.PeekNextToken();
        }
    }

    // factor-->(+|-| )operand
    public void factor() {
        MyToken t = lexer.PeekNextToken();
        if (t.getLexeme().equals("-") || t.getLexeme().equals("~")) {
            MyToken factorSymbol = lexer.GetNextToken();
            OK(factorSymbol);
            operand();
        } else
            operand();
    }

    //operand
    public void operand() {
        MyToken t = lexer.PeekNextToken();
        if (t.getTokenType() == MyToken.TokenType.INT_LITERAL) {
            MyToken integ = lexer.GetNextToken();
            OK(integ);
        } else if (t.getTokenType() == MyToken.TokenType.IDENTIFIER) {
            MyToken identify1 = lexer.GetNextToken();
            OK(identify1);
            t = lexer.PeekNextToken();
            if (t.getLexeme().equals("[") || t.getLexeme().equals("(")) {
                MyToken openBrace0 = lexer.GetNextToken();
                OK(openBrace0);
                if (openBrace0.getLexeme().equals("[")) {
                    expression();
                    MyToken closeBrace0 = lexer.GetNextToken();
                    if (!closeBrace0.getLexeme().equals("]"))
                        Error(closeBrace0, "a ']' is expected");
                    OK(closeBrace0);
                } else if (openBrace0.getLexeme().equals("(")) {
                    expressionList();
                    MyToken closeBrace0 = lexer.GetNextToken();
                    if (!closeBrace0.getLexeme().equals(")"))
                        Error(closeBrace0, "a ')' is expected");
                    OK(closeBrace0);
                }
            } else if (t.getLexeme().equals(".")) {
                MyToken dot = lexer.GetNextToken();
                OK(dot);
                t = lexer.PeekNextToken();
                if (t.getTokenType() != MyToken.TokenType.IDENTIFIER)
                    Error(t, "an identifier expected");
                MyToken identify2 = lexer.GetNextToken();
                OK(identify2);
                t = lexer.PeekNextToken();
                if (t.getLexeme().equals("[") || t.getLexeme().equals("(")) {
                    MyToken openBrace1 = lexer.GetNextToken();
                    OK(openBrace1);
                    if (openBrace1.getLexeme().equals("[")) {
                        expression();
                        MyToken closeBrace1 = lexer.GetNextToken();
                        if (!closeBrace1.getLexeme().equals("]"))
                            Error(closeBrace1, "a ']' is expected");
                        OK(closeBrace1);
                    } else if (openBrace1.getLexeme().equals("(")) {
                        expressionList();
                        MyToken closeBrace1 = lexer.GetNextToken();
                        if (!closeBrace1.getLexeme().equals(")"))
                            Error(closeBrace1, "a ')' is expected");
                        OK(closeBrace1);
                    }
                }
            }
        } else if (t.getLexeme().equals("(")) {
            MyToken openBrace2 = lexer.GetNextToken();
            OK(openBrace2);
            expression();
            MyToken closeBrace2 = lexer.GetNextToken();
            if (!closeBrace2.getLexeme().equals(")"))
                Error(closeBrace2, "a ')' is expected");
            OK(closeBrace2);
        } else if (t.getTokenType() == MyToken.TokenType.STRING_LITERAL) {
            MyToken stringLiteral = lexer.GetNextToken();
            OK(stringLiteral);
        } else if (t.getLexeme().equals("true")) {
            MyToken trueToken = lexer.GetNextToken();
            OK(trueToken);
        } else if (t.getLexeme().equals("false")) {
            MyToken falseToken = lexer.GetNextToken();
            OK(falseToken);
        } else if (t.getLexeme().equals("null")) {
            MyToken nullToken = lexer.GetNextToken();
            OK(nullToken);
        } else if (t.getLexeme().equals("this")) {
            MyToken thisToken = lexer.GetNextToken();
            OK(thisToken);
        } else
            Error(t, "unknown operand");
    }

   
    // testing the parser
    public static void main(String[] args) {
        MyParser trialParser = new MyParser(args[0]);

        trialParser.jackProgram();
        System.out.println("Parsing Successful, no grammar errors");

    }

}
