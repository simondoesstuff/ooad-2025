package ooad.project4;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.concurrent.ThreadLocalRandom;

import ooad.project4.model.store.Clerk;

/**
 * Maintains a pool of un/assigned Clerks.
 * Enables logic regarding sick or exhausted Clerks while simultaneously
 * reusing Clerks between stores or days.
 */
public class ClerkPool {
    private ArrayList<Clerk> unassigned = new ArrayList<>();
    private ArrayList<Clerk> assigned = new ArrayList<>();

    /**
     * Adds a new Clerk to the pool of unassigned clerks.
     * The clerk is now available for assignment.
     */
    public void add(Clerk clerk) {
        if (clerk != null) {
            unassigned.add(clerk);
        }
    }

    /**
     * Takes a randomly available clerk from the unassigned list,
     * moves them to the assigned list, and returns the clerk.
     */
    public Clerk assign() {
        if (unassigned.isEmpty()) {
            throw new NoSuchElementException("No available clerks to assign.");
        }

        var idx = ThreadLocalRandom.current().nextInt(unassigned.size());
        Clerk clerk = unassigned.remove(idx);
        assigned.add(clerk);
        return clerk;
    }

    /**
     * Relinquishes a previously assigned clerk, returning them to the unassigned pool.
     */
    public void makeAssignable(Clerk clerk) {
        // guards
        if (clerk == null) throw new IllegalArgumentException("clerk must not be null");
        if (!assigned.remove(clerk)) throw new IllegalArgumentException("clerk must be previously assigned");

        // add the clerk back to the unassigned pool
        unassigned.add(clerk);
    }

    /**
     * Resets the work streak for all Clerks in the unassigned pool.
     * Notifies each Clerk of the day.
     */
    // TODO: update UML
    public void rest(int day) {
        // all Clerks should always know the day
        // TODO: extract day cycle to singleton
        for (var clerk : assigned) clerk.setToday(day);
        for (var clerk : unassigned) clerk.setToday(day);

        for (var clerk : unassigned) {
            // non working Clerks reset work streak
            clerk.resetWorkStreak();
        }
    }
}
