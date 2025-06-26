package ooad.project3.model.store.tuning;

import java.util.concurrent.ThreadLocalRandom;

import lombok.Getter;
import ooad.project3.model.item.Item;
import ooad.project3.model.item.music.instruments.stringed.Stringed;
import ooad.project3.model.item.music.instruments.wind.Wind;
import ooad.project3.model.item.music.players.Players;

/**
 * Inverts the tunable state of an item
 * ( Stringed: tuned, Players: equalized, Wind: adjusted )
 * based on a probability. If .9, there is a 90% chance
 * that the tuner will flip the state in a "beneficial" way,
 * such as not-tuned to tuned.
 */
public class ProbabilisticInvertTuning implements Tuner {
    @Getter
    private final double beneficialChance;

    public ProbabilisticInvertTuning(double beneficialChance) {
        this.beneficialChance = beneficialChance;
    }

    @FunctionalInterface
    private interface Get {
        boolean run();
    }

    @FunctionalInterface
    private interface Flip {
        void run();
    }

    public boolean tryTune(Item item) {
        Get get;
        Flip flip;

        switch (item) {
            case Stringed i -> {
                get = () -> i.isTuned();
                flip = () -> i.setTuned(!i.isTuned());
            }
            case Players i -> {
                get = () -> i.isEqualized();
                flip = () -> i.setEqualized(!i.isEqualized());
            }
            case Wind i -> {
                get = () -> i.isAdjusted();
                flip = () -> i.setAdjusted(!i.isAdjusted());
            }
            default -> {
                get = null;
                flip = null;
            }
        }

        if (get == null) return false;

        if (!get.run()) {
            if (ThreadLocalRandom.current().nextDouble(1) <= beneficialChance) {
                flip.run();
            }
        } else {
            if (ThreadLocalRandom.current().nextDouble(1) > beneficialChance) {
                flip.run();
                // case true -> false indicates damage
                return true;
            }
        }

        return false;
    }
}
