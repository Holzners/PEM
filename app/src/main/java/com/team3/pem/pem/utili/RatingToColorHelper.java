package com.team3.pem.pem.utili;

import com.team3.pem.pem.R;
import com.team3.pem.pem.mSQLite.FeedReaderDBHelper;

import java.util.HashMap;

/**
 * Created by Stephan on 25.06.15.
 */
public class RatingToColorHelper {

    public static int ratingToColor(String factor, int rating) {
        FeedReaderDBHelper mdHelper = FeedReaderDBHelper.getInstance();

        HashMap<String, String> factorWidthColor = mdHelper.getFactorsFromDatabase();
        if(factor == null || rating == 0) return R.color.white;
        String s = factorWidthColor.get(factor);
        ColorsToPick c = ColorsToPick.getColorByString(s);
        switch (rating) {
            case 1:
                return c.getColor1();
            case 2:
                return c.getColor2();
            case 3:
                return c.getColor3();
            case 4:
                return c.getColor4();
            case 5:
                return c.getColor5();
            default:
                return R.color.transparent;
        }
    }

}
