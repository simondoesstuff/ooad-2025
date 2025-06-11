package ooad.project2;

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
}
