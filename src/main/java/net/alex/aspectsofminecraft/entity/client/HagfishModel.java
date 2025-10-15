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
        float baseTurn = Mth.lerp(partialTick, entity.prevTurnAmount, entity.turnAmount);

        int len = entity.turnHistory.length;
        for (int i = 0; i < len; i++) {
            int index = (entity.turnHistoryIndex - 1 - i + len) % len;
            float delayedTurn = entity.turnHistory[index];
            float strength = 0.05F * (i + 1);

            switch (i) {
                case 0 -> getBone("tail").ifPresent(b -> b.setRotY(b.getRotY() + delayedTurn * strength));
                case 1 -> getBone("tail2").ifPresent(b -> b.setRotY(b.getRotY() + delayedTurn * strength));
                case 2 -> getBone("tail3").ifPresent(b -> b.setRotY(b.getRotY() + delayedTurn * strength));
                case 3 -> getBone("tail4").ifPresent(b -> b.setRotY(b.getRotY() + delayedTurn * strength));
                case 4 -> getBone("tail5").ifPresent(b -> b.setRotY(b.getRotY() + delayedTurn * strength));
                case 5 -> getBone("tail6").ifPresent(b -> b.setRotY(b.getRotY() + delayedTurn * strength));
            }
        }
    }
}
