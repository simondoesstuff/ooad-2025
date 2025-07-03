package ooad.project4.model.customers;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import ooad.project4.ItemFactory;
import ooad.project4.model.item.BuildableItem;
import ooad.project4.model.item.Item;
import ooad.project4.model.item.music.accessories.Cable;
import ooad.project4.model.item.music.accessories.GigBag;
import ooad.project4.model.item.music.accessories.PracticeAmp;
import ooad.project4.model.item.music.accessories.Strings;
import ooad.project4.model.item.music.instruments.stringed.Stringed;
import ooad.project4.model.item.music.instruments.wind.Wind;
import ooad.project4.model.item.music.players.Players;

/**
 * Represents a customer who can buy or sell items.
 */
public class RandomCustomer implements Customer {
    private final int id;
    private Item boughtItem;
    private boolean buyEase;
    private boolean sellEase;

    public RandomCustomer(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Customer " + id;
    }

	@Override
	public Item getPurchaseInterest(List<Item> options) {
        if (ThreadLocalRandom.current().nextDouble(0, 1) <= .03) {
            // 3% chance that the Store doesn't have anything the Customer wants
            return null;
        }

        return options.get(ThreadLocalRandom.current().nextInt(0, options.size()));
	}

	@Override
	public boolean acceptPurchase(Item item, double offer) {
        // INFO: Case 3: the customer has already bought an item,
        // the Store is trying to sell us extras

        if (boughtItem instanceof Stringed) {
            double offset = 0;

            if (!((Stringed) boughtItem).isElectric()) offset -= 10;

            var chance = switch (item) {
                case GigBag i -> .2 + offset;
                case PracticeAmp i -> .25 + offset;
                case Cable i -> .3 + offset;
                case Strings i -> .4 + offset;
                default -> 0;
            };

            return ThreadLocalRandom.current().nextDouble() < chance;
        }

        // INFO:Case 2: the customer already rejected to buy an item once

        if (buyEase) {
            // this is a repeat attempt to offer the purchase,
            // the customer eases up
            buyEase = false;

            if (ThreadLocalRandom.current().nextDouble() < 0.75) {
                return true;
            }

            return false;
        }

        // INFO: Case 1: the customer is yet to recevie an offer

        double allure = 0;

        // certain well kept items are more likely to be bought
        switch (item) {
            case Players i:
                if (i.isEqualized()) allure += .10;
                break;
            case Stringed i:
                if (i.isTuned()) allure += .15;
                break;
            case Wind i:
                if (i.isAdjusted()) allure += .20;
                break;
            default:
                break;
        }

        if (ThreadLocalRandom.current().nextDouble() < 0.5 + allure) {
            buyEase = false;
            boughtItem = item;
            return true;
        }

        buyEase = true;
        return false;
	}

	@Override
	public BuildableItem.Builder<?> getSaleOffer() {
        return ItemFactory.buildRandom();
	}

	@Override
	public boolean acceptSale(Item item) {
        var offer = item.getPurchasePrice();

        // INFO: Case 2: customer has already rejected an offer
        if (sellEase) {
            sellEase = false;
            return ThreadLocalRandom.current().nextDouble() < .75;
        }

        // INFO: Case 1: customer is yet to receive an offer
        if (ThreadLocalRandom.current().nextDouble() < .5) {
            return true;
        }

        sellEase = true;
        return false;
	}
}
