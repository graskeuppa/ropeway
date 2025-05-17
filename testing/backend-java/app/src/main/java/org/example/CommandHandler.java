// Aquí estarán las importaciones de mi implementación de HashTable y AVL, definiré los métodos del mismo 
// modo que los revisé la otra vez. Veamos

package org.example;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import java.io.*;
import java.util.*;

public class CommandHandler {
    private final Gson gson = new Gson();

    public String process(String line) {

        String[] segments = line.trim().split(" ");

        // When command argument is empty
        if (segments.length == 0)
            return gson.toJson(Map.of("error", "Empty command, yo!"));

        // The first element of the array shall be the command
        String command = segments[0];

        switch (command) {
            // Creates a new instance of Move
            case "MAKE_MOVE_AL":
                // MAKE_MOVE <date> <amount> <tag> <source>
                if (segments.length < 5)
                    return gson.toJson(Map.of("Command err",
                            "Not enough arguments, yo! ( MAKE_MOVE_AL <date> <amount> <tag> <source> )"));
                return makeMoveAL(segments);

            // Calls getMovesPerDate (returns all the moves associated with a given date)
            case "GET_MOVES_PER_DATE":
                // GET_DATE <yyyy-mm-dd> or whatever format is placed in, so long as it is
                // consistent
                if (segments.length < 2)
                    return gson.toJson(Map.of("error", "No date, yo!"));
                return getMovesPerDate(segments[1]);

            // MORE COMMANDS HERE!

            default: // Given command does not exist
                return gson.toJson(Map.of("error", "No such command, yo!"));
        }
    }

    private String getMovesPerDate(String date) {
        ArrayList<Move> moves = new ArrayList<>();
        // Mock
        moves.add(new Move(date, 123, "TPT", "Efectivo"));

        return gson.toJson(Map.of("Moves", moves));
    }

    private String makeMoveAL(String[] arguments) {

        File file = new File("./JSON/movesAL.json");
        file.getParentFile().mkdirs();
        ArrayList<Move> moves;

        // Loading or creating a file with the stored list of moves
        if (file.exists()) { // 1. File exists, deserialises the list

            try (Reader reader = new FileReader(file)) {
                moves = gson.fromJson(reader, new TypeToken<ArrayList<Move>>() {
                }.getType());
                if (moves == null)
                    moves = new ArrayList<>();
                // If something goes wrong with any of the above, make a new list
            } catch (IOException e) {
                e.printStackTrace();
                moves = new ArrayList<>();
            }

        } else { // 2. Files does not exist, start with an empty list and make a new file

            moves = new ArrayList<>();

            try {
                if (file.createNewFile()) {
                    System.out.println("File created!");
                    // FileWriter writer = new FileWriter(file);
                    // writer.write("[]");
                    // writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        // Adding a move
        try {
            String date = arguments[1];
            double amount = Double.parseDouble(arguments[2]);
            String tag = arguments[3];
            String source = arguments[4];

            moves.add(new Move(date, amount, tag, source));

            // Writing the list to the file
            try (Writer writer = new FileWriter(file)) {
                gson.toJson(moves, writer);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String result = gson.toJson(Map.of("Moves", moves));
            System.out.println(result);
            return result;
        } catch (Exception e) {
            System.err.println("Something went wrong, yo!");
            return "Exiting, yo!";
        }

    }

}
