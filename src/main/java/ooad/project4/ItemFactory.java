package ooad.project4;

import ooad.project4.model.item.BuildableItem;
import ooad.project4.model.item.Condition;
import ooad.project4.model.item.BuildableItem;
import ooad.project4.model.item.clothing.Bandana;
import ooad.project4.model.item.clothing.Hat;
import ooad.project4.model.item.clothing.Shirt;
import ooad.project4.model.item.music.CD;
import ooad.project4.model.item.music.Cassette;
import ooad.project4.model.item.music.Music;
import ooad.project4.model.item.music.PaperScore;
import ooad.project4.model.item.music.Vinyl;
import ooad.project4.model.item.music.accessories.Cable;
import ooad.project4.model.item.music.accessories.GigBag;
import ooad.project4.model.item.music.accessories.PracticeAmp;
import ooad.project4.model.item.music.accessories.Strings;
import ooad.project4.model.item.music.instruments.stringed.Bass;
import ooad.project4.model.item.music.instruments.stringed.Guitar;
import ooad.project4.model.item.music.instruments.stringed.Mandolin;
import ooad.project4.model.item.music.instruments.stringed.Stringed;
import ooad.project4.model.item.music.instruments.wind.Flute;
import ooad.project4.model.item.music.instruments.wind.Harmonica;
import ooad.project4.model.item.music.instruments.wind.Saxophone;
import ooad.project4.model.item.music.players.CDPlayer;
import ooad.project4.model.item.music.players.CassettePlayer;
import ooad.project4.model.item.music.players.MP3Player;
import ooad.project4.model.item.music.players.RecordPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

/**
 * An ItemFactory for creating items using the Builder pattern.
 * This factory decouples the simulation logic from the concrete item classes.
 * It uses a map of Builder suppliers to instantiate the correct builder, then
 * populates it with random data to create a new item.
 */
public class ItemFactory {
    private static final Random rand = ThreadLocalRandom.current();

    // A map of item classes to a function that creates a new builder for that class.
    private static final Map<Class<? extends BuildableItem>, Supplier<BuildableItem.Builder<?>>> builders = new HashMap<>();
    private static final List<Class<? extends BuildableItem>> itemTypes;
    private static final Set<String> knownNames = new HashSet<>();

    static {
        builders.put(CD.class, CD.Builder::new);
        builders.put(Cassette.class, Cassette.Builder::new);
        builders.put(Vinyl.class, Vinyl.Builder::new);
        builders.put(PaperScore.class, PaperScore.Builder::new);
        builders.put(CDPlayer.class, CDPlayer.Builder::new);
        builders.put(CassettePlayer.class, CassettePlayer.Builder::new);
        builders.put(RecordPlayer.class, RecordPlayer.Builder::new);
        builders.put(MP3Player.class, MP3Player.Builder::new);
        builders.put(Guitar.class, Guitar.Builder::new);
        builders.put(Bass.class, Bass.Builder::new);
        builders.put(Mandolin.class, Mandolin.Builder::new);
        builders.put(Flute.class, Flute.Builder::new);
        builders.put(Harmonica.class, Harmonica.Builder::new);
        builders.put(Saxophone.class, Saxophone.Builder::new);
        builders.put(Hat.class, Hat.Builder::new);
        builders.put(Shirt.class, Shirt.Builder::new);
        builders.put(Bandana.class, Bandana.Builder::new);
        builders.put(PracticeAmp.class, PracticeAmp.Builder::new);
        builders.put(Cable.class, Cable.Builder::new);
        builders.put(Strings.class, Strings.Builder::new);
        builders.put(GigBag.class, GigBag.Builder::new);
        itemTypes = new ArrayList<>(builders.keySet());
    }

    public static List<Class<? extends BuildableItem>> getAllItemTypes() {
        return itemTypes;
    }

    public static Class<? extends BuildableItem> getRandomItemType() {
        return itemTypes.get(rand.nextInt(itemTypes.size()));
    }

    /**
     * Picks a random Item type
     */
    public static BuildableItem.Builder<?> buildRandom() {
        return buildRandom(getRandomItemType());
    }

    /**
     * Builds a new item of a given class with random attributes.
     * Does not provide listPrice, purchasePrice, dayArrived, newOrUsed, or any sale details.
     *
     * @param itemClass The concrete class of the item to build.
     * @return A fully constructed, randomized Item.
     */
    public static BuildableItem.Builder<?> buildRandom(Class<? extends BuildableItem> itemClass) {
        Supplier<BuildableItem.Builder<?>> builderSupplier = builders.get(itemClass);

        if (builderSupplier == null) {
            // should never throw
            throw new IllegalArgumentException("No builder registered for class: " + itemClass.getName());
        }

        var builder = builderSupplier.get()
            .name(trivialName(itemClass.getSimpleName()))
            .condition(Utils.getRandomEnum(Condition.class));

        // specify random specific attributes based on subtype
        return switch(builder) {
            // music
            case Music.Builder<?> b -> b.band(trivialName("band"))
                                        .album(trivialName("album"));
            // instruments
            case Stringed.Builder<?> b -> b.isElectric(rand.nextBoolean());
            case Flute.Builder b -> b.type(Utils.getRandomEnum(Flute.FluteMaterial.class));
            case Harmonica.Builder b -> b.key(Utils.getRandomEnum(Harmonica.HarmonicaKey.class));
            // clothing
            case Hat.Builder b -> b.hatSize(rand.nextDouble(10));
            case Shirt.Builder b -> b.shirtSize(Utils.getRandomEnum(Shirt.ShirtSize.class));
            // (musical) accessories
            case PracticeAmp.Builder b -> b.wattage(rand.nextDouble(10));
            case Cable.Builder b -> b.length(rand.nextDouble(10));
            case Strings.Builder b -> b.type(Utils.getRandomEnum(Strings.StringType.class));
            //
            default -> builder;
        };
    }

    private static String trivialName(String x) {
        var name = x + "(" + Utils.makeFunnyName() + ")";
        if (knownNames.contains(name)) return trivialName(x);
        knownNames.add(name);
        return name;
    }
}
