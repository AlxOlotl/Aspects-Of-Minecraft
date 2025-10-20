package net.alex.aspectsofminecraft.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.alex.aspectsofminecraft.Aspects;
import net.alex.aspectsofminecraft.entity.custom.MantroodonEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class MantroodonRenderer extends GeoEntityRenderer<MantroodonEntity> {
    private static final ResourceLocation WILD =
            new ResourceLocation(Aspects.MOD_ID, "textures/entity/mantroodon.png");
    private static final ResourceLocation WILD_SHEARED =
            new ResourceLocation(Aspects.MOD_ID, "textures/entity/mantroodon_sheared.png");
    private static final ResourceLocation TAME =
            new ResourceLocation(Aspects.MOD_ID, "textures/entity/mantroodon_tame.png");
    private static final ResourceLocation TAME_SHEARED =
            new ResourceLocation(Aspects.MOD_ID, "textures/entity/mantroodon_sheared_tame.png");
    private static final ResourceLocation COLLAR =
            new ResourceLocation(Aspects.MOD_ID, "textures/entity/mantroodon_collar.png");

    public MantroodonRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new MantroodonModel());
        this.shadowRadius = 0.7f;

        addRenderLayer(new GeoRenderLayer<>(this) {
            @Override
            public void render(PoseStack poseStack, MantroodonEntity entity, BakedGeoModel model,
                               RenderType renderType, MultiBufferSource bufferSource,
                               VertexConsumer buffer, float partialTick,
                               int packedLight, int packedOverlay) {

                if (entity.isTame() && !entity.isInvisible()) {
                    int colorId = entity.getCollarColor();
                    float[] rgb = DyeColor.byId(colorId).getTextureDiffuseColors();

                    RenderType collarType = RenderType.entityCutoutNoCull(COLLAR);
                    VertexConsumer collarBuffer = bufferSource.getBuffer(collarType);

                    getRenderer().actuallyRender(
                            poseStack,
                            entity,
                            model,
                            collarType,
                            bufferSource,
                            collarBuffer,
                            true,
                            partialTick,
                            packedLight,
                            packedOverlay,
                            rgb[0], rgb[1], rgb[2], 1.0f
                    );
                }
            }
        });
    }

    @Override
    public ResourceLocation getTextureLocation(MantroodonEntity entity) {
        if (entity.isTame()) {
            return entity.isSheared() ? TAME_SHEARED : TAME;
        } else {
            return entity.isSheared() ? WILD_SHEARED : WILD;
        }
    }
}
