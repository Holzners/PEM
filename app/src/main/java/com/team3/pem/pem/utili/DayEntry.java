package com.team3.pem.pem.utili;

import java.util.List;

/**
 * Created by Stephan on 18.06.15.
 */
public class DayEntry {
    public List<Integer> ratings;
    public String description;

    public DayEntry(List<Integer> ratings, String description){
        this.description = description;
        this.ratings = ratings;
    }
}
