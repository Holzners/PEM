package com.team3.pem.pem.utili;

import com.team3.pem.pem.R;

/**
 * Created by Stephan on 25.06.15.
 */
public enum ColorsToPick {

    GREEN(R.color.green1 , R.color.green2 ,R.color.green3 ,R.color.green4 ,R.color.green5),
    RED(R.color.red1 , R.color.red2 ,R.color.red3 ,R.color.red4 ,R.color.red5),
    BLUE(R.color.blue1 , R.color.blue2 ,R.color.blue3 ,R.color.blue4 ,R.color.blue5),
    YELLOW(R.color.yellow1 , R.color.yellow2 ,R.color.yellow3 ,R.color.yellow4 ,R.color.yellow5),
    VIOLETTE(R.color.violette1 , R.color.violette2 ,R.color.violette3 ,R.color.violette4 ,R.color.violette5),
    BROWN(R.color.brown1 , R.color.brown2 ,R.color.brown3 ,R.color.brown4 ,R.color.brown5);

    private int color1, color2 , color3 , color4, color5;

    private ColorsToPick(int color1, int color2, int color3, int color4, int color5){
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

    public static ColorsToPick getColorByString(String color) {
        if (color.equalsIgnoreCase(GREEN.name())) return GREEN;
        else if (color.equalsIgnoreCase(RED.name())) return RED;
        else if (color.equalsIgnoreCase(BLUE.name())) return BLUE;
        else if (color.equalsIgnoreCase(YELLOW.name())) return YELLOW;
        else if (color.equalsIgnoreCase(BROWN.name())) return BROWN;
        return VIOLETTE;
    }

}
