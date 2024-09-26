import java.util.ArrayList;

public class Parser {

    public ArrayList<Token> tokens; // A list of tokens (assumed to be strings for simplicity)
    public IdTable table;  // The IdTable instance
    public int currentTokenIndex; // Keeps track of which token the Parser is on
    private int lineNumber; // Keeps track of which line the parser is on.
    private boolean isValid;
    private int address;
    
    public Parser(Lexer lexer) {
        // Initialize the parser with tokens from the Lexer
        this.tokens = lexer.getAllTokens();
        this.table = new IdTable();
        // this.currentToken = 0;
        this.lineNumber = 1;
        this.isValid = true;
        // this.address = 0;
    }

    // Main method to parse the entire program
    public void parseProgram() {
        // A statement that says, while this condition, parse through the program
        while (currentTokenIndex < tokens.size() && isValid) {
            parseAssignment();
        }
    }

    // Method that will parse the entire program
    public void parseAssignment(){
        parseId();
        parseAssignOp();
        parseExpression();
    }

    // Method to parse an identifier
    public void parseId() {
        Token id = nextToken();
        if (id.type.equals(Lexer.IDTOKEN) && table.getAddress(id.getValue()) == -1) {
            isValid = true;
            // Add the identifier to the IdTable
            table.add(id.getValue(), this.address);
            this.address++;
        }
        else {
            System.out.println("Expecting Identifier at Line " + id.getLineNumber());
            isValid = false;
            return;
        }
    }

    // Method to parse an AssignOp
    public void parseAssignOp() {
        Token assignOp = nextToken();
        if (assignOp.type.equals(Lexer.ASSMTTOKEN)) {
            isValid = true;
        } else {
            System.out.println("Expecting Assignment Operator at Line " + assignOp.getLineNumber());
            isValid = false;
            return;
        }
    }

    // Method to parse an expression
    public void parseExpression() {
        Token expression = nextToken();
        // If the expression contains an IDTOKEN that is already in the IdTable or an INTTOKEN, T
        if ((expression.type.equals(Lexer.IDTOKEN) && table.getAddress(expression.getValue()) != -1) 
            || expression.type.equals(Lexer.INTTOKEN)) {
            isValid = true;
        } else {
            System.out.println("Expecting Identifier or Integer at Line " + expression.getLineNumber());
            isValid = false;
            return;
        }

        // Handle additional expressions (e.g., after a PLUS token)
        while (currentTokenIndex < tokens.size()) {
            Token next = nextToken();
            if (next.type.equals(Lexer.PLUSTOKEN)) {
                isValid = true;
            } else if (next.type.equals(Lexer.EOFTOKEN)) {
                System.out.println("End of file reached in expression.");
                return;
            } else {
                currentTokenIndex--;
                return;
            }

            // After a PLUS, we expect another identifier or integer
            Token nextT = nextToken();
            if ((nextT.type.equals(Lexer.IDTOKEN) && table.getAddress(nextT.getValue()) != -1)|| nextT.type.equals(Lexer.INTTOKEN)) {
                isValid = true;
            } else {
                System.out.println("Expecting Identifier or Integer at Line " + expression.getLineNumber());
                isValid = false;
                return;
            }
        }
    }

    // Method to get the next token and increment the index
    public Token nextToken() {
        if (currentTokenIndex < tokens.size()) {
            return tokens.get(currentTokenIndex++);
        } else {
            return null;
        }
    }

    public static void main(String[] args) {
        // Read the input from tests.txt
        String input = "test.txt";
        
        // Create a lexer from the input
        Lexer lexer = new Lexer(input);
        
        // Create a parser with the lexer
        Parser parser = new Parser(lexer);
        
        // Parse the program
        parser.parseProgram();

        // Print the token list and the ID table to check results
        System.out.println("\nTokens:");
        for (Token token : parser.tokens) {
            System.out.println(token.toString());
        }

        System.out.println("\nID Table:");
        System.out.println(parser.table.toString());

        // Check if the program is valid
        if (parser.isValid) {
            System.out.println("\nThe program is valid.");
        } else {
            System.out.println("\nThe program is invalid.");
        }
    }
}
