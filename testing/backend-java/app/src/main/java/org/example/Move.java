package org.example;

public class Move {
    // Attributes
    public String date;
    public double amount;
    public String tag;
    public String source;

    // Constructor
    public Move(String date, double ammount, String tag, String source) {
        this.date = date;
        this.amount = ammount;
        this.tag = tag;
        this.source = source;
    }

    public String toString() {
        String identifier = "Move: date: " + this.date + ", amount: " + this.amount + ", tag: " + this.tag
                + ", source: " + this.source;
        return identifier;
    }

}
