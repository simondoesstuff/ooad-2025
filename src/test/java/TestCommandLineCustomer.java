import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Scanner;
import java.io.ByteArrayInputStream;

import ooad.project4.model.item.Condition;
import ooad.project4.model.item.music.instruments.stringed.Guitar;
import ooad.project4.model.customers.CommandLineCustomer;
import ooad.project4.model.item.BuildableItem;
import ooad.project4.model.item.Item;
import ooad.project4.model.item.music.instruments.wind.Flute;
import ooad.project4.model.item.music.instruments.stringed.Guitar;

import org.junit.jupiter.api.Assertions.*;

class TestCommandLineCustomer {

    private CommandLineCustomer createCustomerWithInput(String input) {
        ByteArrayInputStream testIn = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(testIn);
        return new CommandLineCustomer(scanner);
    }

    // --- getPurchaseInterest ---

    @Test
    void test_getPurchaseInterest_selectsValidItem() {
        // Simulate user entering "1"
        var customer = createCustomerWithInput("1\n");
        List<Item> options = List.of(
            new Flute.Builder().listPrice(10).purchasePrice(5).build(),
            new Guitar.Builder().listPrice(20).purchasePrice(15).build()
        );
        var result = customer.getPurchaseInterest(options);
        assert result == options.get(1);
    }

    @Test
    void test_getPurchaseInterest_selectsOutOfBounds() {
        // Simulate user entering "5" which is out of bounds
        var customer = createCustomerWithInput("5\n");
        List<Item> options = List.of(new Guitar.Builder().listPrice(10).purchasePrice(5).build());
        var result = customer.getPurchaseInterest(options);
        assert result == null;
    }

    @Test
    void test_getPurchaseInterest_providesBlankInput() {
        // Simulate user pressing enter (blank line)
        var customer = createCustomerWithInput("\n");
        List<Item> options = List.of(new Guitar.Builder().listPrice(10).purchasePrice(5).build());
        var result = customer.getPurchaseInterest(options);
        assert result == null;
    }

    @Test
    void test_getPurchaseInterest_providesInvalidText() {
        // Simulate user entering non-numeric text
        var customer = createCustomerWithInput("not a number\n");
        List<Item> options = List.of(new Guitar.Builder().listPrice(10).purchasePrice(5).build());
        var result = customer.getPurchaseInterest(options);
        assert result == null;
    }

    // --- acceptPurchase ---

    @Test
    void test_acceptPurchase_accepts() {
        var customer = createCustomerWithInput("y\n");
        var item = new Guitar.Builder().listPrice(100).purchasePrice(50).build();
        boolean result = customer.acceptPurchase(item, 40.0);
        assert result == true;
    }

    @Test
    void test_acceptPurchase_rejects() {
        var customer = createCustomerWithInput("N\n"); // Test case-insensitivity
        var item = new Guitar.Builder().listPrice(100).purchasePrice(50).build();
        boolean result = customer.acceptPurchase(item, 40.0);
        assert result == false;
    }

    @Test
    void test_acceptPurchase_handlesInvalidInputThenAccepts() {
        // Simulate user entering gibberish, then "y"
        var customer = createCustomerWithInput("maybe\ny\n");
        var item = new Guitar.Builder().listPrice(100).purchasePrice(50).build();
        boolean result = customer.acceptPurchase(item, 40.0);
        assert result == true;
    }

    // --- getSaleOffer ---

    @Test
    void test_getSaleOffer_selectsAnItemToBuild() {
        // Simulate skipping the first item ("\n") and selecting the second ("y")
        var customer = createCustomerWithInput("\ny\n");
        BuildableItem.Builder<?> builder = customer.getSaleOffer();
        assert builder != null;
        // Verify it's the correct type of builder
        assert builder instanceof BuildableItem.Builder;
    }

    // --- acceptSale ---

    @Test
    void test_acceptSale_accepts() {
        var customer = createCustomerWithInput("y\n");
        var item = new Guitar.Builder().listPrice(100).purchasePrice(50).build();
        boolean result = customer.acceptSale(item);
        assert result == true;
    }

    @Test
    void test_acceptSale_rejects() {
        var customer = createCustomerWithInput("n\n");
        var item = new Guitar.Builder().listPrice(100).purchasePrice(50).build();
        boolean result = customer.acceptSale(item);
        assert result == false;
    }
}

