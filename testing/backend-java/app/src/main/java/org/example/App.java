package org.example;

import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CommandHandler handler = new CommandHandler();
        // System.out.println("Just checkin");
        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();
            String output = handler.process(input);
            System.out.println(output);
        }
        scanner.close();
        // System.out.println("Everything worked yo");
    }
}
