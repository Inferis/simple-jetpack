package simplejetpack.items;

import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.item.v1.ItemComponentTooltipProviderRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import simplejetpack.SimpleJetpack;
import simplejetpack.blocks.SimpleJetpackBlocks;

import java.util.function.BiFunction;
import java.util.function.Function;

public class SimpleJetpackItems {
    public static JetpackItem JETPACK;
    public static BlockItem RECHARGER;

    public static void registerItems() {
        JETPACK = registerItem("jetpack", JetpackItem::new, new Item.Properties());
        RECHARGER = registerItem("recharger", BlockItem::new, SimpleJetpackBlocks.RECHARGER, new Item.Properties());

        ItemComponentTooltipProviderRegistry.addLast(JETPACK_TOOLTIP_APPENDER);
    }

    private static <T extends Item> T registerItem(final String identifier, final Function<Item.Properties, T> factory, final Item.Properties properties) {
        var key = ResourceKey.create(Registries.ITEM, SimpleJetpack.id(identifier));
        var item = factory.apply(properties.setId(key));
        return Registry.register(BuiltInRegistries.ITEM, key, item);
    }

    private static <T extends Item> T registerItem(final String identifier, final BiFunction<Block, Item.Properties, T> factory, final Block block, final Item.Properties properties) {
        var key = ResourceKey.create(Registries.ITEM, SimpleJetpack.id(identifier));
        var item = factory.apply(block, properties.setId(key));
        return Registry.register(BuiltInRegistries.ITEM, key, item);
    }

    public static final DataComponentType<JetpackToolTipAppender> JETPACK_TOOLTIP_APPENDER = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            SimpleJetpack.id("jetpack_tooltip_appender"),
            DataComponentType.<JetpackToolTipAppender>builder().persistent(MapCodec.unit(new JetpackToolTipAppender()).codec()).build()
    );
}
