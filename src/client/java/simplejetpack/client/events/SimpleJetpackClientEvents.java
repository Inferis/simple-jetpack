package simplejetpack.client.events;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.phys.Vec3;
import simplejetpack.SimpleJetpack;
import simplejetpack.client.rendering.JetpackHudElement;
import simplejetpack.items.JetpackItem;
import simplejetpack.networking.SetJetpackActiveC2SPayload;
import simplejetpack.networking.SetJetpackFuelC2SPayload;

public class SimpleJetpackClientEvents {
    public static void registerEvents() {
        ClientTickEvents.START_CLIENT_TICK.register(SimpleJetpackClientEvents::onClientTick);
        HudElementRegistry.attachElementAfter(VanillaHudElements.BOSS_BAR, SimpleJetpack.id("hud"), new JetpackHudElement());
    }

    private static void onClientTick(Minecraft minecraft) {
        var player = minecraft.player;
        if (player != null) {
            if (player.isSpectator() || player.isCreative()) {
                // we don't want this happening when in creative or spectator
                return;
            }
            if (minecraft.level == null) {
                // just in case
                return;
            }

            var jetpackStack = JetpackItem.getEquippedJetpack(player);
            if (!jetpackStack.isEmpty() && JetpackItem.getActive(jetpackStack)) {
                var fuel = JetpackItem.consumeFuel(jetpackStack);
                fuel.ifPresent(f -> ClientPlayNetworking.send(new SetJetpackFuelC2SPayload(f)));

                if (player.getAbilities().flying) {
                    if (fuel.isPresent() && fuel.get() <= 0) {
                        JetpackItem.setActive(jetpackStack, false);
                        player.getAbilities().flying = false;
                        player.getAbilities().mayfly = false;
                        ClientPlayNetworking.send(new SetJetpackActiveC2SPayload(false));
                    }
                    else {
                        Vec3 vec = Vec3.directionFromRotation(0, player.getYRot()).reverse().multiply(0.33, 0.33, 0.33);
                        var x = player.getX() + vec.x;
                        var y = player.getY() + 0.25;
                        var z = player.getZ() + vec.z;
                        minecraft.level.addParticle(ParticleTypes.CLOUD, x, y, z, 0, -0.05, 0);
                    }
                }
            }
        }
    }
}
