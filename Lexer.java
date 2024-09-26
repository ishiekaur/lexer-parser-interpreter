import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class Lexer {

    String buffer;
    int index = 0;
    public static final String INTTOKEN = "INT";
    public static final String IDTOKEN = "ID";
    public static final String ASSMTTOKEN = "ASSMT";
    public static final String PLUSTOKEN = "PLUS";
    public static final String EOFTOKEN = "EOF";
    public static final String UNKNOWNTOKEN = "UNKNOWN";
    public static final String NEWLINETOKEN = "NEWLINE";
    private int lineNumber = 1; // Track line numbers here

    public Lexer(String fileName) {
        getInput(fileName);
    }

    private void getInput(String fileName) {
        try {
            Path filePath = Paths.get(fileName);
            byte[] allBytes = Files.readAllBytes(filePath);
            buffer = new String(allBytes);
        } catch (IOException e) {
            System.out.println("You did not enter a valid file name in the run arguments.");
            System.out.println("Please enter a string to be parsed:");
            Scanner scanner = new Scanner(System.in);
            buffer = scanner.nextLine();
        }
    }

    public ArrayList<Token> getAllTokens() {
        return getNextToken(); // Simply return the token list generated
    }

    public ArrayList<Token> getNextToken() {
        ArrayList<Token> mylist = new ArrayList<>();

        while (index < this.buffer.length()) {
            char ch = this.buffer.charAt(index);

            if (Character.isWhitespace(ch)) {
                // Handle new lines separately to increment lineNumber
                if (ch == '\n') {
                    lineNumber++; // Increment line number for new lines
                }
                index++;
                continue;
            }

            if (Character.isDigit(ch)) {
                String integer = getInteger();
                mylist.add(new Token(INTTOKEN, integer, lineNumber));
            } else if (Character.isLetter(ch)) {
                String identifier = getIdentifier();
                mylist.add(new Token(IDTOKEN, identifier, lineNumber));
            } else if (ch == '=') {
                mylist.add(new Token(ASSMTTOKEN, String.valueOf(ch), lineNumber));
                index++;
            } else if (ch == '+') {
                mylist.add(new Token(PLUSTOKEN, String.valueOf(ch), lineNumber));
                index++;
            } else {
                // Anything else is considered an unknown token
                mylist.add(new Token(UNKNOWNTOKEN, String.valueOf(ch), lineNumber));
                index++;
            }
        }
        // Add EOF token when the file ends
        mylist.add(new Token(EOFTOKEN, "EOF", lineNumber));
        return mylist;
    }

    private String getIdentifier() {
        StringBuilder identifier = new StringBuilder();
        while (index < this.buffer.length() && Character.isLetterOrDigit(this.buffer.charAt(index))) {
            char ch = this.buffer.charAt(index);
            identifier.append(ch);
            index++;
        }
        return identifier.toString();
    }

    private String getInteger() {
        StringBuilder integer = new StringBuilder();
        while (index < this.buffer.length() && Character.isDigit(this.buffer.charAt(index))) {
            char ch = this.buffer.charAt(index);
            integer.append(ch);
            index++;
        }
        return integer.toString();
    }

    public static void main(String[] args) {
        String fileName = "";
        if (args.length == 0) {
            System.out.println("You can test a different file by adding as an argument");
            System.out.println("See comment above main");
            System.out.println("For this run, test.txt used");
            fileName = "test.txt"; // Default file
        } else {
            fileName = args[0];
        }

        Lexer lexer = new Lexer(fileName);
        // Print out the token list
        ArrayList<Token> tokens = lexer.getAllTokens();
        for (Token token : tokens) {
            System.out.println(token);
        }
    }
}
