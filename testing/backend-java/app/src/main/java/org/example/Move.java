package org.example;

public class Move {
    // Attributes
    public String date;
    public double amount;
    public String tag;
    public String source;

    // Constructor
    public Move(String date, double amount, String tag, String source) {
        this.date = date;
        this.amount = amount;
        this.tag = tag;
        this.source = source;
    }

    public String toString() {
        String identifier = "Move: date: " + this.date + ", amount: " + this.amount + ", tag: " + this.tag
                + ", source: " + this.source;

        String id = "Move: \n" +
                "- Date:" + this.date + "\n" +
                "- Amount:" + this.amount + "\n" +
                "- Tag:" + this.tag + "\n" +
                "- Source:" + this.source + "\n";
        return identifier;
    }

}
