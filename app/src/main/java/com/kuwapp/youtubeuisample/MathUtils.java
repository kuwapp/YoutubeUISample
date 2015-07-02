package com.kuwapp.youtubeuisample;

/**
 * Created by yusukehata on 15/07/02.
 */
public final class MathUtils {

    private MathUtils () {

    }

    public static float clamp (float value, float min, float max) {
        if (value > max) {
            return max;
        }
        if (value < min) {
            return min;
        }
        return value;
    }

}
