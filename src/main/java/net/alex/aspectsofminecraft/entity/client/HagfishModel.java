package net.alex.aspectsofminecraft.entity.client;

import net.alex.aspectsofminecraft.Aspects;
import net.alex.aspectsofminecraft.entity.custom.HagfishEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.core.animation.AnimationState;
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
    @Override
    public void setCustomAnimations(HagfishEntity entity, long uniqueID, AnimationState<HagfishEntity> state) {
        super.setCustomAnimations(entity, uniqueID, state);

        float partialTick = state.getPartialTick();
        float turn = Mth.lerp(partialTick, entity.prevTurnAmount, entity.turnAmount);
        getBone("tail").ifPresent(b -> b.setRotY(turn * 0.1F));
        getBone("tail2").ifPresent(b -> b.setRotY(turn * 0.2F));
        getBone("tail3").ifPresent(b -> b.setRotY(turn * 0.3F));
        getBone("tail4").ifPresent(b -> b.setRotY(turn * 0.4F));
        getBone("tail5").ifPresent(b -> b.setRotY(turn * 0.5F));
        getBone("tail6").ifPresent(b -> b.setRotY(turn * 0.6F));
    }

}
