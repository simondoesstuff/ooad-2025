package ooad.project3.model.store.tuning;
import ooad.project3.model.item.Item;

/**
 * Top level interface for a strategy pattern implemented
 * within Clerk
 */
public interface Tuner {
     /**
      * @returns true if the item should be "damaged" due
      * to tuning
      */
     public boolean tryTune(Item item);
}
