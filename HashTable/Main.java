public class Main {
    public static void main(String[] args){
     
        HashTable<String, Integer> table = new HashTable<>();
        table.put("Panther", 1);
        table.put("Bridget", 2);
        table.put("Paul", 3);
        table.put("Nath", 4);
        table.put("Nath", 5);
        table.put("Null", 6); 
        table.put("Nul", 7); 
        table.put("ull", 8); 
        table.put("N l", 9); 
        table.put("l", 10); 
        table.put("Nuasd", 11); 
        table.put("was", 12); 
        table.put("Nula", 13); 

        System.out.println("Estado inicial del buzón");
        
        table.printTable();
        
        table.remove("Paul");
        
        System.out.println("Segundo estado del buzón");
        table.printTable();
        
    }
}
