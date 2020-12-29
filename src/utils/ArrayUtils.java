package utils;

public class ArrayUtils {

    public static boolean contains(final int[] array, final int v) {
        boolean result = false;

        for (int i : array) {
            if (i == v) {
                result = true;
                break;
            }
        }

        return result;
    }

    public static boolean contains(final int[] array, final int v, final int upperBound) {
        boolean result = false;

        for (int i = 0; i < upperBound; i++) {
            if (array[i] == v) {
                result = true;
                break;
            }
        }

        return result;
    }

}
