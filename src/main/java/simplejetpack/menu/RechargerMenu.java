package simplejetpack.menu;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import simplejetpack.blocks.RechargerBlockEntity;
import simplejetpack.items.SimpleJetpackItems;

public class RechargerMenu extends AbstractContainerMenu {
    private final Container container;
    private final ContainerData data;
    private final Level level;

    public RechargerMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, new SimpleContainer(2), new SimpleContainerData(4));
    }

    public RechargerMenu(int containerId, Inventory playerInventory, Container container, final ContainerData data) {
        super(SimpleJetpackMenuTypes.RECHARGER, containerId);

        this.container = container;
        this.data = data;
        this.level = playerInventory.player.level();

        addSlot(new JetpackSlot(container, 0, 84, 25));
        addSlot(new FuelSlot(this, container, 1, 105, 53));
        addStandardInventorySlots(playerInventory, 8, 84);
        addDataSlots(data);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotIndex) {
        ItemStack itemStack = ItemStack.EMPTY;
        var slot = this.slots.get(slotIndex);
        var jetpackSlot = (JetpackSlot)slots.get(RechargerBlockEntity.JETPACK_SLOT_INDEX);
        var fuelSlot = (FuelSlot)slots.get(RechargerBlockEntity.FUEL_SLOT_INDEX);

        if (slot.hasItem()) {
            var slotStack = slot.getItem();
            if (slot.getItem().is(SimpleJetpackItems.JETPACK)) {
                if (slotIndex == RechargerBlockEntity.JETPACK_SLOT_INDEX) {
                    // going from jetpack slot to inventory
                    if (!moveItemStackTo(slotStack, 29, 38, false)) {
                        if (!moveItemStackTo(slotStack, 2, 29, false)) {
                            return ItemStack.EMPTY;
                        }
                    }
                    slot.setChanged();
                }
                else if (!jetpackSlot.hasItem()) {
                    // going from inventory to jetpack slot
                    if (!moveItemStackTo(slotStack, RechargerBlockEntity.JETPACK_SLOT_INDEX, RechargerBlockEntity.JETPACK_SLOT_INDEX + 1, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            }
            else if (slotIndex == RechargerBlockEntity.FUEL_SLOT_INDEX) {
                // going from fuel slot to inventory
                if (!moveItemStackTo(slotStack, 2, 38, false)) {
                    return ItemStack.EMPTY;
                }
                slot.setChanged();
            }
            else if (isFuel(slot.getItem())) {
                // going from inventory to fuel slot
                if (!moveItemStackTo(slotStack, RechargerBlockEntity.FUEL_SLOT_INDEX, RechargerBlockEntity.FUEL_SLOT_INDEX + 1, false)) {
                    return ItemStack.EMPTY;
                }
            }
        }

        return itemStack;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
    
    public boolean isFuel(ItemStack stack) {
        return level.fuelValues().isFuel(stack);
    }

    public boolean isCharging() {
        return getChargeProgress() > 0;
    }

    public float getChargeProgress() {
        return ((float)data.get(1) - data.get(0)) / data.get(1);
    }
}
