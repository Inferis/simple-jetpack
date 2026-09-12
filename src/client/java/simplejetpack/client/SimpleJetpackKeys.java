package simplejetpack.client;

import com.mojang.authlib.minecraft.client.MinecraftClient;
import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import simplejetpack.SimpleJetpack;
import simplejetpack.items.JetpackItem;
import simplejetpack.networking.SetJetpackActiveC2SPayload;

public class SimpleJetpackKeys {
    private static final KeyMapping.Category KEYS_CATEGORY = KeyMapping.Category.register(SimpleJetpack.id("keys"));
    private static KeyMapping toggleBinding;
    private static KeyMapping flyBinding;

    public static void registerKeyMappings() {
        toggleBinding = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.simplejetpack.hover",
                InputConstants.Type.KEYSYM,
                InputConstants.KEY_H,
                KEYS_CATEGORY)
        );
        flyBinding = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.simplejetpack.fly",
                InputConstants.Type.KEYSYM,
                InputConstants.KEY_J,
                KEYS_CATEGORY)
        );

        ClientTickEvents.START_CLIENT_TICK.register(SimpleJetpackKeys::onClientTick);
    }

    private static void onClientTick(Minecraft minecraft) {
        var player = minecraft.player;
        if (player == null) {
            return;
        }

        if (player.isSpectator() || player.isCreative()) {
            // we don't want this happening when in creative or spectator
            return;
        }
        if (minecraft.level == null) {
            // just in case
            return;
        }

        if (toggleBinding.consumeClick()) {
            toggleJetpackActive(minecraft);
            return;
        }

        if (minecraft.player.onGround() && !minecraft.player.isSpectator() && !minecraft.player.isCreative()) {
            var jetpack = JetpackItem.getEquippedJetpack(minecraft.player);
            if (JetpackItem.getActive(jetpack)) {
                JetpackItem.setActive(jetpack, false);
                player.getAbilities().flying = false;
                player.getAbilities().mayfly = false;
                if (ClientPlayNetworking.canSend(SetJetpackActiveC2SPayload.ID)) {
                    ClientPlayNetworking.send(new SetJetpackActiveC2SPayload(false));
                }
            }

        }
    }

    private static void toggleJetpackActive(Minecraft client) {
        var player = client.player;
        if (player != null && !player.isSpectator() && !player.isCreative()) {
            var jetpackStack = JetpackItem.getEquippedJetpack(player);
            if (!jetpackStack.isEmpty()) {
                if (JetpackItem.getActive(jetpackStack) || jetpackStack.getOrDefault(JetpackItem.FUEL, 0) > 0) {
                    var active = JetpackItem.toggleActive(jetpackStack);
                    player.getAbilities().flying = active;
                    player.getAbilities().mayfly = active;

                    if (player.onGround() && active) {
                        player.setPos(player.getX(), player.getY()+0.5, player.getZ());
                    }

                    ClientPlayNetworking.send(new SetJetpackActiveC2SPayload(active));
                }
            }
        }
    }
}
