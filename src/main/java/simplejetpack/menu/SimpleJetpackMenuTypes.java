package simplejetpack.menu;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.inventory.MenuType;
import simplejetpack.SimpleJetpack;

public class SimpleJetpackMenuTypes {
    public static MenuType<RechargerMenu> RECHARGER;

    public static void registerMenuTypes() {
        RECHARGER = Registry.register(
                BuiltInRegistries.MENU,
                SimpleJetpack.id("recharger"),
                new MenuType<>(RechargerMenu::new, FeatureFlagSet.of()));
    }
}
