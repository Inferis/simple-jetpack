package simplejetpack.items;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.equipment.*;
import org.jetbrains.annotations.NotNull;
import simplejetpack.SimpleJetpack;

import java.util.Optional;

import static net.minecraft.world.item.equipment.EquipmentAssets.ROOT_ID;
import static simplejetpack.items.SimpleJetpackItems.JETPACK_TOOLTIP_APPENDER;

public class JetpackItem extends Item {
    public static final Integer MAX_FUEL = 50000;

    public static final DataComponentType<Boolean> ACTIVE = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            SimpleJetpack.id("active"),
            DataComponentType.<Boolean>builder().persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL).build()
    );
    public static final DataComponentType<Integer> FUEL = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            SimpleJetpack.id("fuel"),
            DataComponentType.<Integer>builder().persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT).build()
    );

    public JetpackItem(Properties properties) {
        super(properties.durability(432)
                        .rarity(Rarity.EPIC)
                        .component(
                                DataComponents.EQUIPPABLE,
                                Equippable.builder(EquipmentSlot.CHEST)
                                        .setEquipSound(SoundEvents.ARMOR_EQUIP_GENERIC)
                                        .setAsset(createAssetId("jetpack"))
                                        .setDamageOnHurt(false)
                                        .build()
                        )
                        .stacksTo(1)
                        .component(FUEL, MAX_FUEL)
                        .component(JETPACK_TOOLTIP_APPENDER, new JetpackToolTipAppender())
                // ACTIVE isn't necessary to initialize
        );
    }

    static ResourceKey<EquipmentAsset> createAssetId(final String name) {
        return ResourceKey.create(ROOT_ID, SimpleJetpack.id(name));
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        var fuel = stack.getOrDefault(FUEL, 0);
        return Math.clamp((int)Math.ceil((float)fuel * 13.0F / MAX_FUEL), 0, 13);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return 0x11c9e9;
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        var fuel = stack.getOrDefault(FUEL, 0);
        return fuel < MAX_FUEL;
    }

    public static boolean toggleActive(ItemStack stack) {
        setActive(stack, !getActive(stack));
        return getActive(stack);
    }

    public static boolean getActive(@NotNull ItemStack stack) {
        return stack.getOrDefault(ACTIVE, false);
    }

    public static void setActive(ItemStack stack, boolean active) {
        if (active) {
            stack.set(ACTIVE, true);
        }
        else {
            stack.remove(ACTIVE);
        }
    }

    public static ItemStack getEquippedJetpack(@NotNull Player player) {
        var chest = player.getItemBySlot(EquipmentSlot.CHEST);
        if (chest.is(SimpleJetpackItems.JETPACK)) {
            return chest;
        }
        return ItemStack.EMPTY;
    }

    public static Optional<Integer> consumeFuel(@NotNull ItemStack jetpackStack) {
        var fuel = jetpackStack.getOrDefault(FUEL, 0);
        if (fuel > 0) {
            fuel -= 1;
            jetpackStack.set(FUEL, fuel);
            return Optional.of(fuel);
        }
        return Optional.empty();
    }
}
