package com.example.robertchung.journalapp;

/**
 * Created by Forche on 2/24/18.
 */

public class Journal {
    private int date;
    private String entry;

    public Journal(int date, String entry) {
        this.date = date;
        this.entry = entry;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public String getEntry() {
        return entry;
    }

    public void setEntry(String entry) {
        this.entry = entry;
    }
}
