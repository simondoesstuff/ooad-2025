package ooad.project1a;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Creator {
    private final double[] utilRandoms;
    private final double[] mathRandoms;
    private final double[] threadLocalRandoms;

    public Creator(int amnt) {
        this.utilRandoms = new double[amnt];
        this.mathRandoms = new double[amnt];
        this.threadLocalRandoms = new double[amnt];
        generateRandoms(amnt);
    }

    private void generateRandoms(int amnt) {
        Random utilRandom = new Random();

        for (int i = 0; i < amnt; i++) {
            utilRandoms[i] = utilRandom.nextDouble();
            mathRandoms[i] = Math.random();
            threadLocalRandoms[i] = ThreadLocalRandom.current().nextDouble();
        }
    }

    public double[] getUtilRandoms() {
        return utilRandoms;
    }

    public double[] getMathRandoms() {
        return mathRandoms;
    }

    public double[] getThreadLocalRandoms() {
        return threadLocalRandoms;
    }
}
