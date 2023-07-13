package com.example.railwayttable;

import android.content.Context;

import androidx.core.content.ContextCompat;

public class ColorManager {
    public enum Theme {
        THEME_A,
        THEME_B,
        THEME_C
    }

    public static int getToolbarColor(Context context, Theme theme) {
        switch (theme) {
            case THEME_A:
                return ContextCompat.getColor(context, R.color.blueb);
            case THEME_B:
                return ContextCompat.getColor(context, R.color.green);
            case THEME_C:
                return ContextCompat.getColor(context, R.color.purple);
            default:
                return ContextCompat.getColor(context, R.color.blueb);
        }
    }


    public static int getHeaderLayout(Theme theme) {
        switch (theme) {
            case THEME_B:
                return R.layout.header_menu_2;
            case THEME_C:
                return R.layout.header_menu_1;
            default:
                return R.layout.header_menu;
        }
    }

    public static int getCardViewColor(Context context, Theme theme, int cardViewIndex) {
        switch (theme) {
            case THEME_B:
                return getCardViewColorThemeB(context, cardViewIndex);
            case THEME_C:
                return getCardViewColorThemeC(context, cardViewIndex);
            default:
                return getCardViewColorThemeA(context, cardViewIndex);
        }
    }

    private static int getCardViewColorThemeA(Context context, int cardViewIndex) {
        switch (cardViewIndex) {
            case 1:
                return ContextCompat.getColor(context, R.color.blueb);
            case 2:
                return ContextCompat.getColor(context, R.color.light_blue1);
            case 3:
                return ContextCompat.getColor(context, R.color.almost_white);
            case 4:
                return ContextCompat.getColor(context, R.color.light_orange);
            default:
                return ContextCompat.getColor(context, R.color.blueb);
        }
    }


    private static int getCardViewColorThemeB(Context context, int cardViewIndex) {
        switch (cardViewIndex) {
            case 1:
                return ContextCompat.getColor(context, R.color.sunny_orange);
            case 2:
                return ContextCompat.getColor(context, R.color.light_green);
            case 3:
                return ContextCompat.getColor(context, R.color.green);
            case 4:
                return ContextCompat.getColor(context, R.color.light_orange);
            default:
                return ContextCompat.getColor(context, R.color.blueb);
        }
    }
    private static int getCardViewColorThemeC(Context context, int cardViewIndex) {
        switch (cardViewIndex) {
            case 1:
                return ContextCompat.getColor(context, R.color.rose);
            case 2:
                return ContextCompat.getColor(context, R.color.fuchsia);
            case 3:
                return ContextCompat.getColor(context, R.color.purple);
            case 4:
                return ContextCompat.getColor(context, R.color.sea);
            default:
                return ContextCompat.getColor(context, R.color.rose);
        }
    }


}
