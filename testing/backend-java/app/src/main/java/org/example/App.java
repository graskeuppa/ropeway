package org.example;

import java.util.Scanner;

public class App {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        ComandoHandler handler = new ComandoHandler();

        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();
            String output = handler.process(input);
            System.out.println(output);
        }
        scanner.close();
    }
}
