package com.example.railwayttable;
public class ThemeManager {
    private static Theme selectedTheme = Theme.THEME_A;

    public enum Theme {
        THEME_A,
        THEME_B,
        THEME_C
    }

    public static Theme getSelectedTheme() {
        return selectedTheme;
    }

    public static void setSelectedTheme(Theme theme) {
        selectedTheme = theme;
    }
}
