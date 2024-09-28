import java.util.ArrayList;

public class BytecodeInterpreter {

    private Lexer lexer;
    private Parser parser;
    
    // create constants (final) for LOAD, LOADI, STORE
    public static final int LOAD = 0;
    public static final int LOADI = 1;
    public static final int STORE = 2;
    
    // create ArrayList datamember "bytecode" for bytecode
    public ArrayList<String> bytecode = new ArrayList<>();
    
    // create private int data member for accumulator 
    private int accumulator = 0;

    private int memoryAddress = 0;
    
    // create ArrayList datamember "memory" for memory
    public ArrayList<String> memory;
    
    // create private int data member for size of memory
    private int memorySize;
    
    public BytecodeInterpreter(int memorySize){
    // create constructor that specifies size of memory
        this.memorySize = memorySize;
        this.memory = new ArrayList<>(memorySize);

        for (int i = 0; i<memorySize; i++){
            memory.add("0");
        }

        String input = "test.txt";
        lexer = new Lexer(input);
        parser = new Parser(lexer);
    }

    public ArrayList<String> generate(){
        while (parser.currentTokenIndex<parser.tokens.size()){
            load();
            loadi();
            store();
        }
        return bytecode;
    }

    private void load(){
        
        if (parser.isValid){
            while(parser.currentTokenIndex<parser.tokens.size()){
                Token token = parser.nextToken();
                // if the idtoken value is equal to value in memory address
                if (token.type.equals(Lexer.IDTOKEN)){
                    bytecode.add(String.valueOf(LOAD));
                    bytecode.add(String.valueOf(memoryAddress));

                }
                break;
            }
        }
    }

    private void loadi(){

        if (parser.isValid){
            while(parser.currentTokenIndex<parser.tokens.size()){
                Token token = parser.nextToken();
                if (token.type.equals(Lexer.INTTOKEN)){
                    // If it's an integer token, add LOADI opcode
                    bytecode.add(String.valueOf(LOADI)); // Add LOADI opcode
                    accumulator += Integer.parseInt(token.getValue());
                    bytecode.add(token.getValue()); // Add the integer value
                    if (!token.type.equals(Lexer.PLUSTOKEN) || !token.type.equals(Lexer.INTTOKEN)){
                        parser.currentTokenIndex--;
                        break;
                    }
                }
            }
     
        }
    }

    private void store(){

        if (parser.isValid){
            while(parser.currentTokenIndex<parser.tokens.size()){
                Token token = parser.nextToken();
                if (token.type.equals(Lexer.INTTOKEN)){
                    bytecode.add(String.valueOf(STORE)); // Add STORE opcode
                    bytecode.add(String.valueOf(memoryAddress)); // Store accumulator value at memory address 
                    if (memoryAddress<memorySize){
                        memory.set(memoryAddress, String.valueOf(accumulator));
                        memoryAddress++;
                    }
                }
                break;
            }
     
        }
    }

    
    public static void main(String[] args) {
        // Create an instance of BytecodeInterpreter with memory size of 10
        BytecodeInterpreter interpreter = new BytecodeInterpreter(10);

        interpreter.generate();


        // Print bytecode
        System.out.println("Bytecode:");
        System.out.println(interpreter.bytecode);

        // Print memory
        System.out.println("Memory:");
        System.out.println(interpreter.memory);
    }
}
