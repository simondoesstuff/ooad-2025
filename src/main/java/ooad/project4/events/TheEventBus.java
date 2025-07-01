package ooad.project4.events;

import lombok.Getter;
import com.google.common.eventbus.EventBus;

/**
 * Singleton that implements a form of observer pattern using guava's
 * event bus. Events are published to this bus and dispatched to listeners
 * in an entirely decoupled way. This flexibilty allows publishers and subscribers
 * to communicate without a direct link between one another. From the perspective
 * of this bus to the listeners, we have an observer pattern.
 */
public class TheEventBus {
    private static TheEventBus instance = new TheEventBus(); // INFO: (unnecessary) eager instantiation
    @Getter
    private final EventBus bus;

    private TheEventBus() {
        this.bus = new EventBus();
    }

    public static TheEventBus getInstance() {
        return instance;
    }
}
