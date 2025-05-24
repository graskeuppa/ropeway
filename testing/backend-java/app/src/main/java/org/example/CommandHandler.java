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
    // objects to their equivalent JSON form :o. Actual godsend
    private final Gson gson = new Gson();

    public String process(String line) {
        // The string is segmented into substrings, each an argument for the
        // CommandHandler
        String[] segments = line.trim().split(" ");
        // When command argument is empty
        if (segments.length == 0)
            return gson.toJson(Map.of("error", "Empty command, yo!"));
        // The first element of the array shall be the command
        String command = segments[0];

        switch (command) {

            case "/splash":

                String version = "(0.1.0-alpha.1)";
                String splash = "    ________   ____  ___ _      ______  __  __\n" +
                        "   / ___/ __ \\/ __ \\/ _ \\ | /| / / __ `/ / / /\n" +
                        "  / /  / /_/ / /_/ /  __/ |/ |/ / /_/ / /_/ / \n" +
                        " /_/   \\____/ .___/\\___/|__/|__/\\__,_/\\__, /  \n" +
                        "           /_/                       /____/" + version + "\n" +

                        "Welcome to ropeway!\n" +

                        "If you´re unsure of what to do, run '/help' to see a list of all available commands.\n";
                return splash;
            case "/help":
                // Returns a list of all commands along with a simple description for each of
                // them
                String help = "-----------------------\n\n"
                        + "ropeway is a financial micro-management application designed to help keep a comprehensible record of all things money.\n\n"
                        +
                        "-----------------------\n\n" +
                        "About Moves:\n" +
                        "Each transaction is refered to as 'Move' within ropeway, they consist of:\n" +
                        // "- Date: You are free to choose the format of the date. I recommend using
                        // <yyyy-mm-dd>.\n"
                        // +

                        "- Date: Make sure that the date is in the <yyyy-mm-dd> format. (at least for now, I'll handle different formats later)\n"
                        +
                        "- Amount: A floating point number that can either be positive or negative\n" +
                        "- Tag: The tag is meant to contain the nature of the Move. Although it's recommend to keep tags simple, they can be of any size. \n"
                        +
                        "By default, ropeway has special handling for tags of length 3 (for example, 'MEL' which abbreviates 'meal', or 'TPT', which abbreviates 'transport').\n"
                        +
                        "- Source: Where is the money coming from. By default, ropeway has special handling for source of length 3 or 5, but there is nothing stoping you from typing more or less than that.\n"
                        +
                        "It's really important to be consistent with date, tag and source formating, not doing so will render moves incompatible for fetching operations.\n\n"
                        +
                        "-----------------------\n\n" +

                        "The current version of ropeway is very barebones and supports only creation and fetching operations, though more interesting functions are planned for future releases :). \n\n"
                        + "To interact with ropeway, type any of the following commands in the format '/command'\n\n"
                        +
                        "List of commands: \n" +

                        "- /splash: Shows the first screen you see when launching ropeway.\n" +
                        "- /help: Brings you to this screen!.\n" +
                        "- /makemove <date> <amount> <tag> <source>: Makes a Move.\n" +
                        "- /getmovesperdate <date>: Returns all moves associated with the given date.\n" +
                        "- /getmovesbetween <date1> <date2>: Returns all moves contained within two dates.\n" +
                        "- /getmovespertag <tag>: Returns all moves associated with the given tag.\n" +
                        "- /getmovespersource <source>: Returns all moves associated with the given source.\n" +
                        "- /getmptbtw <tag> <date1> <date2>: Returns all moves associated with the given tag between the two given dates.\n"
                        +
                        "- /graph <date1> <date2>: Shows a simple bar of each move within the specified period. date1 must be less or equal to date2.\n"
                        +

                        "- /getmpsbtw <source> <date1> <date2>: Returns all moves associated with the given source between the two given dates.\n"
                        +
                        "- /getmptin <tag> <date>: Returns all moves associated with the given tag in the given date.\n"
                        +
                        "- /getmpsin <source> <date>: Returns all moves associated with the given source in the given date.\n";
                return help;

            // --------------------------------------------------------------------------------------------
            // Creates a new instance of Move, adds it to an ArrayList
            case "/makemove_al":
                // MAKE_MOVE_AL <date> <amount> <tag> <source>
                if (segments.length < 5)
                    return gson.toJson(Map.of("Command err",
                            "Not enough arguments, yo! - Expected: MAKE_MOVE_AL -date- -amount- -tag- -source-"));
                try {
                    return makeMoveAL(segments);
                } catch (Exception e) {
                    return gson.toJson(Map.of("Command err", "Mismatched types, yo!"));
                }

                // --------------------------------------------------------------------------------------------

                // --------------------------------------------------------------------------------------------
                // Creates a new move, adds it to three separate data structures for ease of
                // searching. These files will later be used for combined searches.
            case "/makemove":
                // MAKE_MOVE <date> <amount> <tag> <source>
                if (segments.length < 5)
                    return gson.toJson(Map.of("Command err",
                            "Not enough arguments, yo! - Expected: MAKE_MOVE -date- -amount- -tag- -source-"));
                return makeMove(segments);
            // --------------------------------------------------------------------------------------------

            // --------------------------------------------------------------------------------------------
            // Calls getMovesPerDate (returns all the moves associated with the given date)
            case "/getmovesperdate":
                // GET_DATE <yyyy-mm-dd> or whatever format is placed in, so long as it is
                // consistent and uses any non numerical symbol to separate the date numbers.
                if (segments.length < 2)
                    return gson.toJson(Map.of("Command err", "No date, yo! - Expected: GET_MOVES_PER_DATE -date-"));
                return getMovesPerDate(segments[1]);
            // --------------------------------------------------------------------------------------------

            // --------------------------------------------------------------------------------------------
            // Calls getMovesPerTag (returns all moves associated with the given tag)
            case "/getmovespertag":
                // GET_MOVES_PER_TAG <tag>
                try {
                    if (segments.length < 2)
                        return gson.toJson(Map.of("Command err", "No tag, yo! - Expected: GET_MOVES_PER_TAG -tag-"));
                    return getMovesPerTag(segments[1]);

                } catch (Exception e) {
                    return gson.toJson(Map.of("No moves err", "There are no moves with that tag yet, yo!"));
                }
                // --------------------------------------------------------------------------------------------

                // --------------------------------------------------------------------------------------------
                // Calls getMovesPerSource, returns all moves associated with the given source)
            case "/getmovespersource":
                // GET_MOVES_PER_SOURCE <source>
                try {
                    if (segments.length < 2)
                        return gson
                                .toJson(Map.of("Command err",
                                        "No source, yo! - Expected: GET_MOVES_PER_SOURCE -source-"));
                    return getMovesPerSource(segments[1]);

                } catch (Exception e) {
                    return gson.toJson(Map.of("No moves err", "There are no moves with that source yet, yo!"));
                }
                // --------------------------------------------------------------------------------------------

                // --------------------------------------------------------------------------------------------
                // Calls getMovesBetween (returns a list of Pairs, the first element of each
                // pair is the date and the second the list of moves associated with it)
            case "/getmovesbetween":
                // GET_MOVES_BETWEEN <date1> <date2>
                if (segments.length < 3)
                    return gson.toJson(Map.of("Command err",
                            "Incomplete date range, yo! - Expected: GET_MOVES_BETWEEN -date1- -date2-"));
                return getMovesBetween(segments[1], segments[2]);
            // --------------------------------------------------------------------------------------------

            // --------------------------------------------------------------------------------------------
            // Calls getMovesBetween (returns a list of Pairs, the first element of each
            // pair is the date and the second the list of moves associated with it)
            case "/graph":
                // GRAPH <date1> <date2>
                if (segments.length < 3)
                    return gson.toJson(Map.of("Command err",
                            "Incomplete date range, yo! - Expected: GRAPH -date1- -date2- (date1<=date2)"));
                return graph(segments[1], segments[2]);
            // --------------------------------------------------------------------------------------------

            // --------------------------------------------------------------------------------------------
            // Calls getMovesPerTagBetween (returns all moves associated with the tag within
            // the given dates)
            case "/getmptbtw":
                // GET_MOVES_PER_TAG_BETWEEN <tag> <date1> <date2>
                try {
                    if (segments.length < 4)
                        return gson.toJson(Map.of("Command err",
                                "Missing arguments, yo! - Expected: GET_MOVES_PER_TAG_BETWEEN -tag- -date1- -date2-"));
                    return getMovesPerTagBetween(segments[1], segments[2], segments[3]);

                } catch (Exception e) {
                    return gson.toJson(
                            Map.of("No moves err", "There are no moves with that tag within the specified range, yo!"));
                }
                // --------------------------------------------------------------------------------------------

                // --------------------------------------------------------------------------------------------
                // Calls getMovesPerTagBetween (returns all moves associated with the tag within
                // the given dates)
            case "/getmpsbtw":
                // GET_MOVES_PER_SOURCE_BETWEEN <source> <date1> <date2>
                try {
                    if (segments.length < 4)
                        return gson.toJson(Map.of("Command err",
                                "Missing arguments, yo! - Expected: GET_MOVES_PER_SOURCE_BETWEEN -source- -date1- -date2-"));
                    return getMovesPerSourceBetween(segments[1], segments[2], segments[3]);

                } catch (Exception e) {
                    return gson.toJson(
                            Map.of("No moves err", "There are no moves with that tag within the specified range, yo!"));
                }
                // --------------------------------------------------------------------------------------------

                // --------------------------------------------------------------------------------------------
                // Calls getMovesPerTagIn (returns all moves made in the given date)
            case "/getmptin":
                // GET_MOVES_PER_TAG_IN <tag> <date>
                if (segments.length < 3)
                    return gson.toJson(Map.of("Command err",
                            "Missing arguments, yo! - Expected: GET_MOVES_PER_TAG_IN -tag- -date-"));

                return getMovesPerTagIn(segments[1], segments[2]);
            // --------------------------------------------------------------------------------------------

            // --------------------------------------------------------------------------------------------
            // Calls getMovesPerSourceIn (returns all moves made in the given date)
            case "/getmpsin":
                // GET_MOVES_PER_SOURCE_IN <source> <date>
                if (segments.length < 3)
                    return gson.toJson(Map.of("Command err",
                            "Missing arguments, yo! - Expected: GET_MOVES_PER_SOURCE_IN -source- -date-"));

                return getMovesPerSourceIn(segments[1], segments[2]);
            // --------------------------------------------------------------------------------------------

            // MORE COMMANDS HERE!
            // case nuke:

            default: // Given command does not exist
                return gson.toJson(Map.of("Wrong command err", "No such command, yo!"));

        }
    }

    // Lists that are accessed in many methods, instanciated here for ease of
    // access.
    ArrayList<Pir<Integer, ArrayList<Move>>> gmb;
    ArrayList<Move> gmpd;
    ArrayList<Move> gmpt;
    ArrayList<Move> gmps;
    public boolean found;
    public int totalin, totalout, nettotal;

    private String getMovesPerTagIn(String tag, String date) {

        ArrayList<Move> temp = new ArrayList<>();

        // Populates the nondeclared list with the moves made per date, meaning gmpd is
        // full of elements rn
        getMovesPerDateVOID(date);

        for (Move move : gmpd) {
            if (tag.equals(move.tag)) {
                temp.add(move);

            }
        }
        moneyMovement(temp);
        return gson.toJson(Map.of("NET TOTAL = " + nettotal, "INCOME = " + totalin + ", OUTCOME = " + totalout,
                "Moves with the " + tag + " tag made on " + date + ":", temp));

    }

    private String getMovesPerSourceIn(String source, String date) {

        ArrayList<Move> temp = new ArrayList<>();

        getMovesPerDateVOID(date);
        for (Move move : gmpd) {
            if (source.equals(move.source)) {
                temp.add(move);
            }
        }
        moneyMovement(temp);
        return gson.toJson(Map.of("NET TOTAL = " + nettotal, "INCOME = " + totalin + ", OUTCOME = " + totalout,
                "Moves with the " + source + " source made on " + date
                        + ":",
                temp));

    }

    private void moneyMovement(ArrayList<Move> moves) {
        totalin = 0;
        totalout = 0;
        nettotal = 0;

        for (Move move : moves) {

            if (move.amount < 0) {
                totalout += move.amount;
            } else {
                totalin += move.amount;
            }

            nettotal += move.amount;

        }

    }

    private String getMovesPerSourceBetween(String source, String d1, String d2) {
        // 1. We get the moves made bewteen d1 and d2.
        // 1.5 The method getBetween from the trees gives back an ArrayList of Pairs
        // 1.6 Each pir has as its second generic caracterization an ArrayList<Move>
        // 1.7 Pir has implemented a method for returning just this list.
        // 1.8 I'll make a new ArralyList<Move> and for each ArrayList of the pir, I'll
        // add everything to the new, flat ArrayList.
        // 2. For each element in that list, we compare their source to the one that is
        // given to the functions
        // 3. We add only the moves that share that source!
        // // Voilá!

        // This populates gmb
        getMovesBetweenVOID(d1, d2);

        if (found == true) {
            // It is currently ArrayList<Pir<Integer, ArrayList<Move>>>

            // This "flattens" the list
            ArrayList<Move> flat = flattenPirList(gmb);

            ArrayList<Move> temp2 = new ArrayList<>();
            for (Move move : flat) {
                if (source.equals(move.source)) {
                    temp2.add(move);
                }
            }

            moneyMovement(temp2);
            return gson.toJson(
                    Map.of("NET TOTAL = " + nettotal, "INCOME = " + totalin + ", OUTCOME = " + totalout,
                            "Moves with the source " + source + " made between " + d1 + " and " + d2 + ":", temp2));
        } else {
            return gson.toJson(Map.of("No moves err", "There are no moves, yo!"));
        }

    }

    public ArrayList<Move> flattenPirList(ArrayList<Pir<Integer, ArrayList<Move>>> pirlist) {
        ArrayList<Move> flattened = new ArrayList<>();

        for (Pir<Integer, ArrayList<Move>> pir : pirlist) {
            flattened.addAll(pir.returnList());
        }
        return flattened;
    }

    private String getMovesPerTagBetween(String tag, String d1, String d2) {
        getMovesBetweenVOID(d1, d2);

        if (found == true) {

            ArrayList<Move> flat = flattenPirList(gmb);

            ArrayList<Move> temp2 = new ArrayList<>();
            for (Move move : flat) {
                if (tag.equals(move.tag)) {
                    temp2.add(move);
                }
            }

            moneyMovement(temp2);
            return gson.toJson(
                    Map.of("NET TOTAL = " + nettotal, "INCOME = " + totalin + ", OUTCOME = " + totalout,
                            "Moves with the tag " + tag + " made between " + d1 + " and " + d2 + ":", temp2));

        } else {
            return gson.toJson(Map.of("No moves err", "There are no moves, yo!"));
        }
    }

    private String getMovesBetween(String d1, String d2) {
        getMovesBetweenVOID(d1, d2);

        if (found == true) {
            ArrayList<Move> flat = flattenPirList(gmb);
            moneyMovement(flat);
            return gson.toJson(Map.of("NET TOTAL = " + nettotal, "INCOME = " + totalin + ", OUTCOME = " + totalout,
                    "Moves made between " + d1 + " and " + d2 + ":", flat));
        } else {
            return gson.toJson(Map.of("No moves err", "There are no moves, yo!"));
        }

    }

    private String graph(String d1, String d2) {
        getMovesBetweenVOID(d1, d2);

        if (found == true) {
            ArrayList<Move> flat = flattenPirList(gmb);
            moneyMovement(flat);
            return gson.toJson(flat);
        } else {
            return gson.toJson(Map.of("No moves err", "There are no moves, yo!"));
        }

    }

    private void getMovesBetweenVOID(String d1, String d2) {
        File moves = new File("./JSON/movesDATE.json");
        moves.getParentFile().mkdirs();
        AVL<Move> tree;

        int date1, date2;

        try {
            date1 = Integer.parseInt(d1.trim().replaceAll("-", ""));
            date2 = Integer.parseInt(d2.trim().replaceAll("-", ""));
        } catch (Exception e) {
            System.err.println(gson.toJson(Map.of("Date err", "Couldn't convert date String to Integer, yo!")));
            return;
        }

        if (moves.exists()) {
            found = true;
            try (Reader reader = new FileReader(moves)) {
                tree = gson.fromJson(reader, new TypeToken<AVL<Move>>() {
                }.getType());

                if (tree == null)
                    tree = new AVL<>();

            } catch (IOException e) {
                e.printStackTrace();
                tree = new AVL<>();
            }

            // Regresa una lista de Pir
            gmb = tree.getBetween(date1, date2);

        } else {
            found = false;
        }

    }

    private String getMovesPerDate(String date) {
        getMovesPerDateVOID(date);

        if (found == true) {

            moneyMovement(gmpd);
            return gson.toJson(Map.of("NET TOTAL = " + nettotal, "INCOME = " + totalin + ", OUTCOME = " + totalout,
                    "Moves made on " + date, gmpd));

        } else {
            return gson.toJson(Map.of("No moves err", "There are no moves, yo!"));
        }

    }

    private void getMovesPerDateVOID(String date) {
        File moves = new File("./JSON/movesDATE.json");
        moves.getParentFile().mkdirs();
        AVL<Move> tree;

        if (moves.exists()) {
            found = true;
            try (Reader reader = new FileReader(moves)) {
                tree = gson.fromJson(reader, new TypeToken<AVL<Move>>() {
                }.getType());

                if (tree == null)
                    tree = new AVL<>();

            } catch (IOException e) {
                e.printStackTrace();
                tree = new AVL<>();
            }

            gmpd = tree.search(Integer.parseInt(date.replace("-", "")));

        } else {
            found = false;
        }

    }

    private String getMovesPerTag(String tag) {
        getMovesPerTagVOID(tag);

        if (found == true) {

            moneyMovement(gmpt);
            return gson.toJson(Map.of("NET TOTAL = " + nettotal, "INCOME = " + totalin + ", OUTCOME = " + totalout,
                    "Moves with the " + tag + " tag:", gmpt));
        } else {
            return gson.toJson(Map.of("No moves err", "No moves have been made, yo!"));
        }

    }

    private void getMovesPerTagVOID(String tag) {
        File moves = new File("./JSON/movesTAG.json");
        moves.getParentFile().mkdirs();
        HTable<String, Move> htable;

        if (moves.exists()) {
            found = true;
            try (Reader reader = new FileReader(moves)) {
                htable = gson.fromJson(reader, new TypeToken<HTable<String, Move>>() {
                }.getType());

                if (htable == null)
                    htable = new HTable<>();

            } catch (IOException e) {
                e.printStackTrace();
                htable = new HTable<>();
            }

            gmpt = htable.get(tag);

        } else {
            found = false;
        }

    }

    private String getMovesPerSource(String source) {
        getMovesPerSourceVOID(source);

        if (found == true) {
            moneyMovement(gmps);
            return gson.toJson(Map.of("NET TOTAL = " + nettotal, "INCOME = " + totalin + ", OUTCOME = " + totalout,
                    "Moves with the " + source + " source:", gmps));
        } else {
            return gson.toJson(Map.of("No moves err", "No moves have been made, yo!"));
        }

    }

    private void getMovesPerSourceVOID(String source) {
        File moves = new File("./JSON/movesSOURCE.json");
        moves.getParentFile().mkdirs();
        HTable<String, Move> htable;

        if (moves.exists()) {
            found = true;
            try (Reader reader = new FileReader(moves)) {
                htable = gson.fromJson(reader, new TypeToken<HTable<String, Move>>() {
                }.getType());

                if (htable == null)
                    htable = new HTable<>();

            } catch (IOException e) {
                e.printStackTrace();
                htable = new HTable<>();
            }

            gmps = htable.get(source);

        } else {
            found = false;
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

        // Actual points of failure, the date and the amount
        try {
            Integer.parseInt(arguments[1].trim().replaceAll("-", ""));
            Double.parseDouble(arguments[2]);

        } catch (Exception e) {
            return gson.toJson(Map.of("Arguments err", "Mismatched types, yo!"));
        }

        // Routes to the files

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
        } else { // File does not exist, make a new HTable

            pertag = new HTable<>();

            try {
                if (movesTAG.createNewFile()) {
                    String nw = "Found no existing files...";
                    System.out.println(nw + "\n" + "New file created!: " + movesTAG.getName());
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
        } else

        { // File does not exist, make a new one

            persource = new HTable<>();

            try {
                if (movesSOURCE.createNewFile()) {
                    System.out.println("New file created!: " + movesSOURCE.getName());
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
                    System.out.println("New file created!: " + movesDATE.getName());
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

        return "Move saved to files!\nTo see Moves you've made, search them using any of the provided commands!\n\n (Type '/help if you're a little lost!')";

    }

}
