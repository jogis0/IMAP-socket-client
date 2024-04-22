package org.example;

public class Tag {
    private static String prefix;
    private static int count;

    public Tag(String tagPrefix, int initialCount) {
        prefix = tagPrefix;
        count = initialCount;
    }

    public static String getTag() {
        return prefix + String.format("%03d", count);
    }
}
