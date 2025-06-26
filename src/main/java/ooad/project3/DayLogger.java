package ooad.project3;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import com.google.common.eventbus.Subscribe;

import lombok.Getter;
import ooad.project3.events.TheEventBus;
import ooad.project3.events.store.ArriveAtStoreEvent;
import ooad.project3.events.store.CheckRegisterEvent;
import ooad.project3.events.store.CleanTheStoreEvent;
import ooad.project3.events.store.DoInventoryEvent;
import ooad.project3.events.store.GoToBankEvent;
import ooad.project3.events.store.LeaveTheStoreEvent;
import ooad.project3.events.store.OpenTheStoreEvent;
import ooad.project3.events.store.OrderFailureEvent;
import ooad.project3.events.store.PlaceAnOrderEvent;
import ooad.project3.model.item.Item;
import ooad.project3.model.store.Order;

class DayLogger {
    @Getter
    private int day;
    @Getter
    private Path logPath;
    private PrintWriter writer;

    public DayLogger(String dir, int day) {
        this.day = day;
        var dirPath = Paths.get(dir);

        if (!Files.exists(dirPath) || !Files.isDirectory(dirPath)) {
            throw new IllegalArgumentException("Directory does not exist: " + dir);
        }

        this.logPath = Paths.get(dir, "Logger-" + day + ".txt");

        try {
            this.writer = new PrintWriter(this.logPath.toFile());
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("The log file could not be created");
        }

        // INFO: register
        TheEventBus.getInstance().getBus().register(this);
    }

    public void close() {
        TheEventBus.getInstance().getBus().unregister(this);
        this.writer.close();
    }

    public void logf(String s, Object... args) {
        this.writer.printf(s, args);
        System.out.printf(s, args);
    }

    @Subscribe
    private void onArriveAtStore(ArriveAtStoreEvent event) {
        logf("%s arrives at the store.\n", event.getClerkName());
        var orders = event.getRecentlyDelivered();

        if (orders.size() != 0) logf(" - new deliveries!\n");

        for (Order order : orders) {
            for (Item item : order.getItems()) {
                logf(" - added delivered item to inventory: %s\n", item.getName());
            }
        }
    }

    @Subscribe
    private void onCheckRegister(CheckRegisterEvent event) {
        var cash = event.getStore().getCashRegister().getCash();
        logf("%s counts the register. Current cash: $%.2f\n", event.getClerkName(), cash);
    }

    @Subscribe
    private void onGoToBank(GoToBankEvent event) {
        logf("Cash is low. %s is going to the bank.\n", event.getClerkName());
        logf("%s counts the register. Current cash: $%.2f\n",
                event.getClerkName(), event.getOldAmntInRegister());
        logf(" - withdrew from the bank. New register total: $%.2f\n",
                event.getNewAmntInRegister());
    }

    @Subscribe
    public void onDoInventory(DoInventoryEvent event) {
        logf("%s is doing inventory. Total purchase value of items: $%.2f\n",
                event.getClerkName(), event.getStore().getInventory().getTotalPurchasePrice());
        var items = event.getInventory().size();
        var damaged = event.getRecentlyDamaged().size();
        logf(" - there were %d items and %d were damaged during tuning.\n",
                items, damaged);
    }

    @Subscribe
    public void onPlaceAnOrder(PlaceAnOrderEvent event) {
        var order = event.getOrder();
        var orderType = order.getItems().get(0).getClass().getSimpleName();
        var orderSize = order.getItems().size();
        var arrival = order.getArrivalDay();
        logf("%s wanted to order 3 %ss because they were missing from inventory and %d were ordered, to arrive on day %d\n",
        event.getClerkName(), orderType, orderSize, arrival);
    }

    @Subscribe
    public void onOrderFailur(OrderFailureEvent event) {
        logf(" - failed to order %d %s(s) because there was not enough cash in the register.\n",
                event.getFailQuantity(), event.getType().getSimpleName());
    }

    @Subscribe
    public void onOpenTheStore(OpenTheStoreEvent event) {
        logf("%s opens the store.\n", event.getClerkName());
        logf(" - there are %d buying and %d selling customers today.\n",
            event.getBuyingCustomers(), event.getSellingCustomers());
        logf(" - there were %d total sales and %d total purchases.\n",
            event.getTotalSales(), event.getTotalPurchases());
    }

    @Subscribe
    public void onCleanTheStore(CleanTheStoreEvent event) {
        logf("%s is cleaning the store.\n", event.getClerkName());
        var damages = event.getRecentlyDamaged().size();

        if (damages > 0) {
            logf(" - Oh no! %d item(s) were damaged while cleaning.\n", damages);
        }
    }

    @Subscribe
    public void onLeaveTheStore(LeaveTheStoreEvent event) {
        logf("%s locks up and goes home.\n", event.getClerkName());
    }
}
