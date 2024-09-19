import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Class to build an array of Tokens from an input file
 * @author wolberd
 * @see Token
 * @see Parser
 */
public class Lexer {

    String buffer;
    int index = 0;
    public static final String INTTOKEN="INT";
    public static final String IDTOKEN="ID";
    public static final String ASSMTTOKEN="ASSMT";
    public static final String PLUSTOKEN="PLUS";
    public static final String EOFTOKEN="EOF";
    public static final String UNKNOWNTOKEN="UNKNOWN";

    /**
     * call getInput to get the file data into our buffer
     * @param fileName the file we open
     */
    public Lexer(String fileName) {

        getInput(fileName);
    }

    /**
     * Reads given file into the data member buffer
     * @param fileName name of file to parse
    */
    private void getInput(String fileName)  {
        try {
            Path filePath = Paths.get(fileName);
            byte[] allBytes = Files.readAllBytes(filePath);
            buffer = new String (allBytes);
        } catch (IOException e) {
            System.out.println ("You did not enter a valid file name in the run arguments.");
            System.out.println ("Please enter a string to be parsed:");
            Scanner scanner = new Scanner(System.in);
            buffer=scanner.nextLine();
        }
    }

    public ArrayList<Token> getAllTokens(){

        ArrayList<Token> mytokenlist = getNextToken();
        
        for (int i = 0; i<mytokenlist.size(); i++){
            mytokenlist.get(i);
        }

        return mytokenlist;
    }
    /**
     * Return all the token in the file
     * @return ArrayList of Token
     */

    // This is the method where you loop through the buffer (this.buffer) and print out the tokens. 
    public ArrayList<Token> getNextToken()
    {
        ArrayList<Token> mylist = new ArrayList<>();

        //TODO: place your code here for lexing file
        while(index<this.buffer.length()){
            char ch = this.buffer.charAt(index);

            if (Character.isWhitespace(ch)){
                index++;
                continue;
            }
            if (Character.isDigit(ch)){
                String integer = getInteger();
                mylist.add(new Token(INTTOKEN, integer));
            }
            
            else if (Character.isLetter(ch)){
                String identifier = getIdentifier();
                mylist.add(new Token(IDTOKEN, identifier));
            }
            else if (ch == '='){
                mylist.add(new Token(ASSMTTOKEN, String.valueOf(ch)));
                index++;
            }
            else if (ch == '+'){
                mylist.add(new Token(PLUSTOKEN, String.valueOf(ch)));
                index++;
            }
            else{
                mylist.add(new Token(UNKNOWNTOKEN, String.valueOf(ch)));
                index++;
            }
        }
        mylist.add(new Token("EOF", EOFTOKEN));
        return mylist; 
    }

private String getIdentifier(){
    StringBuilder identifier = new StringBuilder();
    while (index<this.buffer.length() && Character.isLetterOrDigit(this.buffer.charAt(index))){
        char ch = this.buffer.charAt(index);
        identifier.append(ch);
        index++;
    }
    return identifier.toString();
}

private String getInteger(){
    StringBuilder integer = new StringBuilder();
    while (index<this.buffer.length() && Character.isDigit(this.buffer.charAt(index))){
        char ch = this.buffer.charAt(index);
        integer.append(ch);
        index++;
    }
    return integer.toString();
}


    /**
     * Before your run this starter code
     * Select Run | Edit Configurations from the main menu.
     * In Program arguments add the name of file you want to test (e.g., test.txt)
     * @param args args[0]
     */
    public static void main(String[] args) {
        String fileName="";
        if (args.length==0) {
            System.out.println("You can test a different file by adding as an argument");
            System.out.println("See comment above main");
            System.out.println("For this run, test.txt used");
            // Change this value to the filename of other examples
            fileName="test.txt";
        } else {

            fileName=args[0];
        }

        Lexer lexer = new Lexer(fileName);
        // just print out the text from the file
        System.out.println(lexer.getAllTokens());
        // here is where you'll call getAllTokens



    }
}
	