package com.ramz.parallelscroll.util;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by ramesh on 1/8/18.
 *
 * @auther Ramesh M Nair
 */
public class ScreenUtil {

    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }
}
