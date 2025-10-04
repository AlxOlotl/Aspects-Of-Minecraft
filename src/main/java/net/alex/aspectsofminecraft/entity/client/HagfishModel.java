package net.alex.aspectsofminecraft.entity.client;

import net.alex.aspectsofminecraft.Aspects;
import net.alex.aspectsofminecraft.entity.custom.HagfishEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class HagfishModel extends GeoModel<HagfishEntity> {
    @Override
    public ResourceLocation getModelResource(HagfishEntity object) {
        return new ResourceLocation(Aspects.MOD_ID, "geo/hagfish.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(HagfishEntity object) {
        return new ResourceLocation(Aspects.MOD_ID, "textures/entity/hagfish.png");
    }

    @Override
    public ResourceLocation getAnimationResource(HagfishEntity object) {
        return new ResourceLocation(Aspects.MOD_ID, "animations/hagfish.animation.json");
    }
}
