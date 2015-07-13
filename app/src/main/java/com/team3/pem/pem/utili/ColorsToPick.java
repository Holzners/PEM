package com.team3.pem.pem.utili;

import com.team3.pem.pem.R;

/**
 * Helper Enum for converting Rating to color
 */
public enum ColorsToPick {


    BLUE(R.color.blue1 , R.color.blue2 ,R.color.blue3 ,R.color.blue4 ,R.color.blue5),
    CYAN(R.color.cyan1 , R.color.cyan2 ,R.color.cyan3 ,R.color.cyan4 ,R.color.cyan5),
    GREEN(R.color.green1 , R.color.green2 ,R.color.green3 ,R.color.green4 ,R.color.green5),
    YELLOW(R.color.yellow1 , R.color.yellow2 ,R.color.yellow3 ,R.color.yellow4 ,R.color.yellow5),
    RED(R.color.red1 , R.color.red2 ,R.color.red3 ,R.color.red4 ,R.color.red5),
    VIOLETTE(R.color.violette1 , R.color.violette2 ,R.color.violette3 ,R.color.violette4 ,R.color.violette5);


    private int color1, color2 , color3 , color4, color5;

    ColorsToPick(int color1, int color2, int color3, int color4, int color5){
        this.color1 = color1;
        this.color2 = color2;
        this.color3 = color3;
        this.color4 = color4;
        this.color5 = color5;
    }

    public int getColor1() {
        return color1;
    }

    public int getColor2() {
        return color2;
    }

    public int getColor3() {
        return color3;
    }

    public int getColor4() {
        return color4;
    }

    public int getColor5() {
        return color5;
    }

    public int[] getAllColors(){
        int result[] =  {color1,color2,color3,color4,color5};
        return result;
    }

    /**
     * Get Enum Field by String
     * @param color
     * @return
     */
    public static ColorsToPick getColorByString(String color) {
        if (color!= null){
            if (color.equalsIgnoreCase(GREEN.name())) return GREEN;
            else if (color.equalsIgnoreCase(RED.name())) return RED;
            else if (color.equalsIgnoreCase(BLUE.name())) return BLUE;
            else if (color.equalsIgnoreCase(YELLOW.name())) return YELLOW;
            else if (color.equalsIgnoreCase(CYAN.name())) return CYAN;
        }
        return VIOLETTE;
    }

}
