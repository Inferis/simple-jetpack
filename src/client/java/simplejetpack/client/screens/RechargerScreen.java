package simplejetpack.client.screens;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import simplejetpack.SimpleJetpack;
import simplejetpack.menu.RechargerMenu;

public class RechargerScreen extends AbstractContainerScreen<RechargerMenu> {
    public static final Identifier BACKGROUND_TEXTURE = SimpleJetpack.id("textures/gui/container/recharger.png");
    public static final Identifier CHARGE_PROGRESS_TEXTURE = SimpleJetpack.id("container/recharger/charge_progress");
    public static final Identifier SPARKLES_TEXTURE = SimpleJetpack.id("container/recharger/sparkles");

    public RechargerScreen(RechargerMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractBackground(graphics, mouseX, mouseY, a);
        graphics.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND_TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight, 256, 256 );
    }
}
