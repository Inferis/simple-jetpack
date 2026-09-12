package simplejetpack.items;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import simplejetpack.SimpleJetpack;

import java.util.function.Function;

public class SimpleJetpackItems {
    public static JetpackItem JETPACK;

    public static void registerItems() {
        JETPACK = registerItem("jetpack", JetpackItem::new, new Item.Properties());
    }

    private static <T extends Item> T registerItem(final String identifier, final Function<Item.Properties, T> factory, final Item.Properties properties) {
        var key = ResourceKey.create(Registries.ITEM, SimpleJetpack.id(identifier));
        var item = factory.apply(properties.setId(key));
        return Registry.register(BuiltInRegistries.ITEM, key, item);
    }
}
