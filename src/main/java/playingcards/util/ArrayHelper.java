package playingcards.util;

import java.util.Random;

public class ArrayHelper {

    public static byte[] toPrimitive(Byte[] array) {

        if (array == null) {
            return null;
        }

        else if (array.length == 0) {
            return new byte[0];
        }

        final byte[] result = new byte[array.length];

        for (int i = 0; i < array.length; i++) {
            result[i] = array[i];
        }

        return result;
    }

    public static Byte[] toObject(byte[] array) {

        if (array == null) {
            return null;
        }

        else if (array.length == 0) {
            return new Byte[0];
        }

        final Byte[] result = new Byte[array.length];

        for (int i = 0; i < array.length; i++) {
            result[i] = array[i];
        }

        return result;
    }

    public static <T> T[] clone(final T[] array) {
        if (array == null) return null;
        return array.clone();
    }

    public static void shuffle(final Object[] array) {

        Random random = new Random();

        for (int i = array.length; i > 1; i--) {
            swap(array, i - 1, random.nextInt(i), 1);
        }
    }

    private static void swap(final Object[] array,  int offset1, int offset2, int length) {

        if (array == null || array.length == 0 || offset1 >= array.length || offset2 >= array.length) {
            return;
        }

        if (offset1 < 0) offset1 = 0;
        if (offset2 < 0) offset2 = 0;

        length = Math.min(Math.min(length, array.length - offset1), array.length - offset2);

        for (int i = 0; i < length; i++, offset1++, offset2++) {
            final Object aux = array[offset1];
            array[offset1] = array[offset2];
            array[offset2] = aux;
        }
    }
}
