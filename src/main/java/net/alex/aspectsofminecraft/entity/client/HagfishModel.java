package net.alex.aspectsofminecraft.entity.client;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;

public class HagfishModel<T extends Entity> extends HierarchicalModel<T> {

    private final ModelPart hagfish;
    private final ModelPart tail;
    private final ModelPart tail2;
    private final ModelPart tail3;
    private final ModelPart tail4;
    private final ModelPart tail5;
    private final ModelPart tail6;

    public HagfishModel(ModelPart root) {
        this.hagfish = root.getChild("hagfish");
        this.tail = this.hagfish.getChild("tail");
        this.tail2 = this.tail.getChild("tail2");
        this.tail3 = this.tail2.getChild("tail3");
        this.tail4 = this.tail3.getChild("tail4");
        this.tail5 = this.tail4.getChild("tail5");
        this.tail6 = this.tail5.getChild("tail6");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition hagfish = partdefinition.addOrReplaceChild("hagfish", CubeListBuilder.create().texOffs(19, 0).addBox(-1.5F, -1.5F, -5.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-2.5F, -2.0F, -4.0F, 5.0F, 4.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(37, 26).addBox(-1.5F, 2.0F, -3.75F, 3.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 21.0F, -20.0F));

        PartDefinition tail = hagfish.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(0, 13).addBox(-2.0F, -2.0F, 0.0F, 4.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 4.0F));

        PartDefinition tail2 = tail.addOrReplaceChild("tail2", CubeListBuilder.create().texOffs(17, 18).addBox(-2.0F, -2.0F, 0.0F, 4.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 8.0F));

        PartDefinition tail3 = tail2.addOrReplaceChild("tail3", CubeListBuilder.create().texOffs(21, 31).addBox(-1.5F, -2.0F, 0.0F, 3.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 8.0F));

        PartDefinition tail4 = tail3.addOrReplaceChild("tail4", CubeListBuilder.create().texOffs(19, 5).addBox(-1.5F, -2.0F, 0.0F, 3.0F, 4.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(0, 42).addBox(0.0F, -2.5F, 0.0F, 0.0F, 5.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 8.0F));

        PartDefinition tail5 = tail4.addOrReplaceChild("tail5", CubeListBuilder.create().texOffs(34, 13).addBox(-1.0F, -2.0F, 0.0F, 2.0F, 4.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(36, 36).addBox(0.0F, -2.5F, 0.0F, 0.0F, 5.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 8.0F));

        PartDefinition tail6 = tail5.addOrReplaceChild("tail6", CubeListBuilder.create().texOffs(34, 0).addBox(-1.0F, -2.0F, 0.0F, 2.0F, 4.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(0, 26).addBox(0.0F, -2.5F, 0.0F, 0.0F, 5.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 8.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        hagfish.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public ModelPart root() {
        return hagfish;
    }
}