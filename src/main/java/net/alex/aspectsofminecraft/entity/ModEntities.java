package net.alex.aspectsofminecraft.entity;

import net.alex.aspectsofminecraft.Aspects;
import net.alex.aspectsofminecraft.entity.custom.HagGooProjectileEntity;
import net.alex.aspectsofminecraft.entity.custom.HagfishEntity;
import net.alex.aspectsofminecraft.entity.custom.MantroodonEntity;
import net.alex.aspectsofminecraft.entity.custom.RootMoleEntity;
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
                    () -> EntityType.Builder.<HagfishEntity>of(HagfishEntity::new, MobCategory.WATER_CREATURE)
                            .sized(0.8F, 0.6F)
                            .build(new ResourceLocation(Aspects.MOD_ID, "hagfish").toString()));

    public static final RegistryObject<EntityType<RootMoleEntity>> ROOT_MOLE =
            ENTITY_TYPES.register("root_mole",
                    () -> EntityType.Builder.of(RootMoleEntity::new, MobCategory.CREATURE)
                            .sized(0.6F, 0.6F)
                            .build("root_mole"));


    //Hostiles

    //Neutrals
    public static final RegistryObject<EntityType<MantroodonEntity>> MANTROODON =
            ENTITY_TYPES.register("mantroodon",
                    () -> EntityType.Builder.of(MantroodonEntity::new, MobCategory.CREATURE)
                            .sized(0.9F, 1.5F)
                            .build("mantroodon"));

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
