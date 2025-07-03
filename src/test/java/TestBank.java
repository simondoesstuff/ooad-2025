import org.junit.jupiter.api.Test;

import ooad.project2.model.Bank;

import static org.junit.jupiter.api.Assertions.*;

public class TestBank {
    @Test
    void verifySingleton() {
        var bank = Bank.getInstance();
        // singleton
        assertEquals(bank, Bank.getInstance());
    }

    @Test
    void testWithdraws() {
        var bank = Bank.getInstance();
        assertEquals(bank.getTotalWithdrawn(), 0);
        bank.withdraw(100);
        assertEquals(bank.getTotalWithdrawn(), 100);
        bank.withdraw(200);
        assertEquals(bank.getTotalWithdrawn(), 300);
        // negative?
        bank.withdraw(-20);
        assertEquals(bank.getTotalWithdrawn(), 300);
    }
}
