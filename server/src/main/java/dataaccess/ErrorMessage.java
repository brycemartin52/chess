package dataaccess;

public class ErrorMessage {
    String message;

    public ErrorMessage(){}

    public ErrorMessage(String message){
        this.message = message;
    }

    public String getMessage(){
        return message;
    }
}
