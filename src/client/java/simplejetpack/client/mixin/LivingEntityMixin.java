package simplejetpack.client.mixin;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import simplejetpack.items.JetpackItem;
import simplejetpack.networking.SetJetpackActiveC2SPayload;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(at = @At("RETURN"), method = "Lnet/minecraft/world/entity/LivingEntity;onEquipItem(Lnet/minecraft/world/entity/EquipmentSlot;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)V")
    public void onEquipItem(EquipmentSlot slot, ItemStack oldStack, ItemStack stack, CallbackInfo ci) {
        var livingEntity = (LivingEntity)(Object)this;
        if (livingEntity.level().isClientSide() && livingEntity instanceof LocalPlayer player && !player.isSpectator() && !player.isCreative()) {
            if (oldStack.getItem() instanceof JetpackItem && slot.isArmor()) {
                JetpackItem.setActive(oldStack, false);
                player.getAbilities().flying = false;
                player.getAbilities().mayfly = false;

                if (ClientPlayNetworking.canSend(SetJetpackActiveC2SPayload.ID)) {
                    ClientPlayNetworking.send(new SetJetpackActiveC2SPayload(false));
                }
            }
        }
    }
}
