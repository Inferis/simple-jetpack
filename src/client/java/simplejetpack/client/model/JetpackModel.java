package simplejetpack.client.model;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;

public class JetpackModel extends EntityModel<HumanoidRenderState> {
    public JetpackModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition createLayer() {
        var mesh = HumanoidModel.createMesh(new CubeDeformation(1.0F), 0F);
        var root = mesh.getRoot();
        var body = root.getChild("body");

        body.addOrReplaceChild("canister_left", CubeListBuilder.create()
                .texOffs(64, 0)
                .addBox(1F, -2F, 1.0F, 4.0F, 14.0F, 4.0F),
                PartPose.ZERO);
        body.addOrReplaceChild("canister_right", CubeListBuilder.create()
                        .texOffs(64, 0)
                        .addBox(-5F, -2F, 1.0F, 4.0F, 14.0F, 4.0F),
                PartPose.ZERO);
        body.addOrReplaceChild("thruster_left", CubeListBuilder.create()
                        .texOffs(80, 0)
                        .addBox(2F, 12F, 2F, 2F, 1F, 2F),
                PartPose.ZERO);
        body.addOrReplaceChild("thruster_right", CubeListBuilder.create()
                        .texOffs(80, 0)
                        .addBox(-4F, 12F, 2F, 2F, 1F, 2F),
                PartPose.ZERO);
        body.addOrReplaceChild("center", CubeListBuilder.create()
                        .texOffs(96, 0)
                        .addBox(-4F, 1F, 0.5F, 8.0F, 8.0F, 6.0F),
                PartPose.ZERO);

        return LayerDefinition.create(mesh, 128, 64);
    }

//    public static TexturedModelData getTexturedModelData() {
//        var root = new ModelPart(new ArrayList<>({ }));
//        var modelData = new Model.Simple(root, id -> { return RenderTypes.solidMovingBlock(); });
//
//        var modelData = BipedEntityModel.getModelData(Dilation.NONE, 0.0F);
//        var root = modelData.getRoot();
//        var head = root.addChild("head");
//        head.addChild("hat");
//        var body = root.addChild("body");
//        root.addChild("left_arm");
//        root.addChild("right_arm");
//        root.addChild("left_leg");
//        root.addChild("right_leg");
//        var canisterBuilder = ModelPartBuilder.create().uv(0, 0).cuboid(-3.0F, 0F, -3.0F, 2.0F, 7.0F, 2.0F, new Dilation(1.0F));
//        body.addChild("canister_left", canisterBuilder, ModelTransform.pivot(-0.5F, 0, 6.0F));
//        body.addChild("canister_right", canisterBuilder, ModelTransform.pivot(4.5F, 0, 6.0F));
//        var thrusterBuilder = ModelPartBuilder.create().uv(8, 0).cuboid(-2.5F, 0F, -2.5F, 1F, 0.5F, 1.0F, new Dilation(1.0F));
//        body.addChild("thruster_left", thrusterBuilder, ModelTransform.pivot(-0.5F, 7.5F, 6.0F));
//        body.addChild("thruster_right", thrusterBuilder, ModelTransform.pivot(4.5F, 7.5F, 6.0F));
//        var centerBuilder = ModelPartBuilder.create().uv(0, 12).cuboid(-4.0F, 0.5F, -3.5F, 4.0F, 4.0F, 3.0F, new Dilation(1.0F));
//        body.addChild("center", centerBuilder, ModelTransform.pivot(2F, 1F, 6.5F));
//        return TexturedModelData.of(modelData, 32, 32);
//    }
}
