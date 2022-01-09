package bll.exceptions;

public class CategoryException extends Throwable{
    String exceptionMessage;
    public CategoryException(String exceptionMessage,Exception exception){
        System.out.println(exceptionMessage+"\n"+exception);
        this.exceptionMessage=exceptionMessage;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }
}
