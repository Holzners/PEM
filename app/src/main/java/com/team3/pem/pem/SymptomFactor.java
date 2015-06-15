package com.team3.pem.pem;

import android.graphics.Color;

/**
 * Speichert die eingegebenen Symptome bzw. Faktoren.
 * Mit SQL vermutlich anders.
 */
public class SymptomFactor {

    public String name;
    public Color color;

    public SymptomFactor(String name, Color color){
        this.name = name;
        this.color = color;
    }
}
