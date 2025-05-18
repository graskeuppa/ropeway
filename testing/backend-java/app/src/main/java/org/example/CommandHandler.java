// Aquí estarán las importaciones de mi implementación de HashTable y AVL, definiré los métodos del mismo 
// modo que los revisé la otra vez. Veamos

package org.example;

import DS.AVL.*;
import DS.HTable.*;
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
            // Creates a new instance of Move, adds it to an ArrayList
            case "MAKE_MOVE_AL":
                // MAKE_MOVE <date> <amount> <tag> <source>
                if (segments.length < 5)
                    return gson.toJson(Map.of("Command err",
                            "Not enough arguments, yo! - Expected: MAKE_MOVE_AL date amount tag source"));
                return makeMoveAL(segments);

            // Creates a new move, adds it to three HashTables, with the keys being the
            // date, tag and source, respectively
            case "MAKE_MOVE":

                if (segments.length < 5)
                    return gson.toJson(Map.of("Command err",
                            "Not enough arguments, yo! - Expected: MAKE_MOVE date amount tag source"));
                return makeMove(segments);

            // Calls getMovesPerDate (returns all the moves associated with a given date)
            case "GET_MOVES_PER_DATE":
                // GET_DATE <yyyy-mm-dd> or whatever format is placed in, so long as it is
                // consistent
                if (segments.length < 2)
                    return gson.toJson(Map.of("Command err", "No date, yo! - Expected: GET_MOVES_PER_DATE date"));
                return getMovesPerDate(segments[1]);

            // MORE COMMANDS HERE!

            default: // Given command does not exist
                return gson.toJson(Map.of("Wrong command err", "No such command, yo!"));
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

    private String makeMove(String[] arguments) {

        // File creation/retrival. TAG stores the tag key indexed HashTable, SOURCE does
        // the same, but using the source as the key index; DATES stores the AVL with
        // nodes indexed through dates.
        // This will come in handy later for search combinations such as "All moves with
        // x tag made during (n,a) period of time, or "All moves with x source of money
        // made during (t,o) period, etc."

        File movesTAG = new File("./JSON/movesTAG.json");
        movesTAG.getParentFile().mkdirs();
        HTable<String, Move> pertag;

        File movesSOURCE = new File("./JSON/movesSOURCE.json");
        movesSOURCE.getParentFile().mkdirs();
        HTable<String, Move> persource;

        File movesDATE = new File("./JSON/movesDATE.json");
        movesDATE.getParentFile().mkdirs();
        AVL<Move> perdate;

        // a). TAG
        // If file exists, deserialises the HTable
        if (movesTAG.exists()) {
            try (Reader reader = new FileReader(movesTAG)) {
                pertag = gson.fromJson(reader, new TypeToken<HTable<String, Move>>() {
                }.getType());
                if (pertag == null)
                    pertag = new HTable<>();
            } catch (IOException e) {
                e.printStackTrace();
                pertag = new HTable<>();
            }
        } else { // File does not exist, make a new one

            pertag = new HTable<>();

            try {
                if (movesTAG.createNewFile()) {
                    System.out.println("File created!");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        try {
            // Adding a new move to the HTable using the tag as a key
            String date = arguments[1];
            double amount = Double.parseDouble(arguments[2]);
            String tag = arguments[3];
            String source = arguments[4];

            pertag.put(tag, new Move(date, amount, tag, source));

            // Writing the list to the file
            try (Writer writer = new FileWriter(movesTAG)) {
                gson.toJson(pertag, writer);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // String result1 = gson.toJson(Map.of("MovesTAG", pertag));

        } catch (Exception e) {
            System.err.println("Something went wrong, yo!");
        }

        // b). SOURCE
        // If file exists, deserialises the HTable
        if (movesSOURCE.exists()) {
            try (Reader reader = new FileReader(movesSOURCE)) {
                persource = gson.fromJson(reader, new TypeToken<HTable<String, Move>>() {
                }.getType());
                if (persource == null)
                    persource = new HTable<>();
            } catch (IOException e) {
                e.printStackTrace();
                persource = new HTable<>();
            }
        } else { // File does not exist, make a new one

            persource = new HTable<>();

            try {
                if (movesSOURCE.createNewFile()) {
                    System.out.println("File created!");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        try {
            // Adding a new move to the HTable using the tag as a key
            String date = arguments[1];
            double amount = Double.parseDouble(arguments[2]);
            String tag = arguments[3];
            String source = arguments[4];

            persource.put(source, new Move(date, amount, tag, source));

            // Writing the list to the file
            try (Writer writer = new FileWriter(movesSOURCE)) {
                gson.toJson(persource, writer);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // String result2 = gson.toJson(Map.of("MovesSOURCE", persource));

        } catch (Exception e) {
            System.err.println("Something went wrong, yo!");
        }

        // c). DATE
        // If file exists, deserialises the AVL

        if (movesDATE.exists()) {

            try (Reader reader = new FileReader(movesDATE)) {
                perdate = gson.fromJson(reader, new TypeToken<AVL<Move>>() {
                }.getType());

            } catch (IOException e) {
                e.printStackTrace();
                perdate = new AVL<>();
            }
        } else { // File does not exists, make a new one

            perdate = new AVL<>();

            try {

                if (movesDATE.createNewFile()) {
                    System.out.println("File created, yo!");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        try {

            String date = arguments[1];
            double amount = Double.parseDouble(arguments[2]);
            String tag = arguments[3];
            String source = arguments[4];

            Move move = new Move(date, amount, tag, source);

            perdate.insert(Integer.parseInt(date.replace("-", "")), move);

            // Writing the tree to a FileReader
            try (Writer writer = new FileWriter(movesDATE)) {

                gson.toJson(perdate, writer);

            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        String result1 = gson.toJson(Map.of("MovesTAG", pertag));
        String result2 = gson.toJson(Map.of("MovesSOURCE", persource));
        String result3 = gson.toJson(Map.of("MovesDATE", perdate));
        String results = result1 + "\n" + result2 + "\n" + result3;
        return results;

    }
}
