package org.example;

import DS.AVL.*;
import DS.AVL.AVL.Pir;
import DS.HTable.*;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import java.io.*;
import java.util.*;

public class CommandHandler {
    // The cornerstone of this whole thing, allows for serialising and deserialising
    // objects to their equivalent JSON form
    private final Gson gson = new Gson();

    public String process(String line) {
        // The command is split by " ", each subsequent substring represents an argument
        String[] segments = line.trim().split(" ");
        // When command argument is empty
        if (segments.length == 0)
            return gson.toJson(Map.of("error", "Empty command, yo!"));
        // The first element of the array shall be the command
        String command = segments[0];

        switch (command) {

            // --------------------------------------------------------------------------------------------
            // Creates a new instance of Move, adds it to an ArrayList
            case "MAKE_MOVE_AL":
                // MAKE_MOVE_AL <date> <amount> <tag> <source>
                if (segments.length < 5)
                    return gson.toJson(Map.of("Command err",
                            "Not enough arguments, yo! - Expected: MAKE_MOVE_AL -date- -amount- -tag- -source-"));
                return makeMoveAL(segments);
            // --------------------------------------------------------------------------------------------

            // --------------------------------------------------------------------------------------------
            // Creates a new move, adds it to three separate data structures for ease of
            // searching. These files will later be used for combined searches.
            case "MAKE_MOVE":
                // MAKE_MOVE <date> <amount> <tag> <source>
                if (segments.length < 5)
                    return gson.toJson(Map.of("Command err",
                            "Not enough arguments, yo! - Expected: MAKE_MOVE -date- -amount- -tag- -source-"));
                return makeMove(segments);
            // --------------------------------------------------------------------------------------------

            // --------------------------------------------------------------------------------------------
            // Calls getMovesPerDate (returns all the moves associated with the given date)
            case "GET_MOVES_PER_DATE":
                // GET_DATE <yyyy-mm-dd> or whatever format is placed in, so long as it is
                // consistent and uses any non numerical symbol to separate the date numbers.
                if (segments.length < 2)
                    return gson.toJson(Map.of("Command err", "No date, yo! - Expected: GET_MOVES_PER_DATE -date-"));
                return getMovesPerDate(segments[1]);
            // --------------------------------------------------------------------------------------------

            // --------------------------------------------------------------------------------------------
            // Calls getMovesPerTag (returns all moves associated with the given tag)
            case "GET_MOVES_PER_TAG":
                // GET_MOVES_PER_TAG <tag>
                if (segments.length < 2)
                    return gson.toJson(Map.of("Command err", "No tag, yo! - Expected: GET_MOVES_PER_TAG -tag-"));
                return getMovesPerTag(segments[1]);

            // --------------------------------------------------------------------------------------------

            // --------------------------------------------------------------------------------------------
            // Calls getMovesPerSource, returns all moves associated with the given source)
            case "GET_MOVES_PER_SOURCE":
                // GET_MOVES_PER_SOURCE <source>
                if (segments.length < 2)
                    return gson
                            .toJson(Map.of("Command err", "No source, yo! - Expected: GET_MOVES_PER_SOURCE -source-"));
                return getMovesPerSource(segments[1]);
            // --------------------------------------------------------------------------------------------

            // --------------------------------------------------------------------------------------------
            // Calls getMovesBetween (returns a list of Pairs, the first element of each
            // pair is the date and the second the list of moves associated with it)
            case "GET_MOVES_BETWEEN":
                // GET_MOVES_BETWEEN <date1> <date2>
                if (segments.length < 3)
                    return gson.toJson(Map.of("Command err",
                            "Incomplete date range, yo! - Expected: GET_MOVES_BETWEEN -date1- -date2-"));
                return getMovesBetween(segments[1], segments[2]);

            // ----------------------------------------------|----------------------------------------------
            // MORE COMMANDS HERE!
            // case "GET_MOVES_PER_TAG_BETWEEN":
            // case "GET_MOVES_PER_SOURCE_BETWEEN":

            default: // Given command does not exist
                return gson.toJson(Map.of("Wrong command err", "No such command, yo!"));

        }
    }

    private String getMovesBetween(String d1, String d2) {
        File moves = new File("./JSON/movesDATE.json");
        moves.getParentFile().mkdirs();
        AVL<Move> tree;

        int date1 = Integer.parseInt(d1);
        int date2 = Integer.parseInt(d2);

        if (moves.exists()) {

            try (Reader reader = new FileReader(moves)) {
                tree = gson.fromJson(reader, new TypeToken<AVL<Move>>() {
                }.getType());

                if (tree == null)
                    tree = new AVL<>();

            } catch (IOException e) {
                e.printStackTrace();
                tree = new AVL<>();
            }

            ArrayList<Pir<Integer, ArrayList<Move>>> moveList = tree.getBetween(date1, date2);

            return gson.toJson(Map.of("Moves made between " + date1 + " and " + date2 + ":", moveList.toString()));

        } else {
            return gson.toJson(Map.of("No moves err", "There are no moves, yo!"));
        }
    }

    private String getMovesPerDate(String date) {
        File moves = new File("./JSON/movesDATE.json");
        moves.getParentFile().mkdirs();
        AVL<Move> tree;

        if (moves.exists()) {

            try (Reader reader = new FileReader(moves)) {
                tree = gson.fromJson(reader, new TypeToken<AVL<Move>>() {
                }.getType());

                if (tree == null)
                    tree = new AVL<>();

            } catch (IOException e) {
                e.printStackTrace();
                tree = new AVL<>();
            }

            ArrayList<Move> dataList = tree.search(Integer.parseInt(date.replace("-", " ")));
            return gson.toJson(Map.of("Moves made on" + date, dataList.toString()));

        } else {
            return gson.toJson(Map.of("No moves err", "There are no moves, yo!"));
        }
    }

    private String getMovesPerTag(String tag) {
        File moves = new File("./JSON/movesTAG.json");
        moves.getParentFile().mkdirs();
        HTable<String, Move> htable;

        if (moves.exists()) {

            try (Reader reader = new FileReader(moves)) {
                htable = gson.fromJson(reader, new TypeToken<HTable<String, Move>>() {
                }.getType());

                if (htable == null)
                    htable = new HTable<>();

            } catch (IOException e) {
                e.printStackTrace();
                htable = new HTable<>();
            }

            ArrayList<Move> taglist = htable.get(tag);

            return gson.toJson(Map.of("Moves with the \"" + tag + "\" tag:", taglist.toString()));

        } else {
            return gson.toJson(Map.of("No moves err", "No moves have been made, yo!"));
        }

    }

    private String getMovesPerSource(String source) {
        File moves = new File("./JSON/movesSOURCE.json");
        moves.getParentFile().mkdirs();
        HTable<String, Move> htable;

        if (moves.exists()) {

            try (Reader reader = new FileReader(moves)) {
                htable = gson.fromJson(reader, new TypeToken<HTable<String, Move>>() {
                }.getType());

                if (htable == null)
                    htable = new HTable<>();

            } catch (IOException e) {
                e.printStackTrace();
                htable = new HTable<>();
            }

            ArrayList<Move> sourcelist = htable.get(source);

            return gson.toJson(Map.of("Moves with the \"" + source + "\" source:", sourcelist.toString()));

        } else {
            return gson.toJson(Map.of("No moves err", "No moves have been made, yo!"));
        }

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
