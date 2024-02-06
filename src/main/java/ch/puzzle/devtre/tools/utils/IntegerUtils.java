package ch.puzzle.devtre.tools.utils;

public class IntegerUtils {

    public static byte getByte(int value, int byteNr) {
        if (byteNr < 0 || byteNr > 3) {
            throw new IllegalArgumentException("byteNr needs to be in interval [0, 3]");
        }

        return (byte) ((value >> (byteNr * 8)) & 255);
    }
}
