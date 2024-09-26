import java.util.HashMap;

public class IdTable {

    // HashMap to store the identifiers and their addresses
    private HashMap<String, Integer> map;

    // Constructor to initialize the IdTable
    public IdTable() {
        map = new HashMap<>();
    }

    // Method to add an entry to the map
    public void add(String identifier, int address) {
        map.put(identifier, address);
    }


    // Method to get the address of an identifier, or return -1 if not found
    public int getAddress(String identifier) {
        if (map.containsKey(identifier)) {
            return map.get(identifier);
        } else {
            return -1;
        }
    }

    // Optional: Method to return the IdTable contents as a string (for debugging)
    public String toString() {
        return map.toString();
    }
}
