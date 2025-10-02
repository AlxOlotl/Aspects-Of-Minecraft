package net.alex.aspectsofminecraft.entity.client;

import net.alex.aspectsofminecraft.Aspects;
import net.alex.aspectsofminecraft.entity.custom.HagfishEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class HagfishModel extends GeoModel<HagfishEntity> {
    @Override
    public ResourceLocation getModelResource(HagfishEntity hagfishEntity) {
        return new ResourceLocation(Aspects.MOD_ID, "geo/hagfish.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(HagfishEntity hagfishEntity) {
        return new ResourceLocation(Aspects.MOD_ID, "textures/entity/hagfish.png");
    }

    @Override
    public ResourceLocation getAnimationResource(HagfishEntity hagfishEntity) {
        return new ResourceLocation(Aspects.MOD_ID, "animations/hagfish.animation.json");
    }

    @Override
    public void setCustomAnimations(HagfishEntity animatable, long instanceId, AnimationState<HagfishEntity> animationState) {
        CoreGeoBone head = getAnimationProcessor().getBone("hagfish");

        if (head != null) {
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

            head.setRotX(entityData.headPitch() * Mth.DEG_TO_RAD);
            head.setRotY(entityData.netHeadYaw() * Mth.DEG_TO_RAD);
        }
    }
}
