package com.kuwapp.youtubeuisample;

import android.app.Activity;
import android.graphics.Point;
import android.view.WindowManager;

/**
 * Created by yusukehata on 15/07/02.
 */
public final class SysUtils {

    private SysUtils () {

    }

    public static Point getWindowSize (Activity activity) {
        WindowManager wm = activity.getWindowManager();
        Point outSize = new Point();
        wm.getDefaultDisplay().getSize(outSize);
        return outSize;
    }
}
