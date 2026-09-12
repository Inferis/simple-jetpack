package simplejetpack.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import simplejetpack.SimpleJetpack;

public class SimpleJetpackBlockEntityTypes {
    public static BlockEntityType<RechargerBlockEntity> RECHARGER;

    public static <T extends BlockEntityType<?>> T register(String identifier, T blockEntityType) {
        return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, SimpleJetpack.id(identifier), blockEntityType);
    }

    public static void registerBlockEntityTypes() {
        RECHARGER  = register(
                "recharger",
                FabricBlockEntityTypeBuilder.create(RechargerBlockEntity::new, SimpleJetpackBlocks.RECHARGER).build());

    }
}
