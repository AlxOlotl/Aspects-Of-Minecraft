package net.alex.aspectsofminecraft.entity.client;

import net.alex.aspectsofminecraft.Aspects;
import net.alex.aspectsofminecraft.entity.custom.MantroodonEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class MantroodonModel extends GeoModel<MantroodonEntity> {
    @Override
    public ResourceLocation getModelResource(MantroodonEntity animatable) {
        return new ResourceLocation(Aspects.MOD_ID, "geo/mantroodon.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(MantroodonEntity animatable) {
        return new ResourceLocation(Aspects.MOD_ID, "textures/entity/mantroodon.png");
    }

    @Override
    public ResourceLocation getAnimationResource(MantroodonEntity animatable) {
        return new ResourceLocation(Aspects.MOD_ID, "animations/mantroodon.animation.json");
    }
}
