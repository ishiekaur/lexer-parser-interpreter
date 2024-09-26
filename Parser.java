import java.util.ArrayList;

public class Parser {

    public ArrayList<Token> tokens; // A list of tokens (assumed to be strings for simplicity)
    public idTable idTable;  // The IdTable instance
    public int currentToken; // Keeps track of which token the Parser is on
    public int lineNumber; // Keeps track of which line the parser is on.
    public boolean isValid;
    public int address;
    
    public Parser(Lexer lexer) {
        // Initialize the parser with tokens from the Lexer
        this.tokens = lexer.getAllTokens();
        this.idTable = new idTable();
        // this.currentToken = 0;
        this.lineNumber = 1;
        this.isValid = true;
        this.address = 0;
    }

    // Main method to parse the entire program
    public void parseProgram() {
        // A statement that says, while this condition, parse through the program
        while (currentToken < tokens.size() && isValid) {
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
        if (!isValid) {
            return;
        }
        
        if (id.type.equals(Lexer.IDTOKEN) && idTable.getAddress(id.getValue()) == -1) {
            isValid = true;
            // Add the identifier to the IdTable
            idTable.add(id.getValue(), address);
            address++;
        } else {
            System.out.println("Expecting Identifier at Line " + id.getLineNumber());
            isValid = false;
            return;
        }
    }

    // Method to parse an AssignOp
    public void parseAssignOp() {
        Token assignOp = nextToken();
        if (!isValid) {
            return;
        }
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
        if (!isValid) {
            return;
        }
        if (expression.type.equals(Lexer.EOFTOKEN)) {
            return;
        }

        // Check if the expression contains an IDTOKEN that is already in the IdTable or an INTTOKEN
        if ((expression.type.equals(Lexer.IDTOKEN) && idTable.getAddress(expression.getValue()) != -1) 
            || expression.type.equals(Lexer.INTTOKEN)) {
            isValid = true;
        } else {
            System.out.println("Expecting Identifier or Integer at Line " + expression.getLineNumber());
            isValid = false;
            return;
        }

        // Handle additional expressions (e.g., after a PLUS token)
        while (currentToken < tokens.size()) {
            Token next = nextToken();
            if (next.type.equals(Lexer.PLUSTOKEN)) {
                isValid = true;
            } else if (next.type.equals(Lexer.EOFTOKEN)) {
                System.out.println("End of file reached in expression.");
                return;
            } else {
                currentToken--;
                return;
            }
            // After a PLUS, we expect another identifier or integer
            Token nextT = nextToken();
            if ((nextT.type.equals(Lexer.IDTOKEN) && idTable.getAddress(nextT.getValue()) != -1)|| nextT.type.equals(Lexer.INTTOKEN)) {
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
        if (currentToken < tokens.size()) {
            return tokens.get(currentToken++);
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
        System.out.println(parser.idTable.toString());

        // Check if the program is valid
        if (parser.isValid) {
            System.out.println("\nThe program is valid.");
        } else {
            System.out.println("\nThe program is invalid.");
        }
    }
}
