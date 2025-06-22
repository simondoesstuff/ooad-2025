package ooad.project3;

import java.util.concurrent.ThreadLocalRandom;

public class Utils {
  public static <T extends Enum<T>> T getRandomEnum(Class<T> enumClass) {
    T[] types = enumClass.getEnumConstants();

    if (types.length == 0) {
        return null;
    }

    int idx = ThreadLocalRandom.current().nextInt(types.length);
    return types[idx];
  }

  private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz".toUpperCase();

  public static String makeFunnyName() {
      char[] chars = new char[4];
      ThreadLocalRandom random = ThreadLocalRandom.current();
      chars[0] = ALPHABET.charAt(random.nextInt(ALPHABET.length()));
      chars[1] = Character.toLowerCase(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
      chars[2] = ALPHABET.charAt(random.nextInt(ALPHABET.length()));
      chars[3] = Character.toLowerCase(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
      return new String(chars);
    }
}
