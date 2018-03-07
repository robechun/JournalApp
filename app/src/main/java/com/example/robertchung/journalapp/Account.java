package com.example.robertchung.journalapp;

/**
 * Created by robertchung on 2/24/18.
 */

public class Account {
    private String email;
    private String name;
    boolean fromFacebook;

    // TODO: Do I really need these fields?
    // private String/dateTime dates [];
    // private double/int/w/e notificationPref;

    public String getEmail() { return email; }
    public String getName() { return name; }

    public Account() {
        email = "";
        name = "";
    }

    public Account(String em, boolean fromFb) {
        if (!fromFb){
            email = em;
        } else {
            email = "Facebook";
        }
        name = "Robert"; // Default for now, TODO
        fromFacebook = fromFb;
    }


}
