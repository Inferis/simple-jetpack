package simplejetpack.client.rendering;

import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import simplejetpack.items.JetpackItem;

public class JetpackHudElement implements HudElement {
    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
        var client = Minecraft.getInstance();
        var player = client.player;

        if (player != null) {
            var jetpack = JetpackItem.getEquippedJetpack(player);
            if (!jetpack.isEmpty()) {
                var fuel = jetpack.getOrDefault(JetpackItem.FUEL, 0);
                graphics.fakeItem(jetpack, 8, graphics.guiHeight() / 2 - 8);
                if (JetpackItem.getActive(jetpack)) {
                    graphics.text(client.font, fuel + "/" + JetpackItem.MAX_FUEL, 34, graphics.guiHeight() / 2 - client.font.lineHeight + 5, 0xffffffff, true);
                }
            }
        }
    }
}
