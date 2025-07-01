package ooad.project4.model.store.tuning;
import ooad.project4.model.item.Item;

/**
 * Uses an 100% beneficial flip rate
 */
public class ElectronicTuning extends ProbabilisticInvertTuning {
     public ElectronicTuning() {
         super(1);
     }
}
