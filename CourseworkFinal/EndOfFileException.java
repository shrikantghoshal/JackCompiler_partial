//meant to be used in MyLexer when the list of characters no longer has any elements inside
public class EndOfFileException extends Exception{
    public EndOfFileException(String errorName){
        super(errorName);
    }
}

