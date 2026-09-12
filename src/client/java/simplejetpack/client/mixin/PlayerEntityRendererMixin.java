package simplejetpack.client.mixin;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import simplejetpack.client.model.JetpackLayer;
import simplejetpack.client.model.JetpackModel;

import java.util.HashMap;
import java.util.Map;

@Mixin(AvatarRenderer.class)
public class PlayerEntityRendererMixin<S extends AvatarRenderState, M extends EntityModel<? super S>> {
    @Inject(at=@At("TAIL"), method="<init>")
    public void init(EntityRendererProvider.Context context, boolean slimSteve, CallbackInfo ci) {
        var renderer = (AvatarRenderer)(Object)this;

        Map<ModelLayerLocation, LayerDefinition> roots = new HashMap<ModelLayerLocation, LayerDefinition>();
        roots.put(JetpackLayer.JETPACK, JetpackModel.createLayer());
        var modelSet = new EntityModelSet(roots);
        var equipmentRenderer = context.getEquipmentRenderer();

        renderer.addLayer(new JetpackLayer<>(renderer, modelSet, equipmentRenderer));
    }
}
