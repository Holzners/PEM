package com.team3.pem.pem.utili;

import java.util.HashMap;

/**
 * Created by Stephan on 18.06.15.
 */
public class DayEntry {
    public HashMap<String, Integer> ratings;
    public String description = "";

    public DayEntry(HashMap<String,Integer> ratings, String description){
        this.description = description;
        this.ratings = ratings;
    }
}
