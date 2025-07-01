package ooad.project4.model.store.tuning;
import ooad.project4.model.item.Item;

/**
 * Uses an 80% beneficial flip rate
 */
public class ManualTuning extends ProbabilisticInvertTuning {
     public ManualTuning() {
         super(.8);
     }
}
