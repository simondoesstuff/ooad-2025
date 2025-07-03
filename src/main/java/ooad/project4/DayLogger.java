package ooad.project4;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.common.eventbus.Subscribe;
import com.google.common.eventbus.EventBus;
import lombok.Getter;
import ooad.project4.events.TheEventBus;
import ooad.project4.events.store.ArriveAtStoreEvent;
import ooad.project4.events.store.CheckRegisterEvent;
import ooad.project4.events.store.CleanTheStoreEvent;
import ooad.project4.events.store.DoInventoryEvent;
import ooad.project4.events.store.GoToBankEvent;
import ooad.project4.events.store.LeaveTheStoreEvent;
import ooad.project4.events.store.OpenTheStoreEvent;
import ooad.project4.events.store.OrderFailureEvent;
import ooad.project4.events.store.PlaceAnOrderEvent;
import ooad.project4.model.item.Item;
import ooad.project4.model.store.Order;

class DayLogger {
    @Getter
    private int day;
    @Getter
    private Path logPath;
    private PrintWriter writer;
    private EventBus bus;

    /**
     * @arg channel: corresponds to the EventBus channel to listen on
     */
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
        this.bus = TheEventBus.getInstance().getBus();
        this.bus.register(this);
    }

    public void close() {
        this.bus.unregister(this);
        this.writer.close();
        this.writer = null;
    }

    synchronized
    public void logf(String s, Object... args) {
        if (this.writer == null) return;

        this.writer.printf(s, args);
        System.out.printf(s, args);
    }

    @Subscribe
    private void onArriveAtStore(ArriveAtStoreEvent event) {
        logf("%s: %s arrives at the store.\n", event.getStore().getName(), event.getClerkName());
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
        logf("%s: %s counts the register. Current cash: $%.2f\n", event.getStore().getName(), event.getClerkName(), cash);
    }

    @Subscribe
    private void onGoToBank(GoToBankEvent event) {
        logf("%s: Cash is low. %s is going to the bank.\n", event.getStore().getName(), event.getClerkName());
        logf(" - withdrew from the bank. New register total: $%.2f\n",
                event.getNewAmntInRegister());
    }

    @Subscribe
    public void onDoInventory(DoInventoryEvent event) {
        logf("%s: %s is doing inventory. Total purchase value of items: $%.2f\n",
               event.getStore().getName(), event.getClerkName(), event.getStore().getInventory().getTotalPurchasePrice());

        if (event.isStartedClothingBan()) {
            logf("%s: The last clothing item has been sold -- buy no more.\n",
                    event.getStore().getName());
        }

        var items = event.getInventory().size();
        var damaged = event.getRecentlyDamaged().size();
        logf(" - there were %d items and %d were damaged during tuning.\n",
                items, damaged);
    }

    @Subscribe
    public void onPlaceAnOrder(PlaceAnOrderEvent event) {
        var order = event.getOrder();
        var orderSize = order.getItems().size();
        var arrival = order.getArrivalDay();

        if (orderSize != 0) {
            var orderType = order.getItems().get(0).getClass().getSimpleName();
            logf("%s: %s wanted to order 3 %ss because they were missing from inventory and %d were ordered, to arrive on day %d\n",
                   event.getStore().getName(), event.getClerkName(), orderType, orderSize, arrival);
        } else {
            logf("%s: %s wanted to order some items but we're broke!\n", event.getStore().getName(), event.getClerkName());
        }

    }

    @Subscribe
    public void onOrderFailure(OrderFailureEvent event) {
        logf(" - failed to order %d %s(s) because there was not enough cash in the register.\n",
                event.getFailQuantity(), event.getType().getSimpleName());
    }

    @Subscribe
    public void onOpenTheStore(OpenTheStoreEvent event) {
        logf("%s: %s finished opening the store.\n", event.getStore().getName(), event.getClerkName());
        logf(" - there are %d buying and %d selling customers today.\n",
            event.getBuyingCustomers(), event.getSellingCustomers());
        logf(" - there were %d total sales and %d total purchases.\n",
            event.getTotalSales(), event.getTotalPurchases());
    }

    @Subscribe
    public void onCleanTheStore(CleanTheStoreEvent event) {
        logf("%s: %s is cleaning the store.\n", event.getStore().getName(), event.getClerkName());
        var damages = event.getRecentlyDamaged().size();

        if (damages > 0) {
            logf(" - Oh no! %d item(s) were damaged while cleaning.\n", damages);
        }
    }

    @Subscribe
    public void onLeaveTheStore(LeaveTheStoreEvent event) {
        logf("%s: %s locks up and goes home.\n", event.getStore().getName(), event.getClerkName());
    }
}
