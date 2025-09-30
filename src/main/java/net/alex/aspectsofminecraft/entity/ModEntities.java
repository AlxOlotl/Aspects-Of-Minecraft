package net.alex.aspectsofminecraft.entity;

import net.alex.aspectsofminecraft.Aspects;
import net.alex.aspectsofminecraft.entity.custom.HagGooProjectileEntity;
import net.alex.aspectsofminecraft.entity.custom.HagfishEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import static net.minecraftforge.registries.ForgeRegistries.ENTITY_TYPES;

public class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(ENTITY_TYPES, Aspects.MOD_ID);

    //Passives
    public static final RegistryObject<EntityType<HagfishEntity>> HAGFISH =
            ENTITIES.register("hagfish",
                    () -> EntityType.Builder.of(HagfishEntity::new, MobCategory.WATER_CREATURE)
                            .sized(2f, 0.5f)
                            .build("hagfish"));
    //Hostiles

    //Neutrals

    //Bosses

    //Projectiles
    public static final RegistryObject<EntityType<HagGooProjectileEntity>> HAG_GOO_PROJECTILE =
            ENTITIES.register("hag_goo_projectile",
                    () -> EntityType.Builder.<HagGooProjectileEntity>of(HagGooProjectileEntity::new, MobCategory.MISC)
                            .sized(0.25F, 0.25F)
                            .clientTrackingRange(4)
                            .updateInterval(10)
                            .build("hag_goo_projectile"));
}
