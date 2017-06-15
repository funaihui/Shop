package com.wizardev.shop;

import android.app.Activity;
import android.os.Build;
import android.view.WindowManager;

/**
 * Created by xiaohui on 2016/10/21.
 */

public class Statebar {
    public static void setStatuebarColor(Activity activity,int statusBarColor) {
        /**
         * 设置状态栏颜色
         */
        if (Build.VERSION.SDK_INT >= 19) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                activity.getWindow().setStatusBarColor(calculateStatusColor(statusBarColor, 100));
            }
        }
    }
    /**
     *
     * @param color 设置状态栏颜色
     * @param alpha 设置状态栏透明度
     * @return 0xffrrggbb rrggbb是设置后的透明度与颜色通过运算后得到的最终的颜色
     */
    private static int calculateStatusColor(int color, int alpha) {
        float a = 1 - alpha / 255f;
        int red = color >> 16 & 0xff;
        int green = color >> 8 & 0xff;
        int blue = color & 0xff;
        red = (int) (red * a + 0.5);
        green = (int) (green * a + 0.5);
        blue = (int) (blue * a + 0.5);
        return 0xff << 24 | red << 16 | green << 8 | blue;
    }
}

