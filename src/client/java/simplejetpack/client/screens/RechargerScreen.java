package simplejetpack.client.screens;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import simplejetpack.SimpleJetpack;
import simplejetpack.menu.RechargerMenu;

public class RechargerScreen extends AbstractContainerScreen<RechargerMenu> {
    public static final Identifier BACKGROUND_TEXTURE = SimpleJetpack.id("textures/gui/container/recharger.png");
    public static final Identifier CHARGE_PROGRESS_TEXTURE = SimpleJetpack.id("textures/gui/sprites/container/recharger/charge_progress.png");
    public static final Identifier SPARKLES_TEXTURE = SimpleJetpack.id("textures/gui/sprites/container/recharger/sparkles.png");

    private int sparkles;

    public RechargerScreen(RechargerMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);

        sparkles = 0;
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractBackground(graphics, mouseX, mouseY, a);
        graphics.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND_TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight, 256, 256 );

        if (menu.isCharging()) {
            int progress = Mth.ceil(menu.getChargeProgress() * 13.0F) + 1;
            graphics.blit(RenderPipelines.GUI_TEXTURED, CHARGE_PROGRESS_TEXTURE, leftPos + 82, topPos + 56 + 14 - progress, 0, 14 - progress, 14, progress, 14, 14);
            sparkles = sparkles + 1;
            if (sparkles > 199) {
                sparkles = 0;
            }
            graphics.blit(RenderPipelines.GUI_TEXTURED, SPARKLES_TEXTURE, leftPos + 80, topPos + 21, 0, (int)Math.floor(sparkles / 40.0) * 24, 24, 24, 24, 120);
        }

        extractTooltip(graphics, mouseX, mouseY);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractRenderState(graphics, mouseX, mouseY, a);


    }
}
