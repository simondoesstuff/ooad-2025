package ooad.project3.model.store.tuning;
import ooad.project3.model.item.Item;

/**
 * Uses an 50% beneficial flip rate
 */
public class HaphazardTuning extends ProbabilisticInvertTuning {
     public HaphazardTuning() {
         super(.5);
     }
}
