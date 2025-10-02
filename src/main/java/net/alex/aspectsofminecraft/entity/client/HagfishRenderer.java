package net.alex.aspectsofminecraft.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.alex.aspectsofminecraft.Aspects;
import net.alex.aspectsofminecraft.entity.custom.HagfishEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class HagfishRenderer extends GeoEntityRenderer<HagfishEntity> {
    public HagfishRenderer(EntityRendererProvider.Context context) {
        super(context, new HagfishModel());
    }

    @Override
    public ResourceLocation getTextureLocation(HagfishEntity animatable) {
        return new ResourceLocation(Aspects.MOD_ID, "textures/entity/hagfish.png");
    }

    @Override
    public void render(HagfishEntity entity, float entityYaw, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight) {
        if(entity.isBaby()) {
            poseStack.scale(0.4f,0.4f,0.4f);
        }

        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}
