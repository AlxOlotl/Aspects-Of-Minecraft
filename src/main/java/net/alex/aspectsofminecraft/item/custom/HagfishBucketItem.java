package net.alex.aspectsofminecraft.item.custom;

import net.alex.aspectsofminecraft.entity.ModEntities;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.MobBucketItem;
import net.minecraft.world.level.material.Fluids;

public class HagfishBucketItem extends MobBucketItem {
    public HagfishBucketItem(Item.Properties properties) {
        super(ModEntities.HAGFISH, () -> Fluids.WATER, () -> SoundEvents.BUCKET_EMPTY_FISH, properties);

    }
}
