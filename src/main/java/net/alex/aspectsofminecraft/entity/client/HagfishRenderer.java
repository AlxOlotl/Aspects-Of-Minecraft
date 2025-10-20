package net.alex.aspectsofminecraft.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.alex.aspectsofminecraft.Aspects;
import net.alex.aspectsofminecraft.entity.custom.HagfishEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class HagfishRenderer extends GeoEntityRenderer<HagfishEntity> {
    public HagfishRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new HagfishModel());
        this.shadowRadius = 0.5f; // size of shadow under entity
    }

    @Override
    public ResourceLocation getTextureLocation(HagfishEntity animatable) {
        return new ResourceLocation(Aspects.MOD_ID, "textures/entity/hagfish.png");
    }

    @Override
    public void render(HagfishEntity entity, float entityYaw, float partialTick,
                       PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        if (entity.isBaby()) {
            poseStack.scale(0.2f, 0.2f, 0.2f);
        }

        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

}
