package simplejetpack.blocks;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import simplejetpack.SimpleJetpack;

import java.util.function.Function;

public class SimpleJetpackBlocks {
    public static RechargerBlock RECHARGER;

    public static void registerBlocks() {
        RECHARGER = registerBlock("recharger", RechargerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.REPEATER));
    }

    private static <T extends Block> T registerBlock(final String identifier, final Function<BlockBehaviour.Properties, T> factory, final BlockBehaviour.Properties properties) {
        var key = ResourceKey.create(Registries.BLOCK, SimpleJetpack.id(identifier));
        var block = factory.apply(properties.setId(key));
        return Registry.register(BuiltInRegistries.BLOCK, key, block);
    }
}
