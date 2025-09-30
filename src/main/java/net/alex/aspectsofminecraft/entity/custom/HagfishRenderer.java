package net.alex.aspectsofminecraft.entity.custom;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class HagfishRenderer extends GeoEntityRenderer<HagfishEntity> {
    public HagfishRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new HagfishModel());
        this.shadowRadius = 0.5f;
    }
}
