package org.example;

public class Move {
    // Attributes
    public String date;
    public double ammount;
    public String tag;
    public String source;

    // Constructor
    public Move(String date, double ammount, String tag, String source) {
        this.date = date;
        this.ammount = ammount;
        this.tag = tag;
        this.source = source;
    }
}
