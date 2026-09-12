package simplejetpack.items;

import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;

import java.util.function.Consumer;

public class JetpackToolTipAppender implements TooltipProvider {
    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> consumer, TooltipFlag flag, DataComponentGetter components) {
        var fuel = components.getOrDefault(JetpackItem.FUEL, 0);
        consumer.accept(Component.translatable("tooltip.simplejetpack.fuel", fuel, JetpackItem.MAX_FUEL));
    }
}
