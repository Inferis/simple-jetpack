package simplejetpack.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;
import simplejetpack.SimpleJetpack;
import simplejetpack.items.JetpackItem;
import simplejetpack.items.SimpleJetpackItems;
import simplejetpack.menu.RechargerMenu;

import java.util.Optional;

public class RechargerBlockEntity extends BlockEntity implements MenuProvider, ContainerListener {
    public static final int JETPACK_SLOT_INDEX = 0;
    public static final int FUEL_SLOT_INDEX = 1;
    private final SimpleContainer container;
    private final ContainerData containerData;
    private int fuelLeft;
    private int fuelMax;

    public RechargerBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(SimpleJetpackBlockEntityTypes.RECHARGER, worldPosition, blockState);
        container = new SimpleContainer(2);

        this.containerData = new ContainerData() {
            public int get(int index) {
                switch (index) {
                    case 0: return RechargerBlockEntity.this.fuelLeft;
                    case 1: return RechargerBlockEntity.this.fuelMax;
                    case 2: {
                        var jetpackStack = container.getItem(JETPACK_SLOT_INDEX);
                        return jetpackStack.getOrDefault(JetpackItem.FUEL, 0);
                    }
                    case 3: return JetpackItem.MAX_FUEL;
                    default: return 0;
                }
            }

            public void set(int index, int value) {
                switch (index) {
                    case 0: RechargerBlockEntity.this.fuelLeft = value; break;
                    case 1: RechargerBlockEntity.this.fuelMax = value; break;
                }
            }

            public int getCount() {
                return 4;
            }
        };
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("screen.jetpack.recharger.title");
    }

    @Override
    public void slotChanged(AbstractContainerMenu menu, int slotIndex, ItemStack itemStack) {
        if (slotIndex == JETPACK_SLOT_INDEX) {
            jetpackSlotChanged(itemStack);
            setChanged();
        }
        else if (slotIndex == FUEL_SLOT_INDEX) {
            setChanged();
        }
    }

    private void jetpackSlotChanged(ItemStack itemStack) {
        var level = getLevel();
        if (level != null) {
            var hasJetpack = itemStack.is(SimpleJetpackItems.JETPACK);
            level.setBlockAndUpdate(
                    worldPosition,
                    level.getBlockState(worldPosition).setValue(RechargerBlock.HAS_JETPACK, hasJetpack));
            SimpleJetpack.LOGGER.info("slotChanged " + hasJetpack);
        }
    }

    @Override
    public void dataChanged(AbstractContainerMenu container, int id, int value) {

    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);

        if (input.contains("jetpack_stack")) {
            var stack = input.read("jetpack_stack", ItemStack.CODEC);
            stack.ifPresent(s -> container.setItem(0, s));
        }

        if (input.contains("fuel_stack")) {
            var stack = input.read("fuel_stack", ItemStack.CODEC);
            stack.ifPresent(s -> container.setItem(1, s));
        }

        fuelLeft = input.getIntOr("fuel_left", 0);
        fuelMax = input.getIntOr("fuel_max", 0);
        
//        getLevel().setBlockAndUpdate(
//                worldPosition,
//                level.getBlockState(worldPosition).setValue(
//                        RechargerBlock.HAS_JETPACK,
//                        container.getItem(JETPACK_SLOT_INDEX).is(SimpleJetpackItems.JETPACK)));
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);

        var stack = container.getItem(JETPACK_SLOT_INDEX);
        if (!stack.isEmpty()) {
            output.store("jetpack_stack", ItemStack.CODEC, stack);
        }

        stack = container.getItem(FUEL_SLOT_INDEX);
        if (!stack.isEmpty()) {
            output.store("fuel_stack", ItemStack.CODEC, stack);
        }

        output.putInt("fuel_left", fuelLeft);
        output.putInt("fuel_max", fuelMax);
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        var menu = new RechargerMenu(containerId, inventory, container, containerData);
        menu.addSlotListener(this);
        return menu;
    }

    public void tick(Level world, BlockPos pos, BlockState state) {
        var jetpackStack = container.getItem(JETPACK_SLOT_INDEX);
        var fuelStack = container.getItem(FUEL_SLOT_INDEX);
    }

    public void swapItem(Inventory inventory, ItemStack itemStack) {
        var oldItem = container.getItem(JETPACK_SLOT_INDEX);
        if (oldItem.isEmpty()) {
            container.setItem(JETPACK_SLOT_INDEX, itemStack);
            inventory.removeItem(itemStack);
            jetpackSlotChanged(itemStack);
        }
        else {
            container.removeAllItems();
            inventory.removeItem(itemStack);
            container.setItem(JETPACK_SLOT_INDEX, itemStack);
            inventory.add(oldItem);
            jetpackSlotChanged(itemStack);
        }
    }

    public Container container() {
        return container;
    }
}
