package net.alex.aspectsofminecraft.entity;

import net.alex.aspectsofminecraft.Aspects;
import net.alex.aspectsofminecraft.entity.custom.HagGooProjectileEntity;
import net.alex.aspectsofminecraft.entity.custom.HagfishEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Aspects.MOD_ID);

    //Passives
    public static final RegistryObject<EntityType<HagfishEntity>> HAGFISH =
            ENTITY_TYPES.register("hagfish",
                    () -> EntityType.Builder.of(HagfishEntity::new, MobCategory.WATER_CREATURE)
                            .sized(0.5f, 0.5f)
                            .build(new ResourceLocation(Aspects.MOD_ID, "hagfish").toString()));

    //Hostiles

    //Neutrals

    //Bosses

    //Projectiles
    public static final RegistryObject<EntityType<HagGooProjectileEntity>> HAG_GOO_PROJECTILE =
            ENTITY_TYPES.register("hag_goo_projectile",
                    () -> EntityType.Builder.<HagGooProjectileEntity>of(HagGooProjectileEntity::new, MobCategory.MISC)
                            .sized(0.25F, 0.25F)
                            .clientTrackingRange(4)
                            .updateInterval(10)
                            .build("hag_goo_projectile"));

    public static void register(IEventBus eventBus) {ENTITY_TYPES.register(eventBus);}
}
