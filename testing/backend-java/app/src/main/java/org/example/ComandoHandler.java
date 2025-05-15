package org.example;

import com.google.gson.Gson;
import java.util.*;

public class ComandoHandler {
    private final Gson gson = new Gson();

    public String process(String line) {

        String[] segments = line.trim().split(" ");

        // When command argument is empty
        if (segments.length == 0)
            return gson.toJson(Map.of("error", "Empty command, yo!"));

        // The first element of the array shall be the command
        String command = segments[0];

        switch (command) {
            // Calls getMovesPerDate (returns all the moves associated with a given date)
            case "GET_DATE":
                // Check correct length for command GET_DATE <yyyy-mm-dd>
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
}
