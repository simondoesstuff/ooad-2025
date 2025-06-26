import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import ooad.project2.model.store.CashRegister;

public class TestCashRegister {
    @Test
    void addCash() {
        var regis = new CashRegister();
        // add cash
        regis.add(20);
        assertEquals(regis.getCash(), 20);
        // negative cash?
        regis.add(-50);
        assertEquals(regis.getCash(), 20);
        // doubly adding?
        regis.add(50);
        assertEquals(regis.getCash(), 70);
        // add zero?
        regis.add(0);
        assertEquals(regis.getCash(), 70);
    }

    @Test
    void withdraw() {
        var regis = new CashRegister();
        regis.add(100);
        // withdraw
        regis.withdraw(99);
        assertEquals(regis.getCash(), 1);
        // over withdraw not allowed
        assertEquals(regis.withdraw(2), false);
        assertEquals(regis.getCash(), 1);
        // safe withdraw should be true
        assertEquals(regis.withdraw(1), true);
        assertEquals(regis.getCash(), 0);
    }
}
