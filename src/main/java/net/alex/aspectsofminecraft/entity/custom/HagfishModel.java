package net.alex.aspectsofminecraft.entity.custom;

import net.alex.aspectsofminecraft.Aspects;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class HagfishModel extends GeoModel<HagfishEntity> {
    @Override
    public ResourceLocation getModelResource(HagfishEntity animatable) {
        return new ResourceLocation(Aspects.MOD_ID, "geo/hagfish.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(HagfishEntity animatable) {
        return new ResourceLocation(Aspects.MOD_ID, "textures/entity/hagfish.png");
    }

    @Override
    public ResourceLocation getAnimationResource(HagfishEntity animatable) {
        return new ResourceLocation(Aspects.MOD_ID, "animations/hagfish.animation.json");
    }
}
