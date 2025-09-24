package net.alex.aspectsofminecraft.entity;

import net.alex.aspectsofminecraft.Aspects;
import net.alex.aspectsofminecraft.entity.custom.HagGooProjectile;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Aspects.MODID);

    public static final RegistryObject<EntityType<HagGooProjectile>> HAG_GOO_PROJECTILE =
            ENTITIES.register("hag_goo_projectile", () ->
                    EntityType.Builder.<HagGooProjectile>of(HagGooProjectile::new, MobCategory.MISC)
                            .sized(0.25f, 0.25f)
                            .clientTrackingRange(4)
                            .updateInterval(10)
                            .build("hag_goo_projectile"));
}
