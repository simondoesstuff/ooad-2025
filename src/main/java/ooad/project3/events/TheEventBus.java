package ooad.project3.events;

import lombok.Getter;
import com.google.common.eventbus.EventBus;

public class TheEventBus {
    private static TheEventBus instance;
    @Getter
    private EventBus bus;

    private TheEventBus() {}

    public static TheEventBus getInstance() {
        if (instance == null) {
            instance = new TheEventBus();
        }

        return instance;
    }
}
