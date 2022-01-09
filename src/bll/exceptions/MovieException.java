package bll.exceptions;

public class MovieException extends Throwable {
    String exceptionMessage;
    public MovieException(String exceptionMessage,Exception exception){
        System.out.println( exceptionMessage+"\n" + exception);
        this.exceptionMessage=exceptionMessage;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }
}
