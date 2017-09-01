package ch.cloudcoins;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public static boolean containsOnlyDigits(String str) {
        return !isNullOrEmpty(str) && str.matches("[0-9]+");
    }

    public static String normalizeSpecialMicrosoftOfficeCharacters(String str) {
        str = str.replace('\u00ab', '"'); // «
        str = str.replace('\u00bb', '"'); // »
        str = str.replace('\u201c', '"'); // “
        str = str.replace('\u201d', '"'); // ”
        str = str.replace('\u201e', '"'); // „

        str = str.replace('\u2039', '\''); // ‹
        str = str.replace('\u203A', '\''); // ›
        str = str.replace('\u0060', '\''); // `
        str = str.replace('\u2018', '\''); // ‘
        str = str.replace('\u201a', '\''); // ‚
        str = str.replace('\u00b4', '\''); // ´
        str = str.replace('\u2019', '\''); // ’
        str = str.replace('\u201b', '\''); // ‛

        str = str.replace('\u2010', '-'); // ‐
        str = str.replace('\u2011', '-'); // ‑
        str = str.replace('\u2012', '-'); // ‒
        str = str.replace('\u2013', '-'); // –
        str = str.replace('\u2014', '-'); // —
        str = str.replace('\u2015', '-'); // ―

        return str;
    }

    public static boolean isNullOrEmptyTrimify(String string) {
        return isNullOrEmpty(trimNullSafe(string));
    }

    public static String trimNullSafe(String string) {
        if (string != null) {
            return string.trim();
        }
        return null;
    }

    public static boolean patternMatches(Pattern pattern, String str) {
        return pattern.matcher(str).find();
    }

    public static boolean anyPatternMatches(List<Pattern> patterns, String str) {
        return patterns.stream().anyMatch(p -> patternMatches(p, str));
    }

    public static String extractFirstGroup(Pattern pattern, String str) {
        return extractGroup(pattern, str, 1);
    }

    public static String extractGroup(Pattern pattern, String str, int group) {
        Matcher m = pattern.matcher(str);
        if (m.find()) {
            return m.group(group);
        } else {
            throw new IllegalStateException("Pattern did not match.");
        }
    }

    public static boolean equalIgnoreCase(String a, String b) {
        if (a == null && b == null || a == b) {
            return true;
        }
        return a != null && b != null && a.equalsIgnoreCase(b);
    }
}
