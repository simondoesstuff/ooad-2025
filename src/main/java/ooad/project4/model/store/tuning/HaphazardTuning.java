package ooad.project4.model.store.tuning;
import ooad.project4.model.item.Item;

/**
 * Uses an 50% beneficial flip rate
 */
public class HaphazardTuning extends ProbabilisticInvertTuning {
     public HaphazardTuning() {
         super(.5);
     }
}
