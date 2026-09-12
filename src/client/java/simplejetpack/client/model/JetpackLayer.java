package simplejetpack.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EquipmentLayerRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.core.component.DataComponents;
import simplejetpack.SimpleJetpack;

public class JetpackLayer<S extends HumanoidRenderState, M extends EntityModel<S>> extends RenderLayer<S, M> {
    public static final ModelLayerLocation JETPACK = createModelLayerLocation("jetpack");
    private final JetpackModel jetpackModel;
    private final EquipmentLayerRenderer equipmentRenderer;

    public JetpackLayer(RenderLayerParent<S, M> renderer, final EntityModelSet modelSet, final EquipmentLayerRenderer equipmentRenderer) {
        super(renderer);
        this.jetpackModel = new JetpackModel(modelSet.bakeLayer(JETPACK));
        this.equipmentRenderer = equipmentRenderer;
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, S state, float yRot, float xRot) {
        var itemStack = state.chestEquipment;
        var equippable = itemStack.get(DataComponents.EQUIPPABLE);
        if (equippable != null && !equippable.assetId().isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(0.0F, 0.0F, 0.125F);
            var jetpackTexture = SimpleJetpack.id("textures/entity/equipment/humanoid/jetpack.png");
            this.equipmentRenderer
                    .renderLayers(
                            EquipmentClientInfo.LayerType.HUMANOID,
                            equippable.assetId().get(),
                            this.jetpackModel,
                            (AvatarRenderState)state,
                            itemStack,
                            poseStack,
                            submitNodeCollector,
                            lightCoords,
                            jetpackTexture,
                            state.outlineColor,
                            0
                    );

            poseStack.popPose();
        }
    }

    private static ModelLayerLocation createModelLayerLocation(String model) {
        return new ModelLayerLocation(SimpleJetpack.id(model), "main");
    }

}
