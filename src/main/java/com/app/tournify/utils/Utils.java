package com.app.tournify.utils;

public class Utils {
    public static String getExtension(String filename) {
        var split = filename.split("\\.");
        return split[split.length - 1];
    }
}
