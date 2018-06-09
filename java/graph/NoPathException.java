package graph;


public class NoPathException extends Exception {
    /** Message show if raise thiswith no message */
    private String message = " No Path ";

    /** Constructor of this, with a message */
    public NoPathException(String message){
        this.message = message;
    }

    /** Get message of this */
    public String getMessage(){
        return message;
    }
}
