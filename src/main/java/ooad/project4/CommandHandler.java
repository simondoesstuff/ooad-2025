package ooad.project4;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

import ooad.project4.model.customers.CommandLineCustomer;
import ooad.project4.model.store.Clerk;

public class CommandHandler {
    private final ArrayList<Clerk> clerks = new ArrayList<>();
    private Clerk selectedClerk = null;

    public CommandHandler(Collection<Clerk> clerks) {
        this.clerks.addAll(clerks);
    }

    public void run() {
        var sc = new Scanner(System.in);

        System.out.println("> help");
        helpCommand();

        while (true) {
            System.out.print("> ");
            var in = sc.nextLine().toLowerCase().strip();

            if (in.isBlank()) continue;

            if (in.equals("exit")) {
                System.out.println("Goodbye!");
                break;
            }

            switch (in) {
                case "help" -> helpCommand();
                case "list" -> listCommand();
                case String s when s.startsWith("select") -> {
                    try {
                        var arg = Integer.parseInt(s.substring(6).strip());
                        selectCommand(arg);
                    } catch (NumberFormatException e) {
                        System.out.println("Expected an integer");
                    }
                }
                case "name" -> nameCommand();
                case "time" -> timeCommand();
                case "buy" -> buyCommand(sc);
                case "sell" -> sellCommand(sc);
                default -> unknownCommand();
            };
        }

        sc.close();
    }

    public void helpCommand() {
        System.out.println(" - help : display commands");
        System.out.println(" - list : view the list of available stores");
        System.out.println(" - select <int> : select a store to visit");
        System.out.println(" - name : ask the clerk for their name");
        System.out.println(" - time : ask the clerk for the time");
        System.out.println(" - buy : start buy wizard");
        System.out.println(" - sell : start sell wizard");
        System.out.println(" - exit : exit command interface");
    }

    private void unknownCommand() {
        System.out.println("Unrecognized command...");
    }

    public void listCommand() {
        for (int i = 0; i < clerks.size(); i++) {
            var clerk = clerks.get(i);
            System.out.printf(" - [%d] %s: staffed by %s\n", i, clerk.getStore().getName(), clerk.getName());
        }
    }

    public void selectCommand(int option) {
        if (option < 0 || option >= clerks.size()) {
            System.out.println("You can only select a store within range of 0 to " + (clerks.size() - 1));
            return;
        }

        selectedClerk = clerks.get(option);
        System.out.printf("You selected %s, working at %s\n", selectedClerk.getName(), selectedClerk.getStore().getName());
    }

    private boolean ensureSelected() {
        if (selectedClerk == null) {
            System.out.println("You have to select a store first.");
            return false;
        }

        return true;
    }

    public void nameCommand() {
        if (!ensureSelected()) return;
        System.out.println("Hi! I'm " + selectedClerk.getName());
    }

    public void timeCommand() {
        if (!ensureSelected()) return;
        var time = LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm a"));
        System.out.println("It's " + time);
    }

    public void buyCommand(Scanner sc) {
        if (!ensureSelected()) return;
        selectedClerk.handleBuyingCustomer(new CommandLineCustomer(sc));
    }

    public void sellCommand(Scanner sc) {
        if (!ensureSelected()) return;
        selectedClerk.handleSellingCustomer(new CommandLineCustomer(sc));
    }
}
