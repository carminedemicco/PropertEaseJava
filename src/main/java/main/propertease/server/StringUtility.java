package main.propertease.server;

/**
 * A utility class for string manipulation.
 */
public class StringUtility {
    /**
     * Escapes a string.
     *
     * @param str The string to escape.
     * @return The escaped string.
     */
    public static String escape(String str) {
        final var result = new StringBuilder();
        for (final var c : str.toCharArray()) {
            switch (c) {
                case '\n':
                    result.append("\\n");
                    break;
                case '\r':
                    result.append("\\r");
                    break;
                case '\t':
                    result.append("\\t");
                    break;
                case '\b':
                    result.append("\\b");
                    break;
                case '\f':
                    result.append("\\f");
                    break;
                case '\\':
                    result.append("\\\\");
                    break;
                case '\'':
                    result.append("\\'");
                    break;
                case '"':
                    result.append("\\\"");
                    break;
                default:
                    result.append(c);
                    break;
            }
        }
        return result.toString();
    }
}
