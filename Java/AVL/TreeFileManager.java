import java.io.*;
import java.util.Scanner;

public class TreeFileManager {

    // Constructor
    public TreeFileManager() {
    }

    // Almacena una cadena en un archivo .txt
    public void saveToFile(String input) {
        File file = new File("Arboles.txt");
    
        try {
            StringBuilder content = new StringBuilder();
    
            // Si el archivo ya existe, lee el contenido
            if (file.exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String existingContent = reader.readLine();
                    if (existingContent != null && !existingContent.isEmpty()) {
                        content.append(existingContent).append(",");
                    }
                }
            }
    
            // Agrega los números nuevos al contenido
            content.append(input);
    
            // Escribe el contenido nuevo al archivo
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(content.toString());
            }
    
            System.out.println("Contenido guardado exitosamente en Arboles.txt.");
        } catch (IOException e) {
            System.err.println("Error al guardar el archivo: " + e.getMessage());
        }
    }

    // Crea un árbol a partir de un archivo
    public AVL<Integer> loadFromFile() {
        File file = new File("Arboles.txt");
        AVL<Integer> avlTree = new AVL<>();
    
        if (!file.exists()) {
            System.out.println("No hay archivo. Se devolverá un árbol vacío.");
            return avlTree; // Devuelve un árbol vacío si el archivo no existe
        }
    
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String content = reader.readLine();
    
            // Divide las llaves
            if (content == null || content.isEmpty()) {
                System.out.println("El archivo está vacío. Se devolverá un árbol vacío.");
                return avlTree;
            }
    
            String[] keys = content.split(",");
            for (String key : keys) {
                // Convierte cada llave en un entero y la inserta en el árbol
                try {
                    int intKey = Integer.parseInt(key.trim());
                    avlTree.insert(intKey, intKey); // Inserta la llave como dato
                } catch (NumberFormatException e) {
                    System.err.println("Error al convertir la llave: " + key.trim());
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        }
        System.out.println("Árbol AVL cargado exitosamente desde Arboles.txt.");
        return avlTree;
    }

    // Crea una cadena con los contenidos de un archivo
    public String readFileContent() {
        File file = new File("Arboles.txt");
    
        if (!file.exists()) {
            return "El archivo no existe.";
        }
    
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String content = reader.readLine();
            if (content == null || content.isEmpty()) {
                return "El archivo está vacío.";
            }
            return content;
        } catch (IOException e) {
            return "Error al leer el archivo: " + e.getMessage();
        }
    }

}