package simplejetpack.menu;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import simplejetpack.items.SimpleJetpackItems;

public class JetpackSlot extends Slot {
    public JetpackSlot(Container inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return stack.is(SimpleJetpackItems.JETPACK);
    }
}
