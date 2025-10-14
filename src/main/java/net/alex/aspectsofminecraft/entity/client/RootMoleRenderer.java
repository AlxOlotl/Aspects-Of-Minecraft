package net.alex.aspectsofminecraft.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.alex.aspectsofminecraft.Aspects;
import net.alex.aspectsofminecraft.entity.custom.HagfishEntity;
import net.alex.aspectsofminecraft.entity.custom.RootMoleEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class RootMoleRenderer extends GeoEntityRenderer<RootMoleEntity> {
    public RootMoleRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new RootMoleModel());
        this.shadowRadius = 0.5f;
    }

    @Override
    public ResourceLocation getTextureLocation(RootMoleEntity animatable) {
        return new ResourceLocation(Aspects.MOD_ID, "textures/entity/root_mole.png");
    }

    @Override
    public void render(RootMoleEntity entity, float entityYaw, float partialTick,
                       PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        if (entity.isBaby()) {
            poseStack.scale(0.2f, 0.2f, 0.2f);
        }

        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}
