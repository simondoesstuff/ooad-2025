package ooad.project3.model.store;

// TODO: Clerk should encapsulate its store-management routine to enable the possibility
// of clerk variants that skip steps or perform extra steps.
public class Clerk {
    private String name;
    private final double cleaningDamageChance;
    private int workStreak = 0;

    public Clerk(String name, double cleaningDamageChance) {
        this.name = name;
        this.cleaningDamageChance = cleaningDamageChance;
    }

    public String getName() {
        return name;
    }

    public double getCleaningDamageChance() {
        return cleaningDamageChance;
    }

    public int getWorkStreak() {
        return workStreak;
    }

    public void increaseWorkStreak() {
        this.workStreak++;
    }

    public void resetWorkStreak() {
        this.workStreak = 0;
    }
}
