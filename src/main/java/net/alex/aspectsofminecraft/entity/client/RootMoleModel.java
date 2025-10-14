package net.alex.aspectsofminecraft.entity.client;

import net.alex.aspectsofminecraft.Aspects;
import net.alex.aspectsofminecraft.entity.custom.RootMoleEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class RootMoleModel extends GeoModel<RootMoleEntity> {
    @Override
    public ResourceLocation getModelResource(RootMoleEntity object) {
        return new ResourceLocation(Aspects.MOD_ID, "geo/root_mole.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(RootMoleEntity object) {
        return new ResourceLocation(Aspects.MOD_ID, "textures/entity/root_mole.png");
    }

    @Override
    public ResourceLocation getAnimationResource(RootMoleEntity object) {
        return new ResourceLocation(Aspects.MOD_ID, "animations/root_mole.animation.json");
    }
}
