public class Main {
    public static void main(String[] args){
     
        HashTable<String, String> table = new HashTable<>();
        table.put("Panther", "The master electrician");
        table.put("Bridget", "My bestest female friend");
        table.put("Pau", "Weird dynamic");
        table.put("Nath", "My pookie :3");
        table.put("Nath", "MY POOKIE");
       
        System.out.println("Estado inicial del buzón");
        
        table.printTable();
        
        table.remove("Pau");
        
        System.out.println("Segundo estado del buzón");
        table.printTable();
        
    }
}
