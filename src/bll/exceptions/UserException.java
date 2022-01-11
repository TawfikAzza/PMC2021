package bll.exceptions;

public class UserException extends Throwable {
    String exceptionMessage;
    public UserException(String exceptionMessage,Exception exception){
        System.out.println( exceptionMessage+"\n" + exception);
        this.exceptionMessage=exceptionMessage;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }
}
