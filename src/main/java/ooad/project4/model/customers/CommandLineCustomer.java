package ooad.project4.model.customers;
import java.util.List;
import java.util.Scanner;

import ooad.project4.model.item.BuildableItem.Builder;
import ooad.project4.model.item.BuildableItem;
import ooad.project4.ItemFactory;
import ooad.project4.model.item.Item;

public class CommandLineCustomer implements Customer {
    private Scanner sc;

    public CommandLineCustomer(Scanner sc) {
        this.sc = sc;
    }

	@Override
	public Item getPurchaseInterest(List<Item> options) {
        System.out.println("Please select an option from the list.");

        for (int i = 0; i < options.size(); i++) {
            var item = options.get(i);
            var type = item.getClass().getSimpleName();
            var cond = item.getCondition().toString();
            var price = item.getListPrice();
            System.out.printf(" - [%d] %s, Condition: %s, $%.2f\n", i, type, cond, price);
        }

        System.out.println("You may respond blank if you are uninterested.");
        var next = sc.nextLine().strip();

        try {
            int choice = Integer.parseInt(next);

            if (choice < 0 || choice >= options.size()) {
                System.out.println("That number isn't in range.");
                return null;
            }

            return options.get(choice);
        } catch (NumberFormatException e) {
            System.out.println("Since you didn't specify an integer, you're considered uninterested.");
            return null;
        }
	}

	@Override
	public boolean acceptPurchase(Item item, double offer) {
        var type = item.getClass().getSimpleName();
        System.out.printf("You're being offered an item: %s for $%.2f. Purchase? [y/n]\n", type, offer);

        while (true) {
            if (sc.hasNext()) {
                var next = sc.next().toLowerCase().strip();

                if (next.equals("y")) return true;
                if (next.equals("n")) return false;
            }

            System.out.println("Your decision is unclear. Expecting y or n.");
        }
	}

	@Override
	public Builder<?> getSaleOffer() {
        Class<? extends BuildableItem> selection = null;
        System.out.println("Select an item to build. Enter 'y' to select the type. Enter blank to skip.");

        for (var type : ItemFactory.getAllItemTypes()) {
            System.out.printf(" - Build %s?\n", type.getSimpleName());
            var next = sc.nextLine().toLowerCase().strip();

            if (next.equals("y")) {
                selection = type;
                break;
            }
        }

        if (selection == null) {
            System.out.println("Since you don't have an item to sell, you cannot sell.");
            return null;
        }

        var item = ItemFactory.buildRandom(selection);
        System.out.printf("You built a %s condition %s.\n", item.getCondition(), selection.getSimpleName());
        return item;
	}

	@Override
	public boolean acceptSale(Item item) {
        var offer = item.getPurchasePrice();
        System.out.printf("You're being offered $%.2f for the %s.\n", offer, item.getClass().getSimpleName());

        while (true) {
            System.out.println("Interested? [y/n]");
            var next = sc.nextLine().strip().toLowerCase();

            if (next.equals("y")) return true;
            if (next.equals("n")) return false;

            System.out.println("Your decision is unclear. Expecting y or n.");
        }
	}

    @Override
    public String toString() {
        return "The Genius";
    }
}
