public class Token {
    public String type;
    public String value;
    public int lineNumber;

    public Token(String type, String value, Integer lineNumber) {
        this.type=type;
        this.value=value;
        this.lineNumber = lineNumber;
       
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }
    public int getLineNumber() {
        return lineNumber;
    }

    public String toString(){

        return type + " " + value + " ";
    }

}