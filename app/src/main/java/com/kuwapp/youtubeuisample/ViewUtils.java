package com.kuwapp.youtubeuisample;

import android.view.View;

/**
 * Created by yusukehata on 15/07/02.
 */
public final class ViewUtils {

    private ViewUtils () {

    }

    public static float getScreenX (View v) {
        int[] loc = new int[2];
        v.getLocationOnScreen(loc);
        return loc[0];
    }

    public static float getScreenY (View v) {
        int[] loc = new int[2];
        v.getLocationOnScreen(loc);
        return loc[1];
    }

    public static void setScale (View v, float scale) {
        v.setScaleX(scale);
        v.setScaleY(scale);
    }

}
